package info.zuyfun.bot.service.impl;

import javax.ws.rs.core.Response;

import org.apache.cxf.jaxrs.client.WebClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import info.zuyfun.bot.service.EventHandler;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class EventHandlerImpl implements EventHandler {
	@Value("${fb_access_token}")
	private String FB_ACCESS_TOKEN;
	@Value("${simsimi_url}")
	private String SIMSIMI_URL;

	private final String fbURL = "https://graph.facebook.com/v2.6/me/messages";

	private WebClient clientPool;

	@Override
	public void handleMessage(String sender_psid, JsonObject received_message) {
		try {
			String jsonStr = "";
			String messageText = received_message.getString("text", "");
			JsonArray attachments = received_message.getJsonArray("attachments", null);
			// Checks if the message contains text
			if (!messageText.isEmpty()) {
				// Create the payload for a basic text message, which
				// will be added to the body of our request to the Send API
				String messageResponse = "";
				JsonObject json = new JsonObject(callSimsimi(messageText));
				if (json != null) {
					messageResponse = json.getString("success");
				}

				jsonStr = "{\r\n     \"text\": \"" + messageResponse + "\"    }";
			} else if (attachments != null) {
				// Get the URL of the message attachment
				String attachmentUrl = attachments.getJsonObject(0).getJsonObject("payload").getString("url");
				jsonStr = "{\r\n" + "      \"attachment\": {\r\n" + "        \"type\": \"template\",\r\n"
						+ "        \"payload\": {\r\n" + "          \"template_type\": \"generic\",\r\n"
						+ "          \"elements\": [{\r\n"
						+ "            \"title\": \"Is this the right picture?\",\r\n"
						+ "            \"subtitle\": \"Tap a button to answer.\",\r\n" + "            \"image_url\": "
						+ attachmentUrl + ",\r\n" + "            \"buttons\": [\r\n" + "              {\r\n"
						+ "                \"type\": \"postback\",\r\n" + "                \"title\": \"Yes!\",\r\n"
						+ "                \"payload\": \"yes\",\r\n" + "              },\r\n" + "              {\r\n"
						+ "                \"type\": \"postback\",\r\n" + "                \"title\": \"No!\",\r\n"
						+ "                \"payload\": \"no\",\r\n" + "              }\r\n" + "            ],\r\n"
						+ "          }]\r\n" + "        }\r\n" + "      }\r\n" + "    }\r\n" + "  } ";
				// Send the response message

			}
			callSendAPI(sender_psid, jsonStr);
		} catch (Exception e) {
			log.error("*** handleMessage System error: " + e);
		}
	}

	@Override
	public void handlePostback(String sender_psid, JsonObject received_postback) {
		String jsonStr = "";

		log.info("handlePostback received_postback: " + received_postback);

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
			log.info("Response message: " + clientResponse.readEntity(String.class));
		} catch (Exception e) {
			log.error("*** callSendAPI System Error: " + e);
		}
		log.info("FB_ACCESS_TOKEN: " + FB_ACCESS_TOKEN);
		log.info("request body: " + requestBody);
	}

	@Override
	public String callSimsimi(String messageText) {
		String result = "";
		try {
			clientPool = WebClient.create(SIMSIMI_URL);
			clientPool.query("text", messageText).query("lang", "vi_VN");
			Response response = clientPool.get();
			result = response.readEntity(String.class);
			log.info("*** Data response " + result);

		} catch (Exception ex) {
			log.error("*** Lỗi hệ thống - Exception: " + ex);
		}
		return result;
	}

}
