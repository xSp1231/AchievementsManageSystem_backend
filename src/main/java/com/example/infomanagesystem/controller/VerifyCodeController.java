package com.example.infomanagesystem.controller;
import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import cn.hutool.captcha.generator.RandomGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * @Author xushupeng
 * @Date 2023-08-02 15:15
 */
@RestController
@CrossOrigin
public class VerifyCodeController {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @GetMapping("/getCode")
    public void getCode(HttpServletResponse response){
        //随机生成4位验证码
        RandomGenerator randomGenerator = new RandomGenerator("0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ",4);
        //定义图片的显示大小
        LineCaptcha lineCaptcha = CaptchaUtil.createLineCaptcha(120, 30);
        response.setContentType("image/jpeg");
        response.setHeader("Pragma","No-cache");
        try{
            //调用父类的setGenerator()方法，设置验证码的类型
            lineCaptcha.setGenerator(randomGenerator);
            //输出到页面
            lineCaptcha.write(response.getOutputStream());
            System.out.println("生成的验证码为："+lineCaptcha.getCode().toLowerCase()); //转换为小写
            //存入redis 过期时间1min
            ValueOperations<String,String> ops=stringRedisTemplate.opsForValue();
            ops.set("verifyCode"+lineCaptcha.getCode().toLowerCase(),lineCaptcha.getCode(),1L, TimeUnit.MINUTES);

        }catch (IOException e){
            e.printStackTrace();
        }finally {
            try {
                //最后进行IO流的关闭
                response.getOutputStream().close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
