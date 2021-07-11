package info.zuyfun.bot.controller;

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

import info.zuyfun.bot.constants.EventType;
import info.zuyfun.bot.model.Callback;
import info.zuyfun.bot.model.Entry;
import info.zuyfun.bot.model.Event;
import info.zuyfun.bot.service.EventHandler;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class BotController {
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
//								sendTypingOnIndicator(event.getSender());
							}
						} else if (event.getDelivery() != null) {
							event.setType(EventType.MESSAGE_DELIVERED);
						} else if (event.getRead() != null) {
							event.setType(EventType.MESSAGE_READ);
						} else if (event.getPostback() != null) {
							event.setType(EventType.POSTBACK);
						} else if (event.getOptin() != null) {
							event.setType(EventType.OPT_IN);
						} else if (event.getReferral() != null) {
							event.setType(EventType.REFERRAL);
						} else if (event.getAccountLinking() != null) {
							event.setType(EventType.ACCOUNT_LINKING);
						} else {
							logger.info("Callback/Event type not supported: {}", event);
							return ResponseEntity.ok("Callback not supported yet!");
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
