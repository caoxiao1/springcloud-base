package com.dcits.clbh.cloud.zuul.server;

import javax.servlet.http.HttpServletRequest;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

/**
 * 自定义路由过滤器
 * */
public class AccessFilter extends ZuulFilter{

	public Object run() {
		// TODO Auto-generated method stub
		RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        System.out.println("request.getServletPath()=" + request.getServletPath());
        
        Object accessToken = request.getParameter("accessToken");
//        if(accessToken == null) {
//	        ctx.setSendZuulResponse(false);
//	        ctx.setResponseStatusCode(401);
//	        return null;
//        }
//        if (request.getParameter("foo") != null) {
//		    // put the serviceId in `RequestContext`
//    		ctx.put(SERVICE_ID_KEY, request.getParameter("foo"));
//    	}
		return true;
	}

	public boolean shouldFilter() {
//		RequestContext ctx = RequestContext.getCurrentContext();
//		return !ctx.containsKey(FORWARD_TO_KEY) // a filter has already forwarded
//				&& !ctx.containsKey(SERVICE_ID_KEY); // a filter has already determined serviceId
		return true;
	}

	@Override
	public int filterOrder() {
		return 0;
	}

	@Override
	public String filterType() {
		return "pre";
	}
	
	

}
