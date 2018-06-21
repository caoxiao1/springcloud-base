package com.dcits.clbh.cloud.config.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.config.server.EnableConfigServer;

@SpringBootApplication  
@EnableConfigServer  
@EnableDiscoveryClient 		// 整合eureka，实现config server高可用，将config-server作为服务注册  
public class BHConfigServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(BHConfigServerApplication.class, args);  
	}
}
