package info.zuyfun.bot.schedule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.client.RestTemplate;

public class HerokuNotIdle {
	private static final Logger logger = LoggerFactory.getLogger(HerokuNotIdle.class);
	@Autowired
	RestTemplate restTemplate;

	@Scheduled(cron = "0 0/15 * * * ? *")
	public void herokuNotIdle() {
		logger.info("***Heroku not idle execution");
		restTemplate.getForObject("https://info-zuy-bot.herokuapp.com/", Object.class);
	}
}
