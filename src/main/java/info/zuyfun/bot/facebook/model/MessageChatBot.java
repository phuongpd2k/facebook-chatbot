package info.zuyfun.bot.facebook.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class MessageChatBot implements Serializable {
	/**
	* 
	*/
	private static final long serialVersionUID = 1L;
	@JsonProperty("result")
	private String result;
	@JsonProperty("response")
	private String response;
	@JsonProperty("msg")
	private String msg;
}
