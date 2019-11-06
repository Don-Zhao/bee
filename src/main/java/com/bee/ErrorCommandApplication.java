package com.bee;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandProperties;

/**
 * 超时时间定为两秒，服务器端程序需要休眠10秒，所以程序超时，会回退
 *
 */
public class ErrorCommandApplication {
	public static void main(String[] args) {
		HystrixErrorCommand command = new HystrixErrorCommand();
		String result = command.execute();
		System.out.println(result);
	}
	
	static class HystrixErrorCommand extends HystrixCommand<String> {
		public HystrixErrorCommand() {
			super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("Test"))
					.andCommandPropertiesDefaults(HystrixCommandProperties.Setter().withExecutionTimeoutInMilliseconds(2000)));
			;
		}

		@Override
		protected String run() throws Exception {
			String url = "http://localhost:8080/err";
			HttpGet httpGet = new HttpGet(url);
			CloseableHttpClient httpClient = HttpClients.createDefault();
			HttpResponse response = httpClient.execute(httpGet);
			String result = EntityUtils.toString(response.getEntity());
			return result;
		}

		@Override
		protected String getFallback() {
			System.out.println("fail back");
			return "出错了";
		}
	}
}
