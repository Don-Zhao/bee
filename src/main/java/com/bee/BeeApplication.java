package com.bee;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class BeeApplication {

	public static void main(String[] args) throws ClientProtocolException, IOException, InterruptedException {
		normalHello();
		errorHello();
	}
	
	public static void normalHello() throws ClientProtocolException, IOException {
		String url = "http://localhost:8080/normal";
		HttpGet httpGet = new HttpGet(url);
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpResponse response = httpClient.execute(httpGet);
		String result = EntityUtils.toString(response.getEntity());
		System.out.println(result);
	}
	
	public static void errorHello() throws ClientProtocolException, IOException {
		String url = "http://localhost:8080/err";
		HttpGet httpGet = new HttpGet(url);
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpResponse response = httpClient.execute(httpGet);
		String result = EntityUtils.toString(response.getEntity());
		System.out.println(result);
	}
}
