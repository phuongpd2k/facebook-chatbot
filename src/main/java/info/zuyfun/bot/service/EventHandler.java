package info.zuyfun.bot.service;

import info.zuyfun.bot.model.Event;
import info.zuyfun.bot.model.Request;

public interface EventHandler {
	// Handles messages events
	void handleMessage(Event event);

	// Handles messaging_postbacks events
	void handlePostback(Event event);

	// Sends response messages via the Send API
	void callSendAPI(Object objRequest);

	String callSimsimi(String messageText);
}
