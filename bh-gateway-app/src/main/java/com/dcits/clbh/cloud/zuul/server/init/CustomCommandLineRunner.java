package com.dcits.clbh.cloud.zuul.server.init;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(value=2)		// 这个顺序，这有些什么实例呢？执行时机时什么时间呢？
public class CustomCommandLineRunner implements CommandLineRunner{

	@Override
	public void run(String... args) throws Exception {
		System.out.println(">>>>>>>>>> CustomCommandLineRunner - This will be execute when the project was started!");
		
		// 此处的 args 参数是否就是入口参数呢？
		System.out.println(args);
		
	}

}
