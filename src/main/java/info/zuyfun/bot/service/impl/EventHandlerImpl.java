package info.zuyfun.bot.service.impl;

import javax.ws.rs.core.Response;

import org.apache.cxf.jaxrs.client.WebClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import info.zuyfun.bot.model.Event;
import info.zuyfun.bot.service.EventHandler;
import lombok.extern.slf4j.Slf4j;

@Service
public class EventHandlerImpl implements EventHandler {
	private static final Logger logger = LoggerFactory.getLogger(EventHandlerImpl.class);

	@Value("${fb_access_token}")
	private String FB_ACCESS_TOKEN;
	@Value("${simsimi_url}")
	private String SIMSIMI_URL;

	private final String fbURL = "https://graph.facebook.com/v2.6/me/messages?access_token=" + FB_ACCESS_TOKEN;

	private WebClient clientPool;

	@Autowired
	protected RestTemplate restTemplate;

	@Override
	public void handleMessage(Event event) {
		try {
			logger.info("FB_ACCESS_TOKEN: {}", FB_ACCESS_TOKEN);
			restTemplate.postForEntity(fbURL,
					new Event().setRecipient(event.getRecipient()).setSenderAction("typing_on"), Response.class);
		} catch (

		Exception e) {
			logger.error("*** handleMessage System error: {}", e);
		}
	}

	@Override
	public void handlePostback(Event event) {
		String jsonStr = "";

		logger.info("handlePostback received_postback: ");

	}

	@Override
	public void callSendAPI(String sender_psid, String response) {
		// Construct the message body
		String requestBody = "{\r\n" + "    \"recipient\": {\r\n" + "      \"id\": " + sender_psid + "\r\n"
				+ "    },\r\n" + "    \"message\": " + response + "\r\n" + "  }";

		// Send the HTTP request to the Messenger Platform
		try {
			clientPool = WebClient.create(fbURL);
			clientPool.query("access_token", FB_ACCESS_TOKEN);
			clientPool.header("Content-Type", "application/json");
			Response clientResponse = clientPool.post((Object) requestBody);
			logger.info("Response message: {}", clientResponse.readEntity(String.class));
		} catch (Exception e) {
			logger.error("*** callSendAPI System Error: " + e);
		}
		logger.info("FB_ACCESS_TOKEN: {}", FB_ACCESS_TOKEN);
		logger.info("request body: {}", requestBody);
	}

	@Override
	public String callSimsimi(String messageText) {
		String result = "";
		try {
			clientPool = WebClient.create(SIMSIMI_URL);
			clientPool.query("text", messageText).query("lang", "vi_VN");
			Response response = clientPool.get();
			result = response.readEntity(String.class);
			logger.info("*** Data response {}", result);

		} catch (Exception ex) {
			logger.error("*** Lỗi hệ thống - Exception: {}", ex);
		}
		return result;
	}

}
