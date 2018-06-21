package com.dcits.clbh.cloud.zuul.server.filter;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Map;

import javax.servlet.ServletInputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StreamUtils;

import com.dcits.clbh.cloud.zuul.server.util.CacheConstants;
import com.dcits.clbh.cloud.zuul.server.util.CookieUtil;
import com.dcits.clbh.cloud.zuul.server.util.redis.RedisUtil;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.http.ServletInputStreamWrapper;

/**
 * 自定义路由过滤器
 */
public class AccessFilter extends ZuulFilter {

	private static Logger logger = LoggerFactory.getLogger(AccessFilter.class);

	private static final String SID_COOKIE_NAME = "sid";

	private static final String USER_ID_HEDAER_NAME = "X-USER-ID";
	
	private String unfilterUrl = "";

	// @Autowired
	// private RedisService redisService;

	@Override
	public Object run() {
		RequestContext ctx = RequestContext.getCurrentContext();
		logger.info("request.getServletPath()=" + ctx.getRequest().getServletPath());
		logger.info("params:" + ctx.getRequestQueryParams());
		logger.info("contentLength:" + ctx.getRequest().getContentLength());
		logger.info("contentType:" + ctx.getRequest().getContentType());

		boolean access = true;

		// 取得会话id
		Cookie ck = CookieUtil.getCookie(ctx.getRequest(), SID_COOKIE_NAME);
		if (null != ck) {
			String sid = ck.getValue();
			if (sid == null) {
				logger.info("sid is empty");
				// ctx.setSendZuulResponse(false);
				// ctx.setResponseStatusCode(401);
				// try {
				// ctx.getResponse().getWriter().write("sid is empty");
				// }catch (Exception e){
				//
				// }
				// return null;

			} else {
				// 根据 sid 从 redis 中取得共享会话
				String key = CacheConstants.AUTH_USER_SESSION + sid;
				// Map<Object, Object> kvs = redisService.hgetAll(key);
				Map<String, String> kvs = RedisUtil.hgetAll(key);
				if (null != kvs.get("userId")) {
					String userId = kvs.get("userId").toString();
					logger.info("userId<" + userId + "> is not empty");

					// 添加 userId 参数
//					setUserIdInBody(userId);
					setUserIdInHeader(userId);

				} else {
					logger.info("userId is empty");

				}

			}

		} else {
			logger.info("No Cookie");

		}

		return access;
	}
	
	private void setUserIdInHeader(String userId) {
		RequestContext ctx = RequestContext.getCurrentContext();
		ctx.addZuulRequestHeader(USER_ID_HEDAER_NAME, userId);
	}
	
	private boolean setUserIdInBody(String userId) {
		boolean access= false;
		RequestContext ctx = RequestContext.getCurrentContext();
		InputStream in = (InputStream) ctx.get("requestEntity");
		try {
			if (in == null) {
				in = ctx.getRequest().getInputStream();
			}
			String body = StreamUtils.copyToString(in, Charset.forName("UTF-8"));
			logger.info("body:" + body);
//			body = StringUtils.replace(body, "sid=" + sid, "sid=" + sid);
			body = "userId=" + userId;
			logger.info("转换后的body：" + body);
			// context.set("requestEntity", new
			// ByteArrayInputStream(body.getBytes("UTF-8")));
			final byte[] reqBodyBytes = body.getBytes();
			ctx.setRequest(new HttpServletRequestWrapper(ctx.getRequest()) {
				@Override
				public ServletInputStream getInputStream() throws IOException {
					return new ServletInputStreamWrapper(reqBodyBytes);
				}
				
				@Override
				public int getContentLength() {
					return reqBodyBytes.length;
				}
				
				@Override
				public long getContentLengthLong() {
					return reqBodyBytes.length;
				}
			});
			
			// 验证授权
			access = true;
			
		} catch (Exception e) {
			e.printStackTrace();
			
			// 验证授权
			access = false;
			
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return access;
	}

	@Override
	public boolean shouldFilter() {
		// RequestContext ctx = RequestContext.getCurrentContext();
		// return !ctx.containsKey(FORWARD_TO_KEY) // a filter has already
		// forwarded
		// && !ctx.containsKey(SERVICE_ID_KEY); // a filter has already
		// determined serviceId

		HttpServletRequest req = RequestContext.getCurrentContext().getRequest();
		logger.info("unfilterUrl=" + unfilterUrl + ", accessUrl=" + req.getRequestURI().toString());

		// URL 判定是否过滤 TODO

		return true;
	}

	@Override
	public int filterOrder() {
		// return PRE_DECORATION_FILTER_ORDER - 1;
		return 0;
	}

	@Override
	public String filterType() {
		return "pre";
	}

}
