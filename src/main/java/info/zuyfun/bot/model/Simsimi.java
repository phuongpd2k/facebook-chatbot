package info.zuyfun.bot.model;

import java.io.Serializable;

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
	@JsonProperty("success")
	private String success;
	@JsonProperty("donate")
	private String donate;
}
