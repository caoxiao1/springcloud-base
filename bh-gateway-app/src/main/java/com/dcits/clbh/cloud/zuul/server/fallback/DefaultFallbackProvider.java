package com.dcits.clbh.cloud.zuul.server.fallback;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.cloud.netflix.zuul.filters.route.ZuulFallbackProvider;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;

import com.alibaba.fastjson.JSONObject;

/**
 * 此实例是用于 hystrix 开启时，还需所有的异常都会开启！
 * 向所有的微服务路由请求失败时的降级处理
 * */
public class DefaultFallbackProvider implements ZuulFallbackProvider {
	
	public DefaultFallbackProvider() {
		System.out.println("init");
	}
	
	@Override
    public String getRoute() {
		// 微服务id，如果需要所有调用都支持回退，则 return "*" 或 return null  
        return "*";
    }

	/** 
     * 如果路由目标微服务失败，返回目标微服务不可用信息给消费者客户端 
     * 如下情况会发生降级：连接超时、连接失败、read超时等等
     */  
    @Override
    public ClientHttpResponse fallbackResponse() {
    	
    	// 取得 hystrix 配置信息
    	
        return new ClientHttpResponse() {
            @Override
            public HttpStatus getStatusCode() throws IOException {
                return HttpStatus.OK;
            }

            /** 
             * 网关向目标服务请求失败，但是消费者客户端向网关发起的请求返回结果应该是OK的
             * 不应该把目标微服务的 404，500 等问题抛给客户端 
             * 网关和目标微服务集群对于客户端来说是黑盒子 
             */ 
            @Override
            public int getRawStatusCode() throws IOException {
                return 200;
            }

            @Override
            public String getStatusText() throws IOException {
                return "OK";
            }

            @Override
            public void close() {

            }

            @Override
            public InputStream getBody() throws IOException {
//                return new ByteArrayInputStream("fallback".getBytes());
            	JSONObject r = new JSONObject();  
                r.put("state", "999");  
                r.put("msg", "目标微服务不可用（这是 zuul 请求目标微服务失败时，执行的降级处理）。");  
                r.put("data", null);
                return new ByteArrayInputStream(r.toJSONString().getBytes("UTF-8"));  
            }

            @Override
            public HttpHeaders getHeaders() {
                HttpHeaders headers = new HttpHeaders();
                // 和body中的内容编码一致，否则容易乱码  
//                headers.setContentType(MediaType.APPLICATION_JSON);
                headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
                return headers;
            }
        };
    }
	
}
