package info.zuyfun.bot.model;

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
public class Message implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@JsonProperty("is_echo")
	private Boolean isEcho;
	@JsonProperty("app_id")
	private String appId;
	private String metadata;
	private String mid;
	private Integer seq;
	private String text;
	private Attachment attachment;
	private List<Attachment> attachments;
	@JsonProperty("quick_reply")
	private Button quickReply;
	@JsonProperty("quick_replies")
	private List<Button> quickReplies;

}
