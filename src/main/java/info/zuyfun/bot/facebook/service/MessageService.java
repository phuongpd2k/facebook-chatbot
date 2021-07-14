package info.zuyfun.bot.facebook.service;

import java.math.BigDecimal;

import info.zuyfun.bot.facebook.model.Message;

public interface MessageService {
	// Handles messages events
	void handleMessage(BigDecimal senderID, Message message);

	// Handles messaging_postbacks events
	void handlePostback(BigDecimal senderID, String payload);

	void typingAction(BigDecimal senderID);
}
