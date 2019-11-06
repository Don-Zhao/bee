package com.bee;

import com.bee.command.ThreadCommand;
import com.netflix.config.ConfigurationManager;
import com.netflix.hystrix.HystrixCommandProperties.ExecutionIsolationStrategy;

/**
 * SEMAPHORE隔离措施（全部同步执行）
 */
public class SemaphoreApplication {
	public static void main(String[] args) throws InterruptedException {
		ConfigurationManager.getConfigInstance()
			.setProperty("hystrix.command.default.execution.isolation.strategy", 
					ExecutionIsolationStrategy.SEMAPHORE);
		
		// 最大并发数为2
		ConfigurationManager.getConfigInstance()
		.setProperty("hystrix.command.default.execution.isolation.semaphore.maxConcurrentRequests", 
				2);
		
		for (int i = 0; i < 6; i++) {
			final int index = i;
			Thread thread = new Thread() {
				@Override
				public void run() {
					ThreadCommand command = new ThreadCommand(index);
					command.execute();
				}
			};
			
			thread.start();
		}
		
		Thread.sleep(5000);
	}
}
