package info.zuyfun.bot.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "user")
@Data
public class User implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "RECIPIENT_ID")
	private BigDecimal recipeintID;
	@Column(name = "IS_CHAT_WITH_BOT")
	private boolean isChatWithBot;
	@Column(name = "IS_NOTIFICATION_KQXS")
	private boolean isNotificationKQXS;
	@Column(name = "LAST_TIME_CHAT", length = 13)
	private String lastTimeChat;
}
