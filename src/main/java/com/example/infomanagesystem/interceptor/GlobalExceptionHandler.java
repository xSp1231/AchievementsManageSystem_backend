package com.example.infomanagesystem.interceptor;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.exception.NotRoleException;
import cn.dev33.satoken.util.SaResult;
import com.example.infomanagesystem.result.R;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @Author xushupeng
 * @Date 2023-08-07 10:01
 */
//全局异常拦截类  必须加 @RestControllerAdvice注解
@RestControllerAdvice
public class GlobalExceptionHandler {
    // 拦截：未登录异常 token过期
    @ExceptionHandler(NotLoginException.class)
    public R handlerException(NotLoginException e) {
        // 打印堆栈，以供调试
        e.printStackTrace();
        // 返回给前端
        return new R(false,404,"登录已经过期,请重新登录");
    }


//    // 拦截：缺少角色异常
//    @ExceptionHandler(NotRoleException.class)
//    public R handlerException(NotRoleException e) {
//        e.printStackTrace();
//        return new R(false,"暂无权限");
//    }




    // 拦截：其它所有异常
//    @ExceptionHandler(Exception.class)
//    public SaResult handlerException(Exception e) {
//        e.printStackTrace();
//        return SaResult.error(e.getMessage());
//    }

}
