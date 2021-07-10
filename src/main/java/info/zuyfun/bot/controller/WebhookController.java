package info.zuyfun.bot.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import info.zuyfun.bot.service.EventHandler;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class WebhookController {

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
				return new ResponseEntity<>(challenge, HttpStatus.OK);
			} else {
				return new ResponseEntity<>("FORBIDDEN", HttpStatus.FORBIDDEN);
			}
		}
		return new ResponseEntity<>("FORBIDDEN", HttpStatus.FORBIDDEN);
	}

	@PostMapping("/webhook")
	public ResponseEntity<Object> sendWebhook(@RequestBody String objReq) {
		// Checks this is an event from a page subscription
		JsonObject objJson = new JsonObject(objReq);
		if (objJson.getString("object", "").equals("page")) {
			JsonArray objJsonArray = objJson.getJsonArray("entry");
			for (int i = 0; i < objJsonArray.size(); i++) {
				JsonObject objEntry = objJsonArray.getJsonObject(i);
				String senderID = objEntry.getJsonArray("messaging").getJsonObject(0).getJsonObject("sender")
						.getString("id");

				JsonObject message = objEntry.getJsonArray("messaging").getJsonObject(0).getJsonObject("message", null);
				log.info("JsonObject message Object: {}" + message);
				if (message != null && !senderID.equals("102246545033338")) {
					JsonObject postback = message.getJsonObject("attachments", null);
					service.handleMessage(senderID, message);
					if (postback != null) {
						service.handlePostback(senderID, postback);
					}
				}
			}
			log.info("JsonObject request: {}" + objReq);
			return new ResponseEntity<>("EVENT_RECEIVED", HttpStatus.OK);
		} else {
			return new ResponseEntity<Object>("NOT_FOUND", HttpStatus.NOT_FOUND);
		}

	}

}
