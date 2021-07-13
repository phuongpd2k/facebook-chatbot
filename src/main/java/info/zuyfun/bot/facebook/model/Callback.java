package info.zuyfun.bot.facebook.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author zuyfun
 * @version 7/11/2021
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class Callback implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String object;
	private List<Entry> entry;

}
