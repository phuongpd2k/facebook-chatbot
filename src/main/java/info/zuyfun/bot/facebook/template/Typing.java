package info.zuyfun.bot.facebook.template;

import java.math.BigDecimal;

import info.zuyfun.bot.facebook.model.Action;
import info.zuyfun.bot.facebook.model.RequestRecipient;

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
