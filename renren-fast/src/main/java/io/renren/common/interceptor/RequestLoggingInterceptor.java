package io.renren.common.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class RequestLoggingInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURL = request.getRequestURL().toString(); // 获取请求地址
        System.out.println("Request URL: " + requestURL); // 打印请求地址
        // 可以在这里添加更多的逻辑，比如记录日志等
        return true; // 继续流程
    }
}