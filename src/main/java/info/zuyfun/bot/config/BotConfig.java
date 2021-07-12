package info.zuyfun.bot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

import info.zuyfun.bot.schedule.HerokuIdle;

@EnableScheduling
@Configuration
public class BotConfig {

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

	@Bean
	public HerokuIdle herokuNotIdle() {
		return new HerokuIdle();
	}
}