package com.bee;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandProperties;

/**
 * 执行命令未超时，不打开断路器，不执行回退
 *
 */
public class TimeoutCommandApplication {
	public static void main(String[] args) {
		TimeoutCommand command = new TimeoutCommand();
		String result = command.execute();
		System.out.println(result);
	}
	
	static class TimeoutCommand extends HystrixCommand<String> {

		public TimeoutCommand() {
			super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("Test"))
					.andCommandPropertiesDefaults(HystrixCommandProperties.Setter().withExecutionTimeoutInMilliseconds(2000)));
			;
			
		}
		@Override
		protected String run() throws Exception {
			System.out.println("执行命令");
			Thread.sleep(1000);
			return "success";
		}
		
		@Override
		protected String getFallback() {
			System.out.println("回退");
			return "roll back";
		}

	}
}
