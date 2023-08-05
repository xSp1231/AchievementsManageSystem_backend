package com.example.infomanagesystem.controller;

import com.example.infomanagesystem.entity.Student;
import com.example.infomanagesystem.result.R;
import com.example.infomanagesystem.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.*;
import sun.security.util.Password;

/**
 * @Author xushupeng
 * @Date 2023-08-05 0:06
 */
@RestController
@CrossOrigin
public class PasswordRecoveryController {
    @Autowired
    private StudentService studentService;

    @Autowired
    StringRedisTemplate stringRedisTemplate;


    //校验用户输入的用户名 密码
    @PostMapping("/checkUsernameAndEmail")
    public R checkUsernameAndEmail(@RequestBody Student student){
        System.out.println("得到的student is "+student);
        String username=student.getUsername();
        String email=student.getEmail();
        int  res=studentService.checkUsernameAndEmail(username,email);
        if(res==1){
            return new R(true,200,"用户名,邮箱号匹配成功");
        }
        else if(res==0){
            return new R(false,400,"用户名输入错误!(用户不存在)");
        }
        else{
            return new R(false,400,"邮箱号填写错误(可能是添加了@qq.com后缀)");
        }
    }

    //校验用户输入的邮箱验证码
    @PostMapping("/checkEmailVerifyCode")
    public R checkEmailVerifyCode(@RequestParam(value = "code",required = false) String code){
        System.out.println("code is "+code);
        //之后检测验证码是否正确
        String EmailVerifyCodeKey="EmailVerifyCode:"+code; //得到邮箱验证码的键
        ValueOperations<String,String> ops=stringRedisTemplate.opsForValue();
        if(ops.get(EmailVerifyCodeKey)==null){//验证码错误 或者失效
            return new R(false,404,"验证码错误或失效,请重新尝试");
        }
        else{
            return new R(true,200,"验证码正确");
        }
    }

    @PostMapping("/ResetPassword")
    public R ResetPassword(@RequestBody Student student,@RequestParam("code") String code ){
        //需要检测验证码  正确有效 才可以编辑
        System.out.println("student is "+student);
        System.out.println("code is "+code);
        String EmailVerifyCodeKey="EmailVerifyCode:"+code; //得到邮箱验证码的键
        ValueOperations<String,String> ops=stringRedisTemplate.opsForValue();
        if(ops.get(EmailVerifyCodeKey)==null){//验证码错误 或者失效
            return new R(false,404,"验证码已经失效,请重新尝试");
        }
        else{//有效期间 密码更改
           if (studentService.updateStudent(student)){
               //将验证码删除
               stringRedisTemplate.delete(EmailVerifyCodeKey);
               return new R(true,200,"密码重置成功");
           }
            else{
               return  R.error();
           }
        }

    }

}
