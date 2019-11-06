package com.bee;

import com.bee.command.ThreadCommand;
import com.netflix.config.ConfigurationManager;

/**
 * 使用线程隔离策略
 * 默认使用的就是线程隔离策略，利用线程池执行
 * 
 * 线程池满了之后，执行回退
 */
public class ThreadApplication {
	
	public static void main(String[] args) throws InterruptedException {
		ConfigurationManager.getConfigInstance()
			.setProperty("hystrix.threadpool.default.coreSize", 4);
		
		for (int i = 0; i < 6; i++) {
			ThreadCommand command = new ThreadCommand(i);
			command.queue();
		}
		
		Thread.sleep(5000);
	}
}
