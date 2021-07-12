package info.zuyfun.bot.template;

import org.springframework.context.annotation.Bean;

import info.zuyfun.bot.model.Action;
import info.zuyfun.bot.model.RequestRecipient;

public class Typing {
	@Bean
	public Action typingOn() {
		Action objAction = new Action();
		RequestRecipient objRecipient = new RequestRecipient();
		objAction.setReceipient(objRecipient);
		objAction.setSenderAction("typing_on");
		return objAction;
	}
}
