package info.zuyfun.bot.facebook.template;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import info.zuyfun.bot.facebook.model.Attachment;
import info.zuyfun.bot.facebook.model.Button;
import info.zuyfun.bot.facebook.model.Element;
import info.zuyfun.bot.facebook.model.Payload;

public class MessageTemplate {
	private static final Logger logger = LoggerFactory.getLogger(MessageTemplate.class);

	Template template;

	public MessageTemplate() {
		template = new Template();
	}

	public Attachment testPayload(String imageUrl) {
		template = new Template();
		Attachment objAttachment = template.attachmentPostback();
		logger.info("***objAttachment: {}", objAttachment);
		List<Element> listElement = new ArrayList<Element>();
		objAttachment.getPayload().setElements(listElement);
		listElement.add(template.elementPostBack("Is this the right picture?", "Tap a button to answer.", imageUrl));
		List<Button> listButton = new ArrayList<Button>();
		objAttachment.getPayload().getElements().get(0).setButtons(listButton);
		listButton.add(template.buttonPostBack("Yes!", "yes"));
		listButton.add(template.buttonPostBack("No!", "no"));
		return objAttachment;
	}
}

class Template {
	public Button buttonPostBack(String title, String payload) {
		Button objButton = new Button();
		objButton.setType("postback");
		objButton.setTitle(title);
		objButton.setPayload(payload);
		return null;
	}

	public Element elementPostBack(String title, String subTitle, String imageUrl) {
		Element objElement = new Element();
		objElement.setTitle(title);
		objElement.setSubtitle(subTitle);
		objElement.setImageUrl(imageUrl);
		return null;
	}

	public Payload payloadPostBack() {
		Payload objPayload = new Payload();
		objPayload.setTemplateType("generic");
		return objPayload;
	}

	public Attachment attachmentPostback() {
		Attachment objAttachment = new Attachment();
		objAttachment.setType("template");
		return null;
	}
}
