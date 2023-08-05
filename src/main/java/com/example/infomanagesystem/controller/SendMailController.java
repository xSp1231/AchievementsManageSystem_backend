package com.example.infomanagesystem.controller;
import com.example.infomanagesystem.result.R;
import com.example.infomanagesystem.service.impl.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author xushupeng
 * @Date 2023-08-05 14:47
 */

@CrossOrigin
@RestController
public class SendMailController {
    @Autowired
    private MailService mailService; //@Autowired注解的效果和使用接口相同，都是将MailService类的实例自动注入到当前类中。

    @GetMapping("/sendMail/{to}")
    public R sendTextMail(@PathVariable  String to){//前端传过来的是qq号
        to+="@qq.com"; //也可以使用to.concat("@qq.com")
        System.out.println("接收者的邮箱号 is "+to);
        if("邮件发送成功".equals(mailService.sendTextMailMessage(to))){
           return R.success();
        }
           return R.error();
        }
}
