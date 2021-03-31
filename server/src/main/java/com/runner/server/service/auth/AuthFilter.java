package com.runner.server.service.auth;

import com.runner.server.service.utils.LogUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.util.ObjectUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;


public class AuthFilter implements HandlerInterceptor {



    /**
     * @description  全局请求拦截
     * @author 星空梦语
     * @date 2021/3/9 下午4:15
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        HttpSession session = request.getSession();
        LogUtil.info("当前会话id："+session.getId());
        String urlString = request.getRequestURI();
        if( !urlString.endsWith("login") && !urlString.endsWith("registerUser")){
            if (ObjectUtils.isEmpty(session.getAttribute("auth"))) {  //请求session中不存在登录标识则返回401
                response.setStatus(401);
                return false;
            }
        }

        return true;
    }
}
