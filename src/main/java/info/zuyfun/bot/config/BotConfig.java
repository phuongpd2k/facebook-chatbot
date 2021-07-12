package info.zuyfun.bot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import info.zuyfun.bot.schedule.HerokuIdle;

@EnableScheduling
@Configuration
public class BotConfig {

	@Bean
	public HerokuIdle herokuIdle() {
		return new HerokuIdle();
	}
}
