package com.bee;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandProperties;

/**
 * 短路器打开，程序总会回退，并不会执行
 */
public class NormalCommandApplication {
	
	public static void main(String[] args) throws ClientProtocolException, IOException, ParseException {
		HystrixNormalCommand command = new HystrixNormalCommand();
		String result = command.execute();
		System.out.println(result);
	}
	
	static class HystrixNormalCommand extends HystrixCommand<String> {
		public HystrixNormalCommand() {
			super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("Test"))
					.andCommandPropertiesDefaults(HystrixCommandProperties.Setter()
							.withExecutionTimeoutInMilliseconds(10000)
							.withCircuitBreakerForceOpen(true))); //断路器打开，总会回退
			;
		}

		@Override
		protected String run() throws Exception {
			String url = "http://localhost:8080/normal";
			HttpGet httpGet = new HttpGet(url);
			CloseableHttpClient httpClient = HttpClients.createDefault();
			HttpResponse response = httpClient.execute(httpGet);
			String result = EntityUtils.toString(response.getEntity());
			return result;
		}

		@Override
		protected String getFallback() {
			return "fallback";
		}

		
	}
}
