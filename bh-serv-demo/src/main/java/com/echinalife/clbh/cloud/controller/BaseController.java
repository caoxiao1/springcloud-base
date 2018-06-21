package com.echinalife.clbh.cloud.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;

public abstract class BaseController {

	private final Logger logger = Logger.getLogger(getClass());
	
	@Autowired
    private DiscoveryClient client;
	
	/**
	 * 打印日志
	 * */
	protected void serviceApiLog(Object result) {
		 // 记录打印日志（AOP TODO）
        ServiceInstance instance = client.getLocalServiceInstance();
        logger.info("/getAll, host:" + instance.getHost() + ", service_id:" + instance.getServiceId() + ", result:" + result);
	}
	
}
