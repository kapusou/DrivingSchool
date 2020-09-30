package com.llb.interception;

import com.llb.entity.RedisConn;
import com.llb.utils.RedisUtils;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * 驾校管理系统拦截器：验证用户是否登录，如果没有登录则重定向到login.html页面
 * @Author llb
 * Date on 2020/3/25
 */
public class LoginIntercept implements HandlerInterceptor{

//    @Autowired
//    private RedisUtils redisUtils;
    /**
     * 逻辑判断，通过获取session来判断用户是否登录，如果登录放行，如果没有登录则拦截
     * @param request
     * @param response
     * @param handler
     * @return
     */
    @Override
        public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws ServletException, IOException {
        HttpSession session = request.getSession();
//        手动注入bean
        BeanFactory factory = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getServletContext());
        RedisUtils redisUtils = (RedisUtils) factory.getBean("redisUtils");

        //获取session
        /*Object student = session.getAttribute("student");*/
        Object admin = session.getAttribute("admin");
        Object teacher = session.getAttribute("teacher");

        Object student = redisUtils.get("student");
        //获取请求路径，根据请求路径来区分登录角色
        String servletPath = request.getServletPath();
        if(servletPath.contains("/student/")) {
            if(student == null) {
//                request.getRequestDispatcher(request.getContextPath()+"/login.html").forward(request, response);
                response.sendRedirect(request.getContextPath() + "/login/index");
                return false;
            }
        } else if(servletPath.contains("/admin/")) {
            if(admin == null) {
//                request.getRequestDispatcher(request.getContextPath()+"/login.html").forward(request, response);
                response.sendRedirect(request.getContextPath() + "/login/index");
                return false;
            }
        } else if(servletPath.contains("/teacher/")) {
            if(teacher == null) {
//                request.getRequestDispatcher(request.getContextPath()+"/login.html").forward(request, response);
                response.sendRedirect(request.getContextPath() + "/login/index");
                return false;
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {

    }
}
