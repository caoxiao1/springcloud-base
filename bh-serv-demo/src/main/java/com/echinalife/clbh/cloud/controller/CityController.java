package com.echinalife.clbh.cloud.controller;

import com.echinalife.clbh.cloud.model.City;
import com.echinalife.clbh.cloud.service.CityService;
import com.echinalife.clbh.cloud.util.JsonUtils;
import com.github.pagehelper.PageInfo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Api(value="城市API相关接口", tags={"城市"})
@RestController
@RequestMapping("/cities")
public class CityController extends BaseController {

	private final Logger logger = Logger.getLogger(getClass());
	
    @Autowired
    private CityService cityService;

    @ApiOperation(value = "取得所有的城市信息", notes = "取得所有的城市信息") 
    @RequestMapping(value = "/all", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public PageInfo<City> getAll(@ApiParam(value = "城市入参对象", required = true) City city) {
    	logger.info("取得所有的城市信息");
    	
        List<City> countryList = cityService.getAll(city);
        
        // 记录打印日志
        serviceApiLog(countryList);
        
        return new PageInfo<City>(countryList);
    }

    @RequestMapping(value = "/add", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public City add() {
        return new City();
    }

    @RequestMapping(value = "/view/{id}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public City view(@PathVariable Integer id) {
        City city = cityService.getById(id);
        
        // 记录打印日志
        serviceApiLog(city);
        
        return city;
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public ModelMap delete(@PathVariable Integer id) {
        ModelMap result = new ModelMap();
        cityService.deleteById(id);
        result.put("msg", "删除成功!");
        
        // 记录打印日志
        serviceApiLog(result);
        
        return result;
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ModelMap save(City city) {
        ModelMap result = new ModelMap();
        String msg = city.getId() == null ? "新增成功!" : "更新成功!";
        cityService.save(city);
        result.put("city", city);
        result.put("msg", msg);
        
        // 记录打印日志
        serviceApiLog(result);
        
        return result;
    }
    
    @RequestMapping(value = "/view1/{id}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public String view1(@PathVariable Integer id) {
        City city = cityService.getById(id);
        String object2Json = JsonUtils.Object2Json(city);
        
        // 记录打印日志
        serviceApiLog(object2Json);
        
        return object2Json;
    }

}
