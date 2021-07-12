package info.zuyfun.bot.controller;

import java.math.BigDecimal;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import info.zuyfun.bot.model.Callback;
import info.zuyfun.bot.model.Entry;
import info.zuyfun.bot.model.Event;
import info.zuyfun.bot.service.EventHandler;

@Controller
public class BotController {
	private static final Logger logger = LoggerFactory.getLogger(BotController.class);
	@Value("${verify_token}")
	private String VERIFY_TOKEN;

	@Autowired
	private EventHandler service;

	@RequestMapping("/herokuIdle")
	public ResponseEntity<Object> herokuNotIdle() {
		return ResponseEntity.ok("SUCCESS");
	}

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
		logger.info("***webhookEndpoint***");
		try {
			// Checks this is an event from a page subscription
			if (!callback.getObject().equals("page")) {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
			for (Entry entry : callback.getEntry()) {

				if (entry.getMessaging() != null) {
					for (Event event : entry.getMessaging()) {
						BigDecimal senderID = event.getSender().getId();
						if (event.getMessage() != null) {
							service.handleMessage(senderID, event.getMessage());
						} else if (event.getPostback() != null) {
							service.handlePostback(senderID, event.getPostback().getPayload());
						} else {
							logger.info("***Callback/Event type not supported: {}", event);
							return ResponseEntity.ok("Callback not supported yet!");
						}
					}
				}
			}
		} catch (Exception e) {
			logger.error("***Error in Facebook webhook: {}", e);
		}
		return ResponseEntity.ok("EVENT_RECEIVED");
	}

}
