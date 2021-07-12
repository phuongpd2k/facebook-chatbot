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

	@Scheduled(cron = "0 0/10 * * * ?")
	public void herokuNotIdle() {
		try {
			String result = restTemplate.getForObject("https://info-zuy-bot.herokuapp.com/herokuNotIdle", String.class);
			logger.info("***herokuNotIdle Execution {}", result);
		} catch (Exception e) {
			logger.error("***herokuNotIdle Exception: {}", e);
		}
	}
}
