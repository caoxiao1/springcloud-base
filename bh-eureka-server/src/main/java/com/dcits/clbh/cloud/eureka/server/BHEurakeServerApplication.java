package com.dcits.clbh.cloud.eureka.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class BHEurakeServerApplication {

	public static void main(String[] args) {
		
		SpringApplication.run(BHEurakeServerApplication.class, args);
	}
	
}
