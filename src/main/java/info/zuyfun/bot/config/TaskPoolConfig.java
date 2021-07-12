package info.zuyfun.bot.config;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@EnableAsync
@Configuration
public class TaskPoolConfig {
	@Bean("webhookEndpoint")
	public Executor taskExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		// core thread pool size
		executor.setCorePoolSize(5);
		// Maximum number of threads
		executor.setMaxPoolSize(15);
		// Queue capacity
		executor.setQueueCapacity(200);
		// Active time
		executor.setKeepAliveSeconds(60);
		// Thread name prefix
		executor.setThreadNamePrefix("webhookEndpoint-");
		// rejection-policy: How to handle new tasks when pool has reached max size
		// CALLER_RUNS: Do not execute tasks in new threads, but in threads where the
		// caller resides.
		executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
		executor.initialize();
		return executor;
	}
}
