package info.zuyfun.bot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import info.zuyfun.bot.facebook.template.MessageTemplate;
import info.zuyfun.bot.facebook.template.Typing;
import info.zuyfun.bot.schedule.HerokuIdle;
import info.zuyfun.bot.utils.UserAction;

@EnableScheduling
@Configuration
public class BotConfig {

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

	@Bean
	public HerokuIdle herokuIdle() {
		return new HerokuIdle();
	}

	@Bean
	public MessageTemplate messageTemplate() {
		return new MessageTemplate();
	}

	@Bean
	public Typing typingTemplate() {
		return new Typing();
	}

	@Bean
	public UserAction userAction() {
		return new UserAction();
	}

	@Bean
	ObjectMapper mapper() {
		return new ObjectMapper();
	}
}
