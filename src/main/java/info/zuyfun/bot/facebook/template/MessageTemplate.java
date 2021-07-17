package info.zuyfun.bot.facebook.template;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import info.zuyfun.bot.facebook.model.Action;
import info.zuyfun.bot.facebook.model.Attachment;
import info.zuyfun.bot.facebook.model.Button;
import info.zuyfun.bot.facebook.model.Element;
import info.zuyfun.bot.facebook.model.Payload;
import info.zuyfun.bot.facebook.model.Request;
import info.zuyfun.bot.facebook.model.RequestMessage;
import info.zuyfun.bot.facebook.model.RequestRecipient;

public class MessageTemplate {

	Template template;

	@Autowired
	Typing typingTemplate;

	public MessageTemplate() {
		template = new Template();
	}

	public Attachment testPayload(String imageUrl) {
		Attachment objAttachment = new Attachment();
		objAttachment.setType("template");
		objAttachment.setPayload(template.payloadPostBack());
		List<Element> listElement = new ArrayList<Element>();
		objAttachment.getPayload().setElements(listElement);
		objAttachment.getPayload().getElements()
				.add(template.elementPostBack("Is this the right picture?", "Tap a button to answer.", imageUrl));
		List<Button> listButton = new ArrayList<Button>();
		objAttachment.getPayload().getElements().get(0).setButtons(listButton);
		listButton.add(template.buttonPostBack("Yes!", "yes"));
		listButton.add(template.buttonPostBack("No!", "no"));
		return objAttachment;
	}

	public Action typingAction(BigDecimal senderID, String action) {
		return typingTemplate.typingAction(senderID, action);
	}

	public Request sendText(BigDecimal recipentID, String textMessage) {
		Request objRequest = new Request();
		RequestRecipient objRequestRecipient = new RequestRecipient();
		objRequestRecipient.setId(recipentID);
		objRequest.setRequestRecipient(objRequestRecipient);
		RequestMessage objRequestMessage = new RequestMessage();
		objRequest.setRequestMessage(objRequestMessage);
		objRequestMessage.setText(textMessage);
		return objRequest;
	}

	public Request sendAttachment(BigDecimal recipentID, Attachment attachmentMessage) {
		Request objRequest = new Request();
		RequestRecipient objRequestRecipient = new RequestRecipient();
		objRequestRecipient.setId(recipentID);
		objRequest.setRequestRecipient(objRequestRecipient);
		RequestMessage objRequestMessage = new RequestMessage();
		objRequest.setRequestMessage(objRequestMessage);
		objRequestMessage.setAttachment(attachmentMessage);
		return objRequest;
	}

}

class Template {
	public Button buttonPostBack(String title, String payload) {
		Button objButton = new Button();
		objButton.setType("postback");
		objButton.setTitle(title);
		objButton.setPayload(payload);
		return objButton;
	}

	public Element elementPostBack(String title, String subTitle, String imageUrl) {
		Element objElement = new Element();
		objElement.setTitle(title);
		objElement.setSubtitle(subTitle);
		objElement.setImageUrl(imageUrl);
		return objElement;
	}

	public Payload payloadPostBack() {
		Payload objPayload = new Payload();
		objPayload.setTemplateType("generic");
		return objPayload;
	}

	public Attachment attachmentPostback() {
		Attachment objAttachment = new Attachment();
		objAttachment.setType("template");
		objAttachment.setPayload(payloadPostBack());
		List<Element> listElement = new ArrayList<Element>();
		objAttachment.getPayload().setElements(listElement);
		return objAttachment;
	}
}
