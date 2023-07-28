//package com.example.infomanagesystem.config;
//
//import cn.dev33.satoken.interceptor.SaInterceptor;
//import cn.dev33.satoken.stp.StpUtil;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
///**
// * @Author xushupeng
// * @Date 2023-07-26 19:42
// */
////路由拦截鉴权
////路由拦截器（Route Interceptor）是一种在Web框架中用于拦截HTTP请求的机制。
////它可以用来在请求到达控制器之前或之后执行某些操作，例如权限验证、日志记录、请求转发等。
//@Configuration
//public class SaTokenRouterConfigure implements WebMvcConfigurer {
//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        // 注册 Sa-Token 拦截器，校验规则为 StpUtil.checkLogin() 登录校验。
//        registry.addInterceptor(new SaInterceptor(handle -> StpUtil.checkLogin()))
//                .addPathPatterns("/**")
//                .excludePathPatterns("/login");
//    }
//}
//
