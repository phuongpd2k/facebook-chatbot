package info.zuyfun.bot.facebook.service.impl;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import info.zuyfun.bot.constants.FacebookAPIUrl;
import info.zuyfun.bot.facebook.model.Attachment;
import info.zuyfun.bot.facebook.model.Message;
import info.zuyfun.bot.facebook.model.Request;
import info.zuyfun.bot.facebook.model.RequestMessage;
import info.zuyfun.bot.facebook.model.RequestRecipient;
import info.zuyfun.bot.facebook.model.Simsimi;
import info.zuyfun.bot.facebook.service.MessageService;
import info.zuyfun.bot.facebook.template.MessageTemplate;

@Service
public class MessageServiceImpl implements MessageService {
	private static final Logger logger = LoggerFactory.getLogger(MessageServiceImpl.class);
	@Value("${fb_access_token}")
	private String FB_ACCESS_TOKEN;

	@Autowired
	protected RestTemplate restTemplate;
	@Autowired
	MessageTemplate messageTemplate;

	@Override
	@Async("asyncService")
	public void handleMessage(BigDecimal senderID, Message objMessage) {
		try {
			testThreadPool();
			if (objMessage == null)
				return;
			Request objRequest = new Request();
			RequestRecipient objRequestRecipient = new RequestRecipient();
			objRequestRecipient.setId(senderID);
			objRequest.setRequestRecipient(objRequestRecipient);
			RequestMessage objRequestMessage = new RequestMessage();
			objRequest.setRequestMessage(objRequestMessage);

			if (objMessage.getText() != null) {
				logger.info("***Attachment object: {}", objMessage);
				String messageText = objMessage.getText();
				// Object Attachment
				if (messageText.contains("/ssm ")) {
					Simsimi simsimi = callSimsimi(messageText.replace("/ssm ", ""));
					if (simsimi == null)
						return;
					objRequestMessage.setText(simsimi.getSuccess());
				} else {
					objRequestMessage.setText(messageText);
				}
			} else if (objMessage.getAttachments() != null || objMessage.getAttachments().isEmpty()) {
				logger.info("***Payload object: {}", objMessage.getAttachments());
				String attachmentUrl = objMessage.getAttachments().get(0).getPayload().getUrl();
				Attachment objAttachment = messageTemplate.testPayload(attachmentUrl);
				objRequestMessage.setAttachment(objAttachment);
			} else {
				return;
			}
			callSendAPI(objRequest);
		} catch (Exception e) {
			logger.error("***handleMessage - Exception: {}", e);
		}
	}

	public void testThreadPool() {
		for (int i = 0; i < 5; i++) {
			try {
				Thread.sleep(1000);
				logger.info("i=" + i);
			} catch (Exception e) {
				logger.info("***loopException : {}", e);
			}
		}
	}

	@Override
	@Async("asyncService")
	public void handlePostback(BigDecimal senderID, String payload) {
		logger.info("***handlePostback***");
		// Get the payload for the postback

		// Set the response based on the postback payload
		Request objRequest = new Request();
		RequestRecipient objRequestRecipient = new RequestRecipient();
		objRequestRecipient.setId(senderID);
		objRequest.setRequestRecipient(objRequestRecipient);
		RequestMessage objRequestMessage = new RequestMessage();
		objRequest.setRequestMessage(objRequestMessage);
		switch (payload) {
		case "yes":
			objRequestMessage.setText("Cảm ơn!");
			break;
		case "no":
			objRequestMessage.setText("Oops, Hãy thử lại tấm ảnh khác!");
			break;
		case "GET_STARTED":
			objRequestMessage.setText("Ồ lần đầu à :o");
			break;
		default:
			objRequestMessage.setText("Oops!!!");
			break;
		}
		callSendAPI(objRequest);

	}

	public void callSendAPI(Object objRequest) {
		// Construct the message body
		// Send the HTTP request to the Messenger Platform
		logger.info("***API Send Attachment***");
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

	public Simsimi callSimsimi(String messageText) {
		logger.info("***Call Simsimi***");

		Simsimi result = null;
//		webClient = WebClient.create(SIMSIMI_URL);
//		try {
//			Flux<Simsimi> flux = webClient.get().uri("?text=" + messageText + "&lang=vi_VN").retrieve()
//					.bodyToFlux(Simsimi.class);
//			result = flux.blockFirst() == null ? null : flux.blockFirst();
//			logger.info("***Simsimi Response {}", result);
//
//		} catch (Exception ex) {
//			logger.error("***callSimsimi Exception: {}", ex);
//		}
		return result;

	}

}
