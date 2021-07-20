package info.zuyfun.bot.facebook.template;

import java.math.BigDecimal;

import info.zuyfun.bot.facebook.model.Action;
import info.zuyfun.bot.facebook.model.RequestRecipient;

public class Typing {
	public Action typingAction(BigDecimal id, String action) {
		Action objAction = new Action();
		RequestRecipient objRecipient = new RequestRecipient();
		objAction.setReceipient(objRecipient);
		objAction.getReceipient().setId(id);
		objAction.setSenderAction(action);
		return objAction;
	}
}
