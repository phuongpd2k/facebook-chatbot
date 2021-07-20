package info.zuyfun.bot.facebook.model;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class Simsimi implements Serializable {
	/**
	* 
	*/
	private static final long serialVersionUID = 1L;
	@JsonProperty("method")
	private String method;
	@JsonProperty("messages")
	private List<MessageSimsimi> messages;
	@JsonProperty("donate")
	private String donate;
}
