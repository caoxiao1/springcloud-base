package com.echinalife.clbh.cloud.jasypt;

import java.util.List;

import javax.annotation.Resource;

import org.jasypt.encryption.StringEncryptor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import com.echinalife.clbh.cloud.BHServDemoApplication;
import com.echinalife.clbh.cloud.mapper.CityMapper;
import com.echinalife.clbh.cloud.model.City;

/**
 * 
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Import(BHServDemoApplication.class)
public class TestJasypt {

//	@Qualifier("jasyptStringEncryptor")
	@Resource(name="jasyptStringEncryptor")
    StringEncryptor stringEncryptor;
	
	@Value("${spring.datasource.password}")
    private String password;
	
	@Autowired
	private CityMapper cityMapper;
	
	/**
	 * 加密配置文件中的密码
	 * */
    @Test
    public void encryp() {
    	// spring.datasource.password=Bighealth@123
        System.out.println(stringEncryptor.encrypt("Bighealth@123"));
        // TXLWcltNkZ4XOL6CpDTPefz9BUN7HrYN
        
        // spring.redis.password=redis1234
        System.out.println(stringEncryptor.encrypt("redis1234"));
        // LFqCYayxmMPuD1EE2Obzho0+bMjlp0vE
        
    }
 
    /**
     * 解密配置文件中的密码，并执行 mybatis 检索数据库
     * */
    @Test
    public void decryp() {
    	System.out.println("连接数据库密码:" + password);
    	List<City> citys = cityMapper.selectAll();
		for (City city : citys) {
			System.out.println(city.getName());
		}
    }
    
}
