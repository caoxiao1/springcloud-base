package com.dcits.clbh.cloud.zuul.server;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;

@EnableZuulProxy
@SpringCloudApplication
@EnableHystrixDashboard
public class BHZuulAdminServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(BHZuulAdminServerApplication.class, args);
    }
    
    @Bean
    public AccessFilter accessFilter() {
        return new AccessFilter();
    }
 
//    @Bean
//    public DefaultFallbackProvider defaultFallbackProvider(){
//    	return new DefaultFallbackProvider();
//    }
    
}