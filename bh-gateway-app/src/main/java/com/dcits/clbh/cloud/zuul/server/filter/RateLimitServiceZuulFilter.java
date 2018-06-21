package com.dcits.clbh.cloud.zuul.server.filter;

import java.net.URL;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap.KeySetView;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.endpoint.SystemPublicMetrics;
import org.springframework.boot.actuate.metrics.Metric;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import com.google.common.collect.Maps;
import com.google.common.util.concurrent.RateLimiter;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;

/**
 * 在 Route 服务路由过滤器之后，否则无法根据微服务路由来进行限流，全局微服务限流不区别微服务，所以其 order 最大！
 * */
public class RateLimitServiceZuulFilter extends ZuulFilter {
	
    private Map<String, RateLimiter> map = Maps.newConcurrentMap();
    
    @Autowired
    private SystemPublicMetrics systemPublicMetrics;
    
    @Override
    public String filterType() {
        return FilterConstants.PRE_TYPE;
    }
    
    @Override
    public int filterOrder() {
        // 这边的order一定要大于org.springframework.cloud.netflix.zuul.filters.pre.PreDecorationFilter的order
        // 也就是要大于5
        // 否则，RequestContext.getCurrentContext()里拿不到serviceId等数据。
        return Ordered.LOWEST_PRECEDENCE;
    }
    
    @Override
    public boolean shouldFilter() {
        // 这里可以考虑弄个限流开启的开关，开启限流返回true，关闭限流返回false，你懂的。
//        return true;
    	
    	// 这里可以考虑弄个限流开启的开关，开启限流返回true，关闭限流返回false，你懂的。
        Collection<Metric<?>> metrics = systemPublicMetrics.metrics();
        Optional<Metric<?>> freeMemoryMetric = metrics.stream()
                .filter(t -> "mem.free".equals(t.getName()))
                .findFirst();
        // 如果不存在这个指标，稳妥起见，返回true，开启限流
        if (!freeMemoryMetric.isPresent()) {
            return true;
        }
        long freeMemory = freeMemoryMetric.get()
                .getValue()
                .longValue();
        // 如果可用内存小于 1gb=1048576kb ，开启流控
        return freeMemory < 1048576L;
    	
    }
    
    @Override
    public Object run() {
        try {
            RequestContext context = RequestContext.getCurrentContext();
            HttpServletResponse response = context.getResponse();
            String key = null;
            
            // RequestContext 实例是一个 ConcurrentHashMap 实例，其key是什么？value是什么？
            KeySetView<String, Object> keySet = context.keySet();
            for (String setKey : keySet) {
            	System.out.println(String.format("key:%s, value:%s", setKey, keySet.getMappedValue()));
			}
            
            // 对于service格式的路由，走 RibbonRoutingFilter
//            String SERVICE_ID_KEY = "bh-serv-architect";
            String SERVICE_ID_KEY = "serviceId";
            String serviceId = (String) context.get(SERVICE_ID_KEY);
            if (serviceId != null) {
                key = serviceId;
                map.putIfAbsent(serviceId, RateLimiter.create(1000.0));
                
                System.out.println("Is RibbonRoutingFilter, the serviceId is:" + serviceId);
            }
            // 如果压根不走RibbonRoutingFilter，则认为是URL格式的路由
            else {
                // 对于URL格式的路由，走 SimpleHostRoutingFilter
                URL routeHost = context.getRouteHost();
                if (routeHost != null) {
                    String url = routeHost.toString();
                    key = url;
                    map.putIfAbsent(url, RateLimiter.create(2000.0));
                    
                    System.out.println("Is SimpleHostRoutingFilter, the serviceId is:" + url);
                }
            }
            RateLimiter rateLimiter = map.get(key);
            if (!rateLimiter.tryAcquire()) {
                HttpStatus httpStatus = HttpStatus.TOO_MANY_REQUESTS;
                response.setContentType(MediaType.TEXT_PLAIN_VALUE);
                response.setStatus(httpStatus.value());
                response.getWriter().append(httpStatus.getReasonPhrase());
                context.setSendZuulResponse(false);
                throw new ZuulException(
                        httpStatus.getReasonPhrase(),
                        httpStatus.value(),
                        httpStatus.getReasonPhrase()
                );
            }
        } catch (Exception e) {
            ReflectionUtils.rethrowRuntimeException(e);
        }
        return null;
    }
}
