package com.echinalife.clbh.cloud.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import com.echinalife.clbh.cloud.BHServDemoApplication;

/**
 * 测试缓存
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Import(BHServDemoApplication.class)
public class TestRedisService {

	@Autowired
	private RedisService redisService;
	
	@Test
    public void test1() {
		redisService.setStr("test.key.001", "bh-serv-demo 2018-01-24");
    }
	
}
