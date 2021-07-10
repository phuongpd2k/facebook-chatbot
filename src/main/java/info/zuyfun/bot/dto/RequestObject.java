package info.zuyfun.bot.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class RequestObject {
	@JsonProperty("object")
	private String object;
	@JsonProperty("entry")
	private List<Messaging> entry;
}
