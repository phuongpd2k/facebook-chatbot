package info.zuyfun.bot.model;

import java.io.Serializable;

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
public class Button implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String type;
	private String url;
	private String title;
	private String payload;
	@JsonProperty("webview_height_ratio")
	private String webviewHeightRatio;
	@JsonProperty("messenger_extensions")
	private Boolean messengerExtensions;
	@JsonProperty("fallback_url")
	private String fallbackUrl;
	@JsonProperty("content_type")
	private String contentType;
	@JsonProperty("image_url")
	private String imageUrl;


}
