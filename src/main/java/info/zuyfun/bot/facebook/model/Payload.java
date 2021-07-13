package info.zuyfun.bot.facebook.model;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/**
 * @author zuyfun
 * @version 7/11/2021
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class Payload implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String url;
	@JsonProperty("coordinates.lat")
	private String coordinatesLat;
	@JsonProperty("coordinates.long")
	private String coordinatesLong;
	@JsonProperty("is_reusable")
	private Boolean isReusable;
	@JsonProperty("attachment_id")
	private String attachmentId;
	@JsonProperty("template_type")
	private String templateType;
	@JsonProperty("intro_message")
	private String introMessage;
	private String locale;
	@JsonProperty("top_element_style")
	private String topElementStyle;
	private String text;
	@JsonProperty("recipient_name")
	private String recipientName;
	@JsonProperty("payment_method")
	private String paymentMethod;
	@JsonProperty("order_url")
	private String orderUrl;
	private List<Button> buttons;
	private List<Element> elements;
	private Long timestamp;

}
