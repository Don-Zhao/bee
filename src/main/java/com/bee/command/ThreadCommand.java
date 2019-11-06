package com.bee.command;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;

public class ThreadCommand extends HystrixCommand<String> {

	private int index;
	
	public ThreadCommand(int index) {
		super(HystrixCommandGroupKey.Factory.asKey("TestGroup"));
		this.index = index;
	}
	
	@Override
	protected String run() throws Exception {
		Thread.sleep(500);
		System.out.println("执行方法， 当前索引为：" + this.index);
		return "";
	}

	@Override
	protected String getFallback() {
		System.out.println("执行回退， 当前索引为：" + this.index);
		return "";
	}

}
