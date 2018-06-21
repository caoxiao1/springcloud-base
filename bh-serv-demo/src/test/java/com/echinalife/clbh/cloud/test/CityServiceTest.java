package com.echinalife.clbh.cloud.test;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import com.echinalife.clbh.cloud.BHServDemoApplication;
import com.echinalife.clbh.cloud.model.City;
import com.echinalife.clbh.cloud.service.CityService;

/**
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Import(BHServDemoApplication.class)
public class CityServiceTest {

    @Autowired
    private CityService cityService;

    @Test
    public void test() {
        City city = new City();
        List<City> all = cityService.getAll(city);
        for (City c : all) {
            System.out.println(c.getName());
        }
    }
    
}
