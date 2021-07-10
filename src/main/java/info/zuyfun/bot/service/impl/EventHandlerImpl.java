package info.zuyfun.bot.service.impl;

import javax.ws.rs.core.Response;

import org.apache.cxf.jaxrs.client.WebClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import info.zuyfun.bot.service.EventHandler;
import io.vertx.core.json.JsonObject;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class EventHandlerImpl implements EventHandler {
	@Value("${fb_access_token}")
	private String FB_ACCESS_TOKEN;
	@Value("${simsimi_url}")
	private String SIMSIMI_URL;

	private WebClient clientPool;

	@Override
	public void handleMessage(String sender_psid, JsonObject received_message) {
		// TODO Auto-generated method stub

	}

	@Override
	public void handlePostback(String sender_psid, JsonObject received_postback) {
		// TODO Auto-generated method stub

	}

	@Override
	public void callSendAPI(String sender_psid, String response) {
		// TODO Auto-generated method stub

	}

	@Override
	public String callSimsimi(String messageText) {
		String result = "";
		try {
			clientPool = WebClient.create(SIMSIMI_URL);
			clientPool.query("text", messageText).query("lang", "vi_VN");
			// Header

			Response response = clientPool.get();
			log.info("=====Got API Simsimi response with status: " + response.getStatus());
			switch (response.getStatus()) {
			case 200:
				result = response.readEntity(String.class);
				log.info("*** Data response " + result);
				break;
			default:
				log.error("*** Lỗi API");
				// result = "API Error";
				result = response.readEntity(String.class);
				break;
			}
		} catch (Exception ex) {
			log.error("*** Lỗi hệ thống - Exception: " + ex);
		}
		return result;
	}

}
