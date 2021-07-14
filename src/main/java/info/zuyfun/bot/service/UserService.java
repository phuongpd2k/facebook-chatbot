package info.zuyfun.bot.service;

import java.math.BigDecimal;

import info.zuyfun.bot.entity.User;

public interface UserService {
	User getByRecipientID(BigDecimal recipientID);

	void addUser(User user);

	void saveUser(User user);
}
