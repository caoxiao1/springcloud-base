package com.echinalife.clbh.cloud;

import static com.google.common.collect.Lists.newArrayList;

import java.util.ArrayList;
import java.util.List;

import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.joda.time.LocalDate;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.HttpMessageConverters;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.fasterxml.classmate.TypeResolver;
import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.schema.AlternateTypeRules;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.schema.WildcardType;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.Contact;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.service.Tag;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.ApiKeyVehicle;
import springfox.documentation.swagger.web.SecurityConfiguration;
import springfox.documentation.swagger.web.UiConfiguration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@RestController
@EnableDiscoveryClient
@EnableWebMvc
@SpringBootApplication
@EnableSwagger2
@MapperScan(basePackages = "com.echinalife.clbh.cloud.mapper")
@EnableEncryptableProperties
@PropertySource(name="EncryptedProperties", value = "classpath:encrypted.properties")
public class BHServDemoApplication implements CommandLineRunner {
	
    private Logger logger = LoggerFactory.getLogger(BHServDemoApplication.class);
    
    /**
     * 解密，默认使用的是 StringEncryptor 其是一个接口
     * */
    @Bean("jasyptStringEncryptor")		// 自定义
    public StringEncryptor stringEncryptor() {
        PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
        SimpleStringPBEConfig config = new SimpleStringPBEConfig();
        config.setPassword("BJAewBBitmY3bJNeixyGs45a453d2dFz");			// 密码不能如此设置，必须是在启动时，以外部入参传入
        config.setAlgorithm("PBEWithMD5AndDES");
        config.setKeyObtentionIterations("1000");
        config.setPoolSize("1");
        config.setProviderName("SunJCE");
        config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");
        config.setStringOutputType("base64");
        encryptor.setConfig(config);
        return encryptor;
    }
    
    /**
     * SPRINGMVC 的转换器
     * */
    @Bean  
    public HttpMessageConverters fastJsonHttpMessageConverters() {  
       FastJsonHttpMessageConverter fastConverter = new FastJsonHttpMessageConverter();  
       FastJsonConfig fastJsonConfig = new FastJsonConfig();  
       fastJsonConfig.setSerializerFeatures(SerializerFeature.PrettyFormat);  
       fastConverter.setFastJsonConfig(fastJsonConfig);  
       HttpMessageConverter<?> converter = fastConverter;  
       return new HttpMessageConverters(converter);  
    }  
    
    public static void main(String[] args) {
    	System.out.println("args=" + args.length);
        SpringApplication.run(BHServDemoApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        logger.info("服务启动完成!");
    }

    /**
     * 测试
     * */
    @RequestMapping("/")
    String home() {
//        return "redirect:countries";
        return "Hello World!";
    }
    
    @Bean
	public Docket petApi() {
		return new Docket(DocumentationType.SWAGGER_2)
				.apiInfo(buildApiInf())
				.select()
				.apis(RequestHandlerSelectors.basePackage("com.echinalife.clbh.cloud.controller"))
				.paths(PathSelectors.any())
				.build()
				.pathMapping("/")
				.directModelSubstitute(LocalDate.class, String.class)
				.genericModelSubstitutes(ResponseEntity.class)
				.alternateTypeRules(AlternateTypeRules.newRule(
						typeResolver.resolve(DeferredResult.class,typeResolver.resolve(ResponseEntity.class, WildcardType.class)),
						typeResolver.resolve(WildcardType.class)))
				.useDefaultResponseMessages(false)
				.globalResponseMessage(RequestMethod.GET,
						newArrayList(new ResponseMessageBuilder().code(500).message("500 message").responseModel(new ModelRef("Error")).build()))
				.securitySchemes(newArrayList(apiKey())).securityContexts(newArrayList(securityContext()))
				.enableUrlTemplating(true)
//				.globalOperationParameters(
//						newArrayList(
//								new ParameterBuilder()
//									.name("someGlobalParameter")
//									.description("Description of someGlobalParameter")
//									.modelRef(new ModelRef("string"))
//						.parameterType("query").required(true).build()))
				.tags(new Tag("Pet Service", "All apis relating to pets"))
				// .additionalModels(typeResolver.resolve(AdditionalModel.class))
				;
	}

	@Autowired
	private TypeResolver typeResolver;

	private ApiKey apiKey() {
		return new ApiKey("mykey", "api_key", "header");
	}

	private SecurityContext securityContext() {
		return SecurityContext.builder().securityReferences(defaultAuth()).forPaths(PathSelectors.regex("/anyPath.*"))
				.build();
	}

	List<SecurityReference> defaultAuth() {
		AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
		AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
		authorizationScopes[0] = authorizationScope;

		// This guava code snippet
		// return newArrayList(
		// new SecurityReference("mykey", authorizationScopes));

		// ... is equivalent to
		List<SecurityReference> list = new ArrayList<SecurityReference>();
		list.add(new SecurityReference("mykey", authorizationScopes));
		return list;
	}

	@Bean
	SecurityConfiguration security() {
		return new SecurityConfiguration("test-app-client-id", "test-app-client-secret", "test-app-realm", "test-app",
				"apiKey", ApiKeyVehicle.HEADER, "api_key",
				"," /* scope separator */);
	}

	@Bean
	UiConfiguration uiConfig() {
		return new UiConfiguration("validatorUrl", // url
				"none", // docExpansion => none | list
				"alpha", // apiSorter => alpha
				"schema", // defaultModelRendering => schema
				UiConfiguration.Constants.DEFAULT_SUBMIT_METHODS, false, // enableJsonEditor
																			// =>
																			// true
																			// |
																			// false
				true, // showRequestHeaders => true | false
				60000L); // requestTimeout => in milliseconds, defaults to null
							// (uses jquery xh timeout)
	}
	
	@Bean
	ApiInfo buildApiInf(){
        return new ApiInfoBuilder()
                    .title("Bighealth大健康微服务demo工程")
                    .description("springboot swagger2")
                    .termsOfServiceUrl("http://springfox.github.io/springfox/")
                    .contact(new Contact("slw", "http://springfox.github.io/springfox/", "superhadoopslw@163.com"))
                    .build();

    }
	
}
