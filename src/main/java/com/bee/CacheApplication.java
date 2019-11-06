package com.bee;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixRequestCache;
import com.netflix.hystrix.strategy.concurrency.HystrixConcurrencyStrategyDefault;
import com.netflix.hystrix.strategy.concurrency.HystrixRequestContext;

/**
 * Hystrix缓存：
 * 	缓存必须在一次请求中才能生效
 * @author zhao.jiahong
 *
 */
public class CacheApplication {
	public static void main(String[] args) {
		// 声明这是一次请求
		HystrixRequestContext context = HystrixRequestContext.initializeContext();
		String key = "cache-key";
		
		CacheCommand command1 = new CacheCommand(key);
		CacheCommand command2 = new CacheCommand(key);
		CacheCommand command3 = new CacheCommand(key);
		command1.execute();
		command2.execute();
		command3.execute();
		System.out.println("command1是否读取缓存：" + command1.isResponseFromCache());
		System.out.println("command2是否读取缓存：" + command2.isResponseFromCache());
		System.out.println("command3是否读取缓存：" + command3.isResponseFromCache());
		
		HystrixRequestCache cache = HystrixRequestCache.getInstance(HystrixCommandKey.Factory.asKey("CommonKey"), 
				HystrixConcurrencyStrategyDefault.getInstance());
		
		cache.clear(key);
		CacheCommand command4 = new CacheCommand(key);
		command4.execute();
		System.out.println("command4是否读取缓存：" + command4.isResponseFromCache());
		
		context.shutdown();
	}
	
	static class CacheCommand extends HystrixCommand<String> {

		private String cacheKey;
		
		public CacheCommand(String cacheKey) {
			super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("TestGroup"))
					.andCommandKey(HystrixCommandKey.Factory.asKey("CommonKey")));
			
			this.cacheKey = cacheKey;
		}
		
		@Override
		protected String run() throws Exception {
			System.out.println("执行方法");
			return "";
		}

		@Override
		protected String getFallback() {
			System.out.println("执行回退");
			return "";
		}

		@Override
		protected String getCacheKey() {
			return this.cacheKey;
		}
	}
}
