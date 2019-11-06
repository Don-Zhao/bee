package com.bee;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;

import rx.Observable;
import rx.Observer;

/**
 * 
 * 执行命令有4个方法：observe， toObservable， queue和execute
 * 
 * execute是同步执行
 * observe和toObservable返回的是可观察对象，并不会立即执行。
 *
 */
public class OtherExecuteMethodApplication {

	public static void main(String[] args) throws InterruptedException {
		HystrixDemoCommand command = new HystrixDemoCommand("observe method");
		command.observe();
		
		HystrixDemoCommand command2 = new HystrixDemoCommand("toObservable method");
		Observable<String> result = command2.toObservable();
		result.subscribe(new Observer<String>() {

			@Override
			public void onCompleted() {
				System.out.println("completed");
			}

			@Override
			public void onError(Throwable e) {
				
			} 

			@Override
			public void onNext(String t) {
				System.out.println(t);
				
			}
		});
		
		Thread.sleep(1000);
	}
	
	static class HystrixDemoCommand extends HystrixCommand<String> {

		private String message;
		
		public HystrixDemoCommand(String message) {
			super(HystrixCommandGroupKey.Factory.asKey("Test"));
			this.message = message;
		}

		@Override
		protected String run() throws Exception {
			return this.message;
		}
	}
}
