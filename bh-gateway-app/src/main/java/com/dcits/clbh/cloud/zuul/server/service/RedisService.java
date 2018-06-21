package com.dcits.clbh.cloud.zuul.server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import com.dcits.clbh.cloud.zuul.server.model.MapModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

/**
 * Created by on 2017/10/25.
 * 缓存规则
 * 1.更新先更新数据库,再更新缓存
 * 2.查询是先查缓存,如果有则取出,否则查询数据库,set进缓存,设置过期时间,
 * 3.重要数据进缓存(使用频率比较大时)
 */
//@Service
public class RedisService {

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Resource(name = "stringRedisTemplate")
    ValueOperations<String, String> valOpsStr;

    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    @Resource(name = "redisTemplate")
    ValueOperations<Object, Object> valOpsObj;

    /**
     * 根据指定key获取String
     * 
     * @param key
     * @return
     */
    public String getStr(String key){
        return valOpsStr.get(key);
    }
    
    /**
     * 根据指定key获取String
     * 
     * @param key
     * @return
     */
    public Map<String, String> getStrMap(String key){
    	Map<String, String> strMap = new HashMap<>();
    	// 取得所有子 key
    	Set<String> strKeys = stringRedisTemplate.keys(key + ".*");
    	List<String> strValues = valOpsStr.multiGet(strKeys);
    	int step = 0;
        for (String strKey : strKeys) {
        	strMap.put(strKey, strValues.get(step));
        	step++;
		}
        return strMap;
    }
    
    /**
     * 根据指定key获取String
     * 
     * @param key
     * @return
     */
    public List<MapModel> getStrList(String key){
    	List<MapModel> mmlist = new ArrayList<>();
    	// 取得所有子 key
    	Set<String> strKeys = stringRedisTemplate.keys(key + ".*");
    	List<String> strValues = valOpsStr.multiGet(strKeys);
    	int step = 0;
        for (String strKey : strKeys) {
        	MapModel mm = new MapModel(strKey, strValues.get(step));
        	mmlist.add(mm);
        	step++;
		}
        return mmlist;
    }

    /**
     * 设置Str缓存
     * 
     * @param key
     * @param val
     */
    public Boolean setStr(String key, String val){
    	try {
            valOpsStr.set(key,val);
            return true;
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * 设置Map缓存
     * 
     * @param map
     */
    public Boolean setMultiStr(Map<String, String> map){
    	try {
            valOpsStr.multiSet(map);
            return true;
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * hash 取得
     * */
//    public List<MapModel> hgetAll(String key) {
//    	List<MapModel> list = new ArrayList<>();
//    	Map<Object, Object> kvs = redisTemplate.boundHashOps(key).entries();
//		for (Object k : kvs.keySet()) {
//			list.add(new MapModel(k.toString(), kvs.get(k).toString()));
//		}
//		return list;
//    }
    
    /**
     * hash 取得
     * */
    public Map<Object, Object> hgetAll(String key) {
		return redisTemplate.boundHashOps(key).entries();
    }
    
    /**
     * 删除指定key
     * 
     * @param key
     */
    public Boolean delStr(String key){
        try {
        	stringRedisTemplate.delete(key);
            return true;
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * 删除匹配key的key
     * 
     * @param key
     */
    public Boolean delMultiStrMatch(String key){
        try {
        	// 删除所有子 key
        	Set<String> delKeys = stringRedisTemplate.keys(key + ".*");
        	stringRedisTemplate.delete(delKeys);
        	// 删除当前 key
        	stringRedisTemplate.delete(key);
            return true;
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * 删除指定keys
     * 
     * @param keys
     */
    public Boolean delMultiStr(List<String> keys){
        try {
        	stringRedisTemplate.delete(keys);
            return true;
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

//    /**
//     * 根据指定o获取Object
//     * 
//     * @param o
//     * @return
//     */
//    public Object getObj(Object o){
//        return valOpsObj.get(o);
//    }
//
//    /**
//     * 设置obj缓存
//     * 
//     * @param o1
//     * @param o2
//     */
//    public void setObj(Object o1, Object o2){
//        valOpsObj.set(o1, o2);
//    }
//
//    /**
//     * 删除Obj缓存
//     * 
//     * @param o
//     */
//    public void delObj(Object o){
//        redisTemplate.delete(o);
//    }

}