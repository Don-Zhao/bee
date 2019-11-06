package com.bee;

import com.netflix.config.ConfigurationManager;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandMetrics.HealthCounts;
import com.netflix.hystrix.HystrixCommandProperties;

/**
 * 断路器在打开后，短路器会有5秒的休眠期，5秒过后，断路器会尝试执行一次命令，成功，则关闭断路器
 * 如果失败，断路器仍然打开。
 *
 */
public class CloseBrokerApplication {
	public static void main(String[] args) throws InterruptedException {
		ConfigurationManager.getConfigInstance()
			.setProperty("hystrix.command.default.circuitBreaker.requestVolumeThreshold", 3);
		
		boolean isTimeout = true;
		for (int i = 0; i < 10; i++) {
			TestCommand command = new TestCommand(isTimeout);
			command.execute();
			HealthCounts hCounts = command.getMetrics().getHealthCounts();
			System.out.println("请求总数：" + hCounts.getTotalRequests());
			System.out.println(command.isCircuitBreakerOpen());
			if (command.isCircuitBreakerOpen()) {
				isTimeout = false;
				System.out.println("**********等待休眠期结束*****");
				Thread.sleep(6000);
			}
		}
	}
	
	static class TestCommand extends HystrixCommand<String> {
		private boolean isTimeout;
		
		public TestCommand(boolean isTimeout) {
			super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("TestGroup"))
					.andCommandPropertiesDefaults(HystrixCommandProperties
							.Setter().withExecutionTimeoutInMilliseconds(500)));
			
			this.isTimeout = isTimeout;
		}

		@Override
		protected String run() throws Exception {
			if (isTimeout) {
				Thread.sleep(800);
			} else {
				Thread.sleep(200);
			}
			
			return "";
		}

		@Override
		protected String getFallback() {
			return "fallback";
		}
	}
}
