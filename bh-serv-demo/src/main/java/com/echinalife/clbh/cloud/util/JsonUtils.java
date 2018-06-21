package com.echinalife.clbh.cloud.util;

import com.alibaba.fastjson.JSONObject;

public class JsonUtils {

	public static String  Object2Json(Object obj){  
        String jsonStr = JSONObject.toJSONString(obj);//将java对象转换为json对象  
          
        return jsonStr;  
    }  
}
