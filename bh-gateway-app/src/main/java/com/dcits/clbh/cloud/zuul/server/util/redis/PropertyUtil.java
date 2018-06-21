package com.dcits.clbh.cloud.zuul.server.util.redis;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * properties 配置文件读取工具类
 * */
public class PropertyUtil {
	
	private static Map<String, PropertyUtil> mgrs = new HashMap<>();
	private Properties props = null;

	private PropertyUtil(String name) {
		props = new Properties();
		InputStream in = null;
		try {
			in = getPropertiesStream(name);
			props.load(in);
		} catch (IOException ie) {
			ie.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private InputStream getPropertiesStream(String name) {
		String filename = "/" + name + ".properties";
		InputStream in = getClass().getResourceAsStream(filename);
		return in;
	}
	
	/**
	 * 从指定配置文件中读取配置属性
	 * 
	 * @param name 配置文件名称
	 * @return
	 * */
	public static synchronized PropertyUtil getPropertyManager(String name) {
		PropertyUtil item = mgrs.get(name);
		if (item == null) {
			item = new PropertyUtil(name);
			mgrs.put(name, item);
			return item;
			
		} else {
			return item;
			
		}
	}
	
	/**
	 * 从全局的 Properties 对象中取得属性，如果已加载的多个配置文件中存在重复的键，那么将返回最后读取的配置文件中的键值
	 * 
	 * @param propName 属性键
	 * @return
	 * */
	public String getProperty(String propName) {
		return props.getProperty(propName);
	}

}
