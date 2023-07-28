//package com.example.infomanagesystem.controller;
//
//import cn.dev33.satoken.annotation.*;
//import cn.dev33.satoken.stp.StpUtil;
//import cn.dev33.satoken.util.SaResult;
//import org.springframework.web.bind.annotation.CrossOrigin;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
///**
// * @Author xushupeng
// * @Date 2023-07-26 19:53
// */
//@RestController
//@CrossOrigin
//public class routerTestController {
//
//    @RequestMapping("/user/doLogin")
//    public String doLogin(String username, String password) {
//        // 此处仅作模拟示例，真实项目需要从数据库中查询数据进行比对
//        if("zhang".equals(username) && "123456".equals(password)) {
//            StpUtil.login("xsp");
//            return "登录成功"+"token is "+StpUtil.getTokenValue() +" token name is "+StpUtil.getTokenName()+"token有效期 "+StpUtil.getTokenTimeout();
//        }
//        return "登录失败";
//    }
//
//    // 查询登录状态，浏览器访问： http://localhost:8080/user/isLogin
//    @RequestMapping("/user/isLogin")
//    public String isLogin() {
//        System.out.println("该账号所拥有的权限有"+StpUtil.getPermissionList());
//        System.out.println(StpUtil.getRoleList());
//        return "当前会话是否登录：" + StpUtil.isLogin();
//    }
//
//    @RequestMapping("/user/test")    //先看是否登录 之后看是否拥有用户角色
//    public String test() {
//        System.out.println("路由被拦截了");
//        return "路由拦截";
//    }
//
//
//
//
//
//
//
//
//
//}
