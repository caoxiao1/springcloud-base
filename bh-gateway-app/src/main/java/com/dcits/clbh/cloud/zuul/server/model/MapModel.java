package com.dcits.clbh.cloud.zuul.server.model;

/**
 * 返回结果
 * */
public class MapModel {

	private String cacheKey;
	private String cacheValue;
	
	public MapModel(String cacheKey, String cacheValue) {
		this.cacheKey = cacheKey;
		this.cacheValue = cacheValue;
	}
	
	public String getCacheKey() {
		return cacheKey;
	}
	public void setCacheKey(String cacheKey) {
		this.cacheKey = cacheKey;
	}
	public String getCacheValue() {
		return cacheValue;
	}
	public void setCacheValue(String cacheValue) {
		this.cacheValue = cacheValue;
	}
	
}
