package info.zuyfun.bot.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ws.rs.core.Response;

import org.apache.cxf.jaxrs.client.WebClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;

import info.zuyfun.bot.model.Attachment;
import info.zuyfun.bot.model.Button;
import info.zuyfun.bot.model.Element;
import info.zuyfun.bot.model.Event;
import info.zuyfun.bot.model.Message;
import info.zuyfun.bot.model.Payload;
import info.zuyfun.bot.model.Request;
import info.zuyfun.bot.model.RequestMessage;
import info.zuyfun.bot.model.RequestRecipient;
import info.zuyfun.bot.service.EventHandler;
import io.vertx.core.json.JsonObject;

@Service
public class EventHandlerImpl implements EventHandler {
	private static final Logger logger = LoggerFactory.getLogger(EventHandlerImpl.class);
	@Value("${fb_access_token}")
	private String FB_ACCESS_TOKEN;
	@Value("${simsimi_url}")
	private String SIMSIMI_URL;

	private String fbURLSender = "https://graph.facebook.com/v2.6/me/messages?access_token=";

	private WebClient clientPool;

	private ObjectMapper mapper;

	@PostConstruct
	public void EventHandlerImpl() {
		fbURLSender += FB_ACCESS_TOKEN;
		mapper = new ObjectMapper();
	}

	@Autowired
	protected RestTemplate restTemplate;

	@Override
	public void handleMessage(Event event) {
		try {
			Message objMessage = event.getMessage();

			Request objRequest = new Request();

			RequestRecipient objRequestRecipient = new RequestRecipient();
			objRequestRecipient.setId(event.getSender().getId());
			objRequest.setRequestRecipient(objRequestRecipient);

			RequestMessage objRequestMessage = new RequestMessage();
			objRequest.setRequestMessage(objRequestMessage);
			if (objMessage.getText() != null || !objMessage.getText().isEmpty()) {
				// Object Message
				objRequestMessage.setText(objMessage.getText());
			} else if (objMessage.getAttachments() != null || objMessage.getAttachments().isEmpty()) {
				String attachmentUrl = objMessage.getAttachments().get(0).getPayload().getUrl();
				Attachment objAttachment = new Attachment();
				Payload objPayload = new Payload();
				List<Element> objElements = new ArrayList<Element>();
				List<Button> objButtons = new ArrayList<Button>();
				// Object message
				objRequestMessage.setAttachment(objAttachment);
				// Type
				objAttachment.setType("template");
				// Payload
				objPayload.setTemplateType("generic");
				objPayload.setElements(objElements);
				// Element
				for (Element element : objElements) {
					element.setTitle("Is this the right picture?");
					element.setSubtitle("Tap a button to answer.");
					element.setImageUrl(attachmentUrl);
					element.setButtons(objButtons);
					objButtons.get(0).setType("postback");
					objButtons.get(0).setTitle("Yes!");
					objButtons.get(0).setPayload("yes");
					objButtons.get(1).setType("postback");
					objButtons.get(1).setTitle("No!");
					objButtons.get(1).setPayload("no");
				}
			}
			callSendAPI(objRequest);
		} catch (Exception e) {
			logger.error("handleMessage - Exception: {}", e);
		}
	}

	@Override
	public void handlePostback(Event event) {

		logger.info("handlePostback received_postback: ");

	}

	@Override
	public void callSendAPI(Request objRequest) {
		// Construct the message body
		// Send the HTTP request to the Messenger Platform
		try {
			clientPool = WebClient.create(fbURLSender);
			logger.info("Json Object request :" + mapper.writeValueAsString(objRequest));
			Response response = clientPool.post("{" + mapper.writeValueAsString(objRequest) + "}");

			String result = response.readEntity(String.class);
			logger.info("Response Object {}", result);
		} catch (Exception e) {
			logger.error("*** callSendAPI System Error: " + e);
		}

	}

	@Override
	public String callSimsimi(String messageText) {
		logger.debug("***Call Simsimi");
		String result = "";
		try {
			clientPool = WebClient.create(SIMSIMI_URL);
			clientPool.query("text", messageText).query("lang", "vi_VN");
			Response response = clientPool.get();
			result = response.readEntity(String.class);
			logger.info("Simsimi Data response {}", result);

		} catch (Exception ex) {
			logger.error("callSimsimi - Exception: {}", ex);
		}
		return result;
	}

}
