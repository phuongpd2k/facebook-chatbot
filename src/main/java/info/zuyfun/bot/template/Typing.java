package info.zuyfun.bot.template;

import java.math.BigDecimal;

import info.zuyfun.bot.model.Action;
import info.zuyfun.bot.model.RequestRecipient;

public class Typing {
	public Action typingOn(BigDecimal id) {
		Action objAction = new Action();
		RequestRecipient objRecipient = new RequestRecipient();
		objAction.setReceipient(objRecipient);
		objAction.getReceipient().setId(id);
		objAction.setSenderAction("typing_on");
		return objAction;
	}
}
