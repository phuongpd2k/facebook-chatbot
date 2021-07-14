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
	public User getByRecipientID(BigDecimal recipientID) {
		try {
			return userRepository.getById(recipientID);
		} catch (Exception e) {
			logger.error("***getByRecipientID Exception: {}", e);
			return null;
		}
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
}
