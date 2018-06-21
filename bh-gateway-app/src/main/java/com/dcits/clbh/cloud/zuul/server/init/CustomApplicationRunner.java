package com.dcits.clbh.cloud.zuul.server.init;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.netflix.hystrix.HystrixCommandProperties;

@Component
@Order(value=1)		// 这个顺序，这有些什么实例呢？执行时机时什么时间呢？
public class CustomApplicationRunner implements ApplicationRunner {

	@Override
	public void run(ApplicationArguments args) throws Exception {
		System.out.println(">>>>>>>>>> CustomApplicationRunner - This will be execute when the project was started!");
		
		// 此处的 args 参数是否就是入口参数呢？
		System.out.println(args);
		
		// 输出当前 hystrix 的配置信息
		System.out.println(">>>>>>>>>> CustomApplicationRunner - 输出当前 hystrix 的配置信息");
		System.out.println(HystrixCommandProperties.Setter().getExecutionTimeoutEnabled() + ","
				+ HystrixCommandProperties.Setter().getExecutionTimeoutInMilliseconds() + "," 
				+ HystrixCommandProperties.Setter().getExecutionIsolationStrategy());
		
		// 以上输出全部为 null，在 debug 模式下发现 HystrixCommandProperties 的属性全部为空
	}

}
