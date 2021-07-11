package info.zuyfun.bot.controller;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Queue;
import java.util.regex.Matcher;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import info.zuyfun.bot.common.BaseBot;
import info.zuyfun.bot.constants.EventType;
import info.zuyfun.bot.model.Callback;
import info.zuyfun.bot.model.Entry;
import info.zuyfun.bot.model.Event;
import info.zuyfun.bot.service.EventHandler;

@Controller
public class BotController extends BaseBot {
	private static final Logger logger = LoggerFactory.getLogger(BotController.class);
	@Value("${verify_token}")
	private String VERIFY_TOKEN;

	@Autowired
	private EventHandler service;

	@GetMapping("/webhook")
	public ResponseEntity<Object> verifyWebhook(HttpServletRequest req) {
		// Parse the query params
		String mode = req.getParameter("hub.mode") == null ? "" : req.getParameter("hub.mode");
		String token = req.getParameter("hub.verify_token") == null ? "" : req.getParameter("hub.verify_token");
		String challenge = req.getParameter("hub.challenge") == null ? "" : req.getParameter("hub.challenge");
		// Checks if a token and mode is in the query string of the request
		if (!mode.isEmpty() && !token.isEmpty()) {
			// Checks the mode and token sent is correct
			if (mode.equals("subscribe") && token.equals(VERIFY_TOKEN)) {
				// Responds with the challenge token from the request
				return ResponseEntity.ok(challenge);
			}
		}
		return new ResponseEntity<>(HttpStatus.FORBIDDEN);
	}

	@ResponseBody
	@PostMapping("/webhook")
	public ResponseEntity<Object> webhookEndpoint(@RequestBody Callback callback) {

		try {
			// Checks this is an event from a page subscription
			if (!callback.getObject().equals("page")) {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
			logger.info("Callback from fb: {}", callback);
			for (Entry entry : callback.getEntry()) {
				if (entry.getMessaging() != null) {
					for (Event event : entry.getMessaging()) {
						if (event.getMessage() != null) {
							if (event.getMessage().isEcho() != null && event.getMessage().isEcho()) {
								event.setType(EventType.MESSAGE_ECHO);
							} else if (event.getMessage().getQuickReply() != null) {
								event.setType(EventType.QUICK_REPLY);
							} else {
								event.setType(EventType.MESSAGE);
								// send typing on indicator to create a conversational experience
								service.handleMessage(event);
							}
						} else if (event.getDelivery() != null) {
							event.setType(EventType.MESSAGE_DELIVERED);
						} else if (event.getRead() != null) {
							event.setType(EventType.MESSAGE_READ);
						} else if (event.getPostback() != null) {
							event.setType(EventType.POSTBACK);
						} else {
							logger.info("Callback/Event type not supported: {}", event);
							return ResponseEntity.ok("Callback not supported yet!");
						}
						if (isConversationOn(event)) {
							invokeChainedMethod(event);
						} else {
							invokeMethods(event);
						}
						logger.info("Event type : {}", event);
					}
				}
			}

		} catch (Exception e) {
			logger.error("Error in fb webhook: Callback: {} \nException: ", callback.toString(), e);
		}
		// fb advises to send a 200 response within 20 secs
		return ResponseEntity.ok("EVENT_RECEIVED");
	}

	protected final void startConversation(Event event, String methodName) {
		startConversation(event.getSender().getId(), methodName);
	}

	protected final void nextConversation(Event event) {
		nextConversation(event.getSender().getId());
	}

	protected final void stopConversation(Event event) {
		stopConversation(event.getSender().getId());
	}

	protected final boolean isConversationOn(Event event) {
		return isConversationOn(event.getSender().getId());
	}

	private void invokeMethods(Event event) {
		try {
			List<MethodWrapper> methodWrappers = eventToMethodsMap.get(event.getType().name().toUpperCase());
			if (methodWrappers == null)
				return;

			methodWrappers = new ArrayList<>(methodWrappers);
			MethodWrapper matchedMethod = getMethodWithMatchingPatternAndFilterUnmatchedMethods(
					getPatternFromEventType(event), methodWrappers);
			if (matchedMethod != null) {
				methodWrappers = new ArrayList<>();
				methodWrappers.add(matchedMethod);
			}

			for (MethodWrapper methodWrapper : methodWrappers) {
				Method method = methodWrapper.getMethod();
				if (Arrays.asList(method.getParameterTypes()).contains(Matcher.class)) {
					method.invoke(this, event, methodWrapper.getMatcher());
				} else {
					method.invoke(this, event);
				}
			}
		} catch (Exception e) {
			logger.error("Error invoking controller: ", e);
		}
	}

	private void invokeChainedMethod(Event event) {
		Queue<MethodWrapper> queue = conversationQueueMap.get(event.getSender().getId());

		if (queue != null && !queue.isEmpty()) {
			MethodWrapper methodWrapper = queue.peek();
			try {
				EventType[] eventTypes = methodWrapper.getMethod()
						.getAnnotation(info.zuyfun.bot.common.Controller.class).events();
				for (EventType eventType : eventTypes) {
					if (eventType.name().equalsIgnoreCase(event.getType().name())) {
						methodWrapper.getMethod().invoke(this, event);
						return;
					}
				}
			} catch (Exception e) {
				logger.error("Error invoking chained method: ", e);
			}
		}
	}

	private String getPatternFromEventType(Event event) {
		switch (event.getType()) {
		case MESSAGE:
			return event.getMessage().getText();
		case QUICK_REPLY:
			return event.getMessage().getQuickReply().getPayload();
		case POSTBACK:
			return event.getPostback().getPayload();
		default:
			return event.getMessage().getText();
		}
	}

}
