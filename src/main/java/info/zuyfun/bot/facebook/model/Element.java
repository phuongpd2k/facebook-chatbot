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
public class Element implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String title;
	private String subtitle;
	private Integer quantity;
	private Double price;
	private String currency;
	@JsonProperty("image_url")
	private String imageUrl;
	@JsonProperty("item_url")
	private String itemUrl;
	@JsonProperty("default_action")
	private Button defaultAction;
	private List<Button> buttons;

}
