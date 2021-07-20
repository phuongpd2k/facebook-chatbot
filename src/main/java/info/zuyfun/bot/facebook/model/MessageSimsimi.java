package info.zuyfun.bot.facebook.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class MessageSimsimi {
	@JsonProperty("ask")
	private String ask;
	@JsonProperty("text")
	private String text;
	@JsonProperty("response")
	private String response;
	@JsonProperty("result")
	private String result;
}