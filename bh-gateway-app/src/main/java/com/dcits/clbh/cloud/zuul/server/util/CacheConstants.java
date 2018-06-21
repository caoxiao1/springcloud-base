package com.dcits.clbh.cloud.zuul.server.util;

/**
 * 缓存 key
 * */
public class CacheConstants {
	
	/**
	 * redis 缓存短信key
	 * 
	 */
	public static final String AUTH_PHONE_MESSAGE = "bh.auth.phone.message.";
	
	/**
	 * redis 缓存用户主信息key
	 */
	public static final String AUTH_USER_MAIN = "bh.auth.user.main.";
	
	/**
	 * redis 缓存用户主信息过期时间
	 */
	public final static int AUTH_USER_MAIN_TIMES = 60 * 60 * 24 * 2;
	
	/**
	 * redis 缓存服务用户
	 */
	public final static String SERV_MAIN_USERLIST = "bh.serv.main.userlist.";
	
	/**
	 * redis 缓存服务信息key
	 */
	public final static String SERV_MAIN_INFO = "bh.serv.main.info.";
	
	/**
	 * redis 缓存推广员信息key
	 */
	public final static String AUTH_PROMOTER_INFO = "bh.auth.promoter.info.";
	
	/**
	 * redis 缓存会话key
	 */
	public static final String AUTH_USER_SESSION = "bh.auth.user.session.";
}
