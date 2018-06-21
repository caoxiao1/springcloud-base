package com.echinalife.clbh.cloud.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/jasypt")
public class JasyptController {
	
	@Value("${secret.property}")
	private String demoPwd;
	
//	@Value("${ext.name}")
//	private String externalName;
	
	@RequestMapping(value = "/val", method = RequestMethod.GET)
    public String getDemoPwdFromVal() {
		return demoPwd;			// chupacabras
    }
	
//	@RequestMapping(value = "/en", method = RequestMethod.GET)
//    public String getDemoPwdFromEnvironment() {
//		environment.getProperty("secret.property");
//		return demoPwd;			// chupacabras
//    }
	
}
