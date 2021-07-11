package info.zuyfun.bot.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class Error implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@JsonProperty("message")
	private String message;
	@JsonProperty("message")
	private String type;
	@JsonProperty("message")
	private BigDecimal code;
	@JsonProperty("message")
	private BigDecimal error_subcode;
	@JsonProperty("message")
	private String fbtrace_id;
}
