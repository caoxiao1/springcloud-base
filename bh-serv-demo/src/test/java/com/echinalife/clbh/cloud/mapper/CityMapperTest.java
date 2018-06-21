package com.echinalife.clbh.cloud.mapper;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import com.echinalife.clbh.cloud.BHServDemoApplication;
import com.echinalife.clbh.cloud.model.City;

@RunWith(SpringRunner.class)
@SpringBootTest
@Import(BHServDemoApplication.class)
public class CityMapperTest {

	@Autowired
	private CityMapper cityMapper;
	
	@Test
	public void testMybatis() {
		List<City> citys = cityMapper.selectAll();
		for (City city : citys) {
			System.out.println(city.getName());
		}
	}
	
}
