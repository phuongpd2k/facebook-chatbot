package info.zuyfun.bot.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class Messaging {
	@JsonProperty("messaging")
	private List<Message> messaging;
}
