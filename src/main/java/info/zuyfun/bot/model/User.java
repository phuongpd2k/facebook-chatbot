package info.zuyfun.bot.model;

import java.io.Serializable;
import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/**
 * @author zuyfun
 * @version 7/11/2021
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class User implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private BigDecimal id;
	@JsonProperty("first_name")
	private String firstName;
	@JsonProperty("last_name")
	private String lastName;
	@JsonProperty("profile_pic")
	private String profilePic;
	@JsonProperty("phone_number")
	private String phoneNumber;
	private String locale;
	private Double timezone;
	private String gender;
	@JsonProperty("contact_name")
	private String contactName;
	@JsonProperty("contact_email")
	private String contactEmail;
	@JsonProperty("contact_phone")
	private String contactPhone;

}