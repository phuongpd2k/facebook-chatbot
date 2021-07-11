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
	@JsonProperty("type")
	private String type;
	@JsonProperty("code")
	private BigDecimal code;
	@JsonProperty("error_subcode")
	private BigDecimal error_subcode;
	@JsonProperty("fbtrace_id")
	private String fbtrace_id;
}
