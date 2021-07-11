package info.zuyfun.bot.model;

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
	@JsonProperty("messaging_type")
	private String messagingType;
	private Postback postback;
	private Read read;
	private Delivery delivery;
	@JsonProperty("checkout_update")
	private String senderAction;
	@JsonProperty("setting_type")
	private String settingType;
	@JsonProperty("account_linking_url")
	private String accountLinkingUrl;
	@JsonProperty("whitelisted_domains")
	private List<String> whitelistedDomains;
	@JsonProperty("domain_action_type")
	private String domainActionType;
	@JsonProperty("thread_state")
	private String threadState;
	@JsonProperty("payment_privacy_url")
	private String paymentPrivacyUrl;
	@JsonProperty("hub.mode")
	private String mode;
	@JsonProperty("hub.verify_token")
	private String token;
	@JsonProperty("hub.challenge")
	private String challenge;
	@JsonProperty("get_started")
	private Postback getStarted;
	private List<Payload> greeting;
}
