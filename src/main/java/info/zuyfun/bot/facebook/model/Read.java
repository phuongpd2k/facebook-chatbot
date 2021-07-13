package info.zuyfun.bot.facebook.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

/**
 * @author zuyfun
 * @version 7/11/2021
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class Read implements Serializable{
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long watermark;
    private Integer seq;

}
