package info.zuyfun.bot.service;

import java.math.BigDecimal;

import info.zuyfun.bot.entity.User;

public interface UserService {
	User getBySenderID(BigDecimal senderID);

	void addUser(User user);

	void saveUser(User user);

	boolean isChatWithBot(BigDecimal senderID);

	void updateIsChatWithBot(BigDecimal senderID);

	void updateIsNotificationKQXS(BigDecimal senderID);
}
