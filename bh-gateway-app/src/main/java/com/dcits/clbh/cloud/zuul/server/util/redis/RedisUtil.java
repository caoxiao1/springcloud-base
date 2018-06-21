package com.dcits.clbh.cloud.zuul.server.util.redis;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPoolConfig;

/**
 * 集群连接池
 * 
 * 参照：
 * 		https://my.oschina.net/zhuguowei/blog/411077
 */
public class RedisUtil {
	
	/**
	 * 可用连接实例的最大数目，默认值为8；
	 * 如果赋值为-1，则表示不限制；如果pool已经分配了maxActive个jedis实例，则此时pool的状态为exhausted(耗尽)。
	 * */
	private final static int MAX_TOTAL = RedisConfigEnv.getRedisMaxConnect();

	/**
	 * 最大空闲连接数
	 * 控制一个pool最多有多少个状态为idle(空闲的)的jedis实例，默认值也是8。
	 * */
	private final static int MAX_IDLE = RedisConfigEnv.getRedisMacIdle();

	/**
	 * 最大等待时间
	 * 等待可用连接的最大时间，单位毫秒，默认值为-1，表示永不超时。如果超过等待时间，则直接抛出JedisConnectionException；
	 * */
	private final static Long MAX_WAIT_MILLIS = RedisConfigEnv.getRedis_max_wait();
	
	/**
	 * 在borrow一个jedis实例时，是否提前进行validate操作；如果为true，则得到的jedis实例均是可用的；
	 * */
	private final static boolean TEST_ON_BORROW = true;
	
	/**
	 * Redis服务器IP
	 * */
	// private static String ADDR = "192.168.0.100";
	private final static String ADDR = RedisConfigEnv.getRedisIp();
	// redisIp=10.31.23.179,10.31.23.179,10.31.23.179,10.31.23.179,10.31.23.179,10.31.23.179

	/**
	 * Redis服务器端口号
	 * */
	// private static int PORT = 6379;
	private final static String PORT = RedisConfigEnv.getRedisPort();
	// redisPort=7001,7002,7003,7004,7005,7006
	
	/**
	 * 访问密码
	 * */
	private final static String AUTH = RedisConfigEnv.getRedisPassword();
	
	/**
	 * 超时
	 * */
	private final static Integer TIMEOUT = RedisConfigEnv.getRedisTimeout();
	
	/**
	 * TODO
	 * */
	private final static Integer SOTIMEOUT = RedisConfigEnv.getRedisSoTimeout();
	
	/**
	 * 集群节点信息
	 * */
	private static Set<HostAndPort> redisClusterNodes = new HashSet<HostAndPort>();
	
	/**
	 * 集群 master 节点信息
	 * */
//	private static List<HostAndPort> redisMasterNodes = null;
	
	/**
	 * 集群对象 jedis 2.9+ 以后才有的
	 * */
	private static JedisCluster jc = null;

	// 初始化Redis连接池
	static {
		try {
			// 连接池参数配置
			JedisPoolConfig config = new JedisPoolConfig();
			config.setMaxTotal(MAX_TOTAL);
			config.setMaxIdle(MAX_IDLE);
			config.setMaxWaitMillis(MAX_WAIT_MILLIS);
			config.setTestOnBorrow(TEST_ON_BORROW);
			
			// redis 服务器配置
			String[] addrs = ADDR.split(",");
			String[] ports = PORT.split(",");
			for (int i = 0; i < addrs.length; i++) {
				String addr = addrs[i];
				int port = 6379;
				try {
					String portstr = ports[i];
					port = Integer.parseInt(portstr);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
				redisClusterNodes.add(new HostAndPort(addr, port));
			}
			
			if (null != AUTH && !AUTH.isEmpty()) {
				jc = new JedisCluster(redisClusterNodes, TIMEOUT, SOTIMEOUT, 3, AUTH, config);
				
			} else {
			    jc = new JedisCluster(redisClusterNodes, TIMEOUT, SOTIMEOUT, 3, config);
				
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * del
	 * */
	public static void del(String key) {
        jc.del(key);
    }
	
	/**
	 * del
	 * */
	public static void del(byte[] key) {
        jc.del(key);
    }
	
	/**
     * update expire time
     * */
    public static void updateExpireTime(byte[] key, int seconds){
    	jc.expire(key, seconds);
    }
    
    /**
     * update expire time
     * */
    public static void updateExpireTime(String key, int seconds){
    	jc.expire(key, seconds);
    }
	
	/**
	 * string get
	 * */
	public static String get(String key) {
		return jc.get(key);  
	}
	
	/**
	 * string set
	 * */
	public static void set(String key, String value) {
		jc.set(key, value);  
	}
	
    /**
	 * string set with expire time
	 * */
    public static void setex(String key, String value, int seconds) {
        jc.setex(key, seconds , value);
    }
    
    /**
	 * hash set
	 * */
    public static void hset(String key, String field, String value) {
        jc.hset(key, field, value);
    }
    
    /**
	 * hash set with expire time
	 * */
    public static void hset(String key, String field, String value, int seconds) {
        jc.hset(key, field, value);
        jc.expire(key, seconds);
    }
    
    /**
	 * hash set
	 * */
    public static void hset(String key, Map<String, String> hash) {
        jc.hmset(key, hash);
    }
    
    /**
	 * hash set with expire time
	 * */
    public static void hset(String key, Map<String, String> hash, int seconds) {
        jc.hmset(key, hash);
        jc.expire(key, seconds);
    }
    
    /**
   	 * hash get
   	 * */
    public static String hget(String key, String field) {
       return jc.hget(key, field);
    }
    
    /**
   	 * hash get
   	 * */
    public static List<String> hget(String key, String... fields) {
       return jc.hmget(key, fields);
    }
    
    /**
   	 * hash getAll
   	 * */
    public static Map<String, String> hgetAll(String key) {
       return jc.hgetAll(key);
    }
    
    /**
	 * list set
	 * */
    public static void rpush(String key, String... vals) {
        jc.rpush(key, vals);
    }
    
    /**
	 * list set with expire time
	 * */
    public static void rpush(String key, int seconds, String... vals) {
        jc.rpush(key, vals);
        jc.expire(key, seconds);
    }
    
    /**
   	 * list get
   	 * */
    public static List<String> lrange(String key, long start, long end) {
    	 return jc.lrange(key, 0, -1);
    }
    
    /**
	 * set sadd
	 * */
    public static void sadd(String key, String... members) {
        jc.sadd(key, members);
    }
    
    /**
	 * set sadd with expire time
	 * */
    public static void sadd(String key, int seconds, String... members) {
        jc.sadd(key, members);
        jc.expire(key, seconds);
    }
    
    /**
	 * set sadd
	 * */
    public static Set<String> smembers(String key) {
         return jc.smembers(key);
    }
    
    /**
     * set bytes with expire time
     * */
    public static void setByte(byte[] key, byte[] value, int seconds){
    	if (!jc.exists(key)) {
    		jc.set(key, value);
		}
    	jc.expire(key, seconds);
    } 
    
    /**
     * set bytes
     * */
    public static byte[] getByte(byte[] key){
    	return jc.get(key);
    }
    
    /**
     * 在一个无限循环中不停的读写，测试主从切换时，仍够提供服务
     * 
     * @throws InterruptedException
     */
    public static void setAndWriteStringValueRepeatedly() throws InterruptedException{
        String key = "test_oper_during_failover";
        jc.del(key);
        long failureTime = 0;
        long recoveryTime = 0;
        while(true){
            try {
                String result = jc.get(key);
                if(failureTime != 0 && recoveryTime==0){
                    recoveryTime =  System.currentTimeMillis();
                    System.out.println("Cluster is recovered! Downtime lasted "+(recoveryTime-failureTime)+" ms");
                }
                     
                System.out.println(result);
                jc.set(key, System.currentTimeMillis()+"");
                 
            } catch (Exception e) {
                if(failureTime==0)
                    failureTime=System.currentTimeMillis();
                e.printStackTrace();
            }
            Thread.sleep(1000);
        }
         
    }
    
//    public static void main(String[] args) throws InterruptedException {
//    	setAndWriteStringValueRepeatedly();
//    	
//	}
	
}
