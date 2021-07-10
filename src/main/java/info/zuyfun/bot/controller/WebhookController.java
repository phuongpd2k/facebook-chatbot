package info.zuyfun.bot.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import info.zuyfun.bot.dto.Messaging;
import info.zuyfun.bot.dto.RequestObject;

@Controller
public class WebhookController {

	@Value("${verify_token}")
	private String VERIFY_TOKEN;

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
//				      console.log('WEBHOOK_VERIFIED');
				return new ResponseEntity<>(challenge, HttpStatus.OK);
			} else {
				return new ResponseEntity<>("FORBIDDEN", HttpStatus.FORBIDDEN);
			}
		}
		return new ResponseEntity<>("FORBIDDEN", HttpStatus.FORBIDDEN);
	}

	@PostMapping("/webhook")
	public ResponseEntity<Object> sendWebhook(@RequestBody RequestObject objReq) {
		// Checks this is an event from a page subscription
		if (objReq.getObject().equals("page")) {
			for (Messaging mess : objReq.getEntry()) {
				System.out.println(mess);
			}
			return new ResponseEntity<>("EVENT_RECEIVED", HttpStatus.OK);
		} else {
			// Returns a '404 Not Found' if event is not from a page subscription
			return new ResponseEntity<>("NOT_FOUND", HttpStatus.NOT_FOUND);
		}

	}
}
