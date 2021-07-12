package info.zuyfun.bot.schedule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.client.RestTemplate;

public class HerokuIdle {
	private static final Logger logger = LoggerFactory.getLogger(HerokuIdle.class);

	@Autowired
	RestTemplate restTemplate;

	@Scheduled(cron = "0 0/10 * * * ?")
	public void herokuIdle() {
		try {
			String result = restTemplate.getForObject("https://info-zuy-bot.herokuapp.com/herokuIdle", String.class);
			logger.info("***herokuIdle Execution: {}", result);
		} catch (Exception e) {
			logger.error("***herokuIdle Exception: {}", e);
		}
	}
}
