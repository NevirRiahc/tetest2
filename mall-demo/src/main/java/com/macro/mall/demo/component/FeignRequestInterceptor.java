package com.macro.mall.demo.component;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

/**
 * Used as an interceptor for Feign to pass request headers
 */
public class FeignRequestInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate requestTemplate) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes();
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            Enumeration<String> headerNames = request.getHeaderNames();
            if (headerNames != null) {
                while (headerNames.hasMoreElements()) {
                    String name = headerNames.nextElement();
                    String values = request.getHeader(name);
                    //There is a truncation issue when the value of the request header is in JSON format
                    if("user".equals(name)&&StrUtil.isNotEmpty(values)){
                        JSONObject jsonObject = JSONUtil.parseObj(values);
                        String id = jsonObject.getStr("id");
                        values = JSONUtil.createObj().putOnce("id",id).toString();
                    }
                    requestTemplate.header(name, values);
                }
            }
        }
    }
}
