package com.dcits.clbh.cloud.zuul.server.util.redis;

public class RedisConfigEnv {
	private static String redis_ip;					// Redis服务器IP
	private static String redis_port;				// Redis服务器端口号
	private static String redis_password;			// 访问密码
	private static Integer redis_max_connect;		// 可用连接实例的最大数目，默认值为8；
	private static Integer redis_max_idle;			// 最大空闲连接数
	private static Long redis_max_wait;				// 最大等待时间
	private static Integer redis_timeout;			// 超时
	private static Integer redis_soTimeout;			// TODO
	private static Boolean redis_test;				// 在borrow一个jedis实例时，是否提前进行validate操作
	
	private static final String PROPERTY_FILE_NAME = "redis-cluster"; 
	
	static{
		if(redis_ip == null || redis_ip.equals("")){
			redis_ip = PropertyUtil.getPropertyManager(PROPERTY_FILE_NAME).getProperty("redis.ip");
		}
		if(redis_port == null || redis_port.equals("")){
			redis_port = PropertyUtil.getPropertyManager(PROPERTY_FILE_NAME).getProperty("redis.port");
		}
		if(redis_password == null || redis_password.equals("")){
			redis_password = PropertyUtil.getPropertyManager(PROPERTY_FILE_NAME).getProperty("redis.password");
		}
		if(redis_max_connect == null || redis_max_connect.equals("")){
			redis_max_connect = 1024;
			String redisMaxConnect = PropertyUtil.getPropertyManager(PROPERTY_FILE_NAME).getProperty("redis.max.connect");
			try{
				if(redisMaxConnect != null && !redisMaxConnect.equals("")){
					redis_max_connect = Integer.parseInt(redisMaxConnect);
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		if(redis_max_idle == null || redis_max_idle.equals("")){
			redis_max_idle = 200;
			String redisMacIdle = PropertyUtil.getPropertyManager(PROPERTY_FILE_NAME).getProperty("redis.max.idle");
			try{
				if(redisMacIdle != null && !redisMacIdle.equals("")){
					redis_max_idle = Integer.parseInt(redisMacIdle);
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		if(redis_max_wait == null || redis_max_wait.equals("")){
			redis_max_wait = 1000L;
			String redisMaxWait = PropertyUtil.getPropertyManager(PROPERTY_FILE_NAME).getProperty("redis.max.wait");
			try{
				if(redisMaxWait != null && !redisMaxWait.equals("")){
					redis_max_wait = Long.valueOf(redisMaxWait);
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		if(redis_timeout == null || redis_timeout.equals("")){
			redis_timeout = 10000;
			String redisTimeout = PropertyUtil.getPropertyManager(PROPERTY_FILE_NAME).getProperty("redis.timeout");
			try{
				if(redisTimeout != null && !redisTimeout.equals("")){
					redis_timeout = Integer.valueOf(redisTimeout);
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		if(redis_soTimeout == null || redis_soTimeout.equals("")){
			redis_soTimeout = 10000;
			String redisSoTimeout = PropertyUtil.getPropertyManager(PROPERTY_FILE_NAME).getProperty("redis.so.timeout");
			try{
				if(redisSoTimeout != null && !redisSoTimeout.equals("")){
					redis_soTimeout = Integer.valueOf(redisSoTimeout);
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		if(redis_test == null){
			redis_test = true;
			String redisIsTest = PropertyUtil.getPropertyManager(PROPERTY_FILE_NAME).getProperty("redis.test");
			if("true".equals(redisIsTest)){
				redis_test = true;
			}else{
				redis_test = false;
			}
		}
		
	}
	
	public static String getRedisIp(){
		return redis_ip;
	}
	public static String getRedisPort(){
		return redis_port;
	}
	public static String getRedisPassword(){
		return redis_password;
	}
	public static Integer getRedisMaxConnect(){
		return redis_max_connect;
	}
	public static Integer getRedisMacIdle(){
		return redis_max_idle;
	}
	public static Long getRedis_max_wait() {
		return redis_max_wait;
	}
	public static Integer getRedisTimeout(){
		return redis_timeout;
	}
	public static Integer getRedisSoTimeout(){
		return redis_soTimeout;
	}
	public static Boolean getRedisIsTest(){
		return redis_test;
	}

}
