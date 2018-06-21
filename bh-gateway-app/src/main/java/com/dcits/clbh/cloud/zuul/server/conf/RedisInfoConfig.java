package com.dcits.clbh.cloud.zuul.server.conf;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * 微服务实例基本信息
 * */
//@Component
//@ConfigurationProperties(prefix="redis")
//@PropertySource("classpath:/config/rediscluster.properties")
public class RedisInfoConfig {
	
	/**
	 * 微服务名称
	 * */
	private String password;
	
	/**
	 * 微服务实例序号，由1开始，逐步累加
	 * */
	private String passwordUse;
	
	/**
	 * 微服务描述
	 * */
	private String maxConnect;
	private String maxIdle;
	private String maxWait;
	private String timeout;
	private String test;
	private String soTimeout;
	
	private String ip;
	private String port;
	
	
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getPasswordUse() {
		return passwordUse;
	}
	public void setPasswordUse(String passwordUse) {
		this.passwordUse = passwordUse;
	}
	public String getMaxConnect() {
		return maxConnect;
	}
	public void setMaxConnect(String maxConnect) {
		this.maxConnect = maxConnect;
	}
	public String getMaxIdle() {
		return maxIdle;
	}
	public void setMaxIdle(String maxIdle) {
		this.maxIdle = maxIdle;
	}
	public String getMaxWait() {
		return maxWait;
	}
	public void setMaxWait(String maxWait) {
		this.maxWait = maxWait;
	}
	public String getTimeout() {
		return timeout;
	}
	public void setTimeout(String timeout) {
		this.timeout = timeout;
	}
	public String getTest() {
		return test;
	}
	public void setTest(String test) {
		this.test = test;
	}
	public String getSoTimeout() {
		return soTimeout;
	}
	public void setSoTimeout(String soTimeout) {
		this.soTimeout = soTimeout;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getPort() {
		return port;
	}
	public void setPort(String port) {
		this.port = port;
	}
	
	
}
