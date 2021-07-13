package info.zuyfun.bot.facebook.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Model for the webhook events.
 *
 * @author zuyfun
 * @version 7/11/2021
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class Event implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private User sender;
	private User recipient;
	private Long timestamp;
	private Message message;
	private Postback postback;
	private Read read;
	private Delivery delivery;
	@JsonProperty("setting_type")
	private String settingType;
	@JsonProperty("account_linking_url")
	private String accountLinkingUrl;
	@JsonProperty("get_started")
	private Postback getStarted;
	private List<Payload> greeting;
}
