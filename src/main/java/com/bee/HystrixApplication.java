package com.bee;

import com.netflix.config.ConfigurationManager;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandProperties;

/**
 * 
 * 短路器打开条件：
 * 	1. 10秒内请求10次（默认为20次）
 * 	2. 错误率超过50%
 *
 * 断路器在打开后，短路器会有5秒的休眠期，5秒过后，断路器会尝试执行一次命令，成功，则关闭断路器
 * 如果失败，断路器仍然打开。
 * 
 * 测试断路器打开条件
 */
public class HystrixApplication {
	public static void main(String[] args) {
		// 10秒内10次请求,错误率超过50%，则开启断路器
		ConfigurationManager.getConfigInstance()
			.setProperty("hystrix.command.default.circuitBreaker.requestVolumeThreshold", 10);
		
		for (int i = 0; i < 15; i++) {
			ErrorCommand command = new ErrorCommand();
			command.execute();
			// 判断断路器是否打开
			System.out.println(command.isCircuitBreakerOpen());
		}
			
	}
	
	static class ErrorCommand extends HystrixCommand<String> {
		
		public ErrorCommand() {
			super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("TestGroup"))
				.andCommandPropertiesDefaults(HystrixCommandProperties.Setter()
						.withExecutionTimeoutInMilliseconds(500)));
		}

		@Override
		protected String run() throws Exception {
			Thread.sleep(800);
			return "";
		}

		@Override
		protected String getFallback() {
			return "fallback";
		}
		
		
	}
}
