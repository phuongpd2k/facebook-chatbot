package info.zuyfun.bot.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ValidateAction {
	private static final Logger logger = LoggerFactory.getLogger(ValidateAction.class);

	public boolean isCommand(String textMessage) {
		if (textMessage.trim().charAt(0) == '/')
			return true;
		return false;
	}

}
