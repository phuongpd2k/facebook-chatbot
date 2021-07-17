package info.zuyfun.bot.facebook.service.impl;

import java.math.BigDecimal;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;

import info.zuyfun.bot.constants.ChatBotAPIUrl;
import info.zuyfun.bot.constants.CommandConstants;
import info.zuyfun.bot.constants.FacebookAPIUrl;
import info.zuyfun.bot.constants.MessageConstants;
import info.zuyfun.bot.constants.PayloadConstants;
import info.zuyfun.bot.facebook.model.Action;
import info.zuyfun.bot.facebook.model.Attachment;
import info.zuyfun.bot.facebook.model.Message;
import info.zuyfun.bot.facebook.model.Profile;
import info.zuyfun.bot.facebook.model.Request;
import info.zuyfun.bot.facebook.model.Simsimi;
import info.zuyfun.bot.facebook.service.MessageService;
import info.zuyfun.bot.facebook.template.MessageTemplate;
import info.zuyfun.bot.service.UserService;
import info.zuyfun.bot.utils.UserAction;

@Service
public class MessageServiceImpl implements MessageService {

	private static final Logger logger = LoggerFactory.getLogger(MessageServiceImpl.class);

	@Value("${fb_access_token}")
	private String FB_ACCESS_TOKEN;
	@Autowired
	UserAction userAction;
	@Autowired
	protected RestTemplate restTemplate;

	@Autowired
	MessageTemplate messageTemplate;

	@Autowired
	UserService userService;
	@Autowired
	ObjectMapper mapper;

	public void callSendAPI(Object objRequest) {
		logger.info("***API Send***");
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<Object> requestBody = new HttpEntity<>(objRequest, headers);
			logger.info("***Request Object: {}", requestBody.getBody());
			UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(FacebookAPIUrl.SEND_MESSAGE)
					.queryParam("access_token", FB_ACCESS_TOKEN);
			String uriBuilder = builder.build().encode().toUriString();
			restTemplate.exchange(uriBuilder, HttpMethod.POST, requestBody, String.class);
		} catch (Exception e) {
			logger.error("***callSendAPI Exception: {}", e);
		}

	}

	public Profile callGetUserAPI(BigDecimal senderID) {
		logger.info("***API GET PROFILE***");
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<Object> requestBody = new HttpEntity<>(headers);
			UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(FacebookAPIUrl.GET_PROFILE + senderID)
					.queryParam("fields", "first_name,last_name,profile_pic")
					.queryParam("access_token", FB_ACCESS_TOKEN);
			String uriBuilder = builder.build().encode().toUriString();
			String bodyResponse = restTemplate.exchange(uriBuilder, HttpMethod.GET, requestBody, String.class)
					.getBody();
			Profile objProfile = mapper.readValue(bodyResponse, Profile.class);
			logger.info("***callGetUserAPI : {}", objProfile);
			return objProfile;
		} catch (Exception e) {
			logger.error("***callGetUserAPI Exception: {}", e);
		}
		return null;
	}

	@Override
	public void handleMessage(BigDecimal senderID, Message objMessage) {
		try {
			if (objMessage == null)
				return;
			Request objRequest = null;
			if (objMessage.getText() != null) {
				logger.info("***Message object: {}", objMessage);
				logger.info("***User object: {}", userService.getBySenderID(senderID));
				String messageText = objMessage.getText().toLowerCase();
				if (userAction.isCommand(messageText)) {
					patternCommand(senderID, messageText);
				} else if (userService.isChatWithBot(senderID)) {
					// Call simsimi here
					Simsimi objSim = callSimsimi(messageText);
					if (objSim != null) {
						objRequest = messageTemplate.sendText(senderID, objSim.getSuccess());
					}
				} else {
					objRequest = messageTemplate.sendText(senderID, MessageConstants.MESSAGE_ERROR);
				}
			} else if (objMessage.getAttachments() != null || objMessage.getAttachments().isEmpty()) {
				logger.info("***Payload object: {}", objMessage.getAttachments());
				String attachmentUrl = objMessage.getAttachments().get(0).getPayload().getUrl();
				Attachment objAttachment = messageTemplate.testPayload(attachmentUrl);
				objRequest = messageTemplate.sendAttachment(senderID, objAttachment);
				logger.info("***objRequest object: {}", objRequest);
			} else {
				return;
			}
			if (objRequest == null)
				return;
			logger.info("***objRequest object: {}", objRequest);
			callSendAPI(objRequest);
		} catch (Exception e) {
			logger.error("***handleMessage - Exception: {}", e);
		}
	}

	@Override
	public void handlePostback(BigDecimal senderID, String payload) {
		logger.info("***handlePostback***");
		switch (payload) {
		case "yes":
			payloadGetStarted(senderID);
			break;
		case PayloadConstants.GET_STARTED:
			payloadGetStarted(senderID);
			break;
		default:
			break;
		}
	}

	@Override
	public void typingAction(BigDecimal senderID, String action) {
		Action objAction = messageTemplate.typingAction(senderID, action);
		callSendAPI(objAction);
	}

	public Simsimi callSimsimi(String messageText) {
		logger.info("***Call Simsimi***");
		try {
			UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(ChatBotAPIUrl.SIMSIMI)
					.queryParam("text", messageText).queryParam("lang", "vi_VN");
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
			headers.add("user-agent",
					"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
			HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
			String url = builder.build().encode().toUriString();
			ResponseEntity<String> res = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
			logger.error("***callSimsimi response: {}", res);
		} catch (Exception e) {
			logger.error("***callSimsimi Exception: {}", e);
		}
		return null;
	}

	public void payloadGetStarted(BigDecimal senderID) {
		Profile userProfile = callGetUserAPI(senderID);
		String messageText = MessageConstants.PAYLOAD_GET_STARTED.replace("username", userProfile.getFirstName());
		Request objRequest = messageTemplate.sendText(senderID, messageText);
		callSendAPI(objRequest);
	}

	public void patternCommand(BigDecimal senderID, String messageText) {
		String[] textArray = messageText.split("\\s+");
		logger.info("***textArray : {}", textArray.length);
		Request objRequest = null;
		try {
			if (textArray.length == 1) {
				objRequest = messageTemplate.sendText(senderID, MessageConstants.MESSAGE_ERROR);
			} else if (textArray.length > 1) {

				if (textArray[0].equals(CommandConstants.CHAT_WITH_BOT)) {
					if (textArray[1].equals(CommandConstants.ON)) {
						logger.info("***textArray.length > 1 : {}", textArray[1]);
						userService.updateIsChatWithBot(senderID, true);
						objRequest = messageTemplate.sendText(senderID, MessageConstants.TURN_ON_CHAT_BOT);
					}
					if (textArray[1].equals(CommandConstants.OFF)) {
						userService.updateIsChatWithBot(senderID, false);
						objRequest = messageTemplate.sendText(senderID, MessageConstants.TURN_OFF_CHAT_BOT);
					}
				}
			} else {
				objRequest = messageTemplate.sendText(senderID, MessageConstants.MESSAGE_ERROR);
			}
			callSendAPI(objRequest);
		} catch (Exception e) {
			logger.error("***patternCommand Exception: {}", e);
		}

	}

}
