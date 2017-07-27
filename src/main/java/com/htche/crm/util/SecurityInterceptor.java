package com.htche.crm.util;

import com.alibaba.fastjson.JSON;
import com.htche.crm.model.ApiResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by jankie on 16/5/26.
 */
@Component
public class SecurityInterceptor implements HandlerInterceptor {

    protected static final Logger logger = LoggerFactory.getLogger(SecurityInterceptor.class);


    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws Exception {

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request,
                           HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
    }

    private void flush(int status, String err, HttpServletRequest request
            , HttpServletResponse response) throws Exception {

        ApiResult apiResult = new ApiResult();
        apiResult.setStatus(status);
        apiResult.setMessage(err);

        String msg = JSON.toJSONString(apiResult);
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Allow-Methods", "POST, GET, PUT, UPDATE, DELETE, OPTIONS");
        response.addHeader("Access-Control-Allow-Credentials", "true");
        response.addHeader("Access-Control-Allow-Headers",
                "Authorization, origin, content-type, accept, x-requested-with, x-custom-header");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.getWriter().write(msg);
        response.getWriter().flush();
    }
}
