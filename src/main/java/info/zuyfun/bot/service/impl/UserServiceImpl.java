package info.zuyfun.bot.service.impl;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import info.zuyfun.bot.entity.User;
import info.zuyfun.bot.repository.UserRepository;
import info.zuyfun.bot.service.UserService;

@Service
public class UserServiceImpl implements UserService {
	private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

	@Autowired
	UserRepository userRepository;

	@Override
	public User getBySenderID(BigDecimal senderID) {
		try {
			if (userRepository.existsById(senderID))
				return userRepository.getById(senderID);
		} catch (Exception e) {
			logger.error("***getByRecipientID Exception: {}", e);
		}
		return null;
	}

	@Override
	public void addUser(User user) {
		try {
			userRepository.save(user);
		} catch (Exception e) {
			logger.error("***addUser Exception: {}", e);
		}
	}

	@Override
	public void saveUser(User user) {
		try {
			userRepository.save(user);
		} catch (Exception e) {
			logger.error("***saveUser Exception: {}", e);
		}
	}

	@Override
	public boolean isChatWithBot(BigDecimal senderID) {
		try {
			User objUser = getBySenderID(senderID);
			if (objUser == null)
				return false;
			if (objUser.isChatWithBot())
				return true;
		} catch (Exception e) {
			logger.error("***isChatWithBot Exception: {}", e);
		}
		return false;
	}

	@Override
	public void updateIsChatWithBot(BigDecimal senderID, boolean isChat) {
		try {
			User objUser = getBySenderID(senderID);
			if (objUser == null)
				return;
			objUser.setChatWithBot(isChat);
			userRepository.save(objUser);
		} catch (Exception e) {
			logger.error("***isChatWithBot Exception: {}", e);
		}
	}

	@Override
	public void updateIsNotificationKQXS(BigDecimal senderID, boolean isNoti) {
		try {
			User objUser = getBySenderID(senderID);
			if (objUser == null)
				return;
			objUser.setNotificationKQXS(isNoti);
			userRepository.save(objUser);
		} catch (Exception e) {
			logger.error("***isChatWithBot Exception: {}", e);
		}
	}
}
