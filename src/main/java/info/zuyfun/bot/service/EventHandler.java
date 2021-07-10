package info.zuyfun.bot.service;

import io.vertx.core.json.JsonObject;

public interface EventHandler {
	// Handles messages events
	void handleMessage(String sender_psid, JsonObject received_message);

	// Handles messaging_postbacks events
	void handlePostback(String sender_psid, JsonObject received_postback);

	// Sends response messages via the Send API
	void callSendAPI(String sender_psid, String response);

	String callSimsimi(String messageText);
}
