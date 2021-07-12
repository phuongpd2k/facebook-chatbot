package info.zuyfun.bot.service;

import java.math.BigDecimal;

import info.zuyfun.bot.model.Message;

public interface MessageHandler {
	// Handles messages events
	void handleMessage(BigDecimal senderID, Message message);

	// Handles messaging_postbacks events
	void handlePostback(BigDecimal senderID, String payload);

}
