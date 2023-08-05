package com.example.infomanagesystem.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @Author xushupeng
 * @Date 2023-08-05 14:42
 */
@Service
public class MailService {
    private static final Logger logger = LoggerFactory.getLogger(MailService.class);

    /**
     * 注入邮件工具类
     */
    @Autowired
    private JavaMailSenderImpl MailSender;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Value("${spring.mail.username}")
    private String sendMailer; //发送者的邮箱

    public String sendTextMailMessage(String to){ //to  指的是 接收者的邮箱号
        try {
            //true 代表支持复杂的类型
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(MailSender.createMimeMessage(),true);
            //邮件发信人
            mimeMessageHelper.setFrom(sendMailer);
            //邮件收信人  1或多个
            mimeMessageHelper.setTo(to.split(","));
            //邮件主题
            mimeMessageHelper.setSubject("Swust-学生成果管理系统验证码");
            //邮件内容 构建邮件内容
            // 构建HTML内容
            String base="0123456789abcdefghijklmnopqrstuvwxyz";
            Random random = new Random();
            StringBuilder code= new StringBuilder();

            for (int i = 0; i < 6; i++) {
                int randomIndex = random.nextInt(base.length());
                char randomChar = base.charAt(randomIndex);
                code.append(randomChar);
            }
            //得到随机验证码 6位 存入redis  设置过期时间
            ValueOperations<String,String> ops=stringRedisTemplate.opsForValue();
            ops.set("EmailVerifyCode:"+code, String.valueOf(code),6L, TimeUnit.MINUTES);//有效时间6mins
            System.out.println("邮箱验证码存入redis成功");
            String html = "<html><body>";
            html += "<h1>"+to+"用户,以下是找回密码所以需要的验证^_^</h1>";
            html += "<h2 style=\"color: red;\">"+code+"</h2>";
            html += "<ul>";
            html += "<li>验证码不要告诉他人</li>";
            html += "<li>验证码在6分钟内有效，请尽快完成找回密码操作</li>";
            html += "</ul>";
            html += "<h2>swust-学生成果管理系统</h2>";
            html += "</body></html>";

            mimeMessageHelper.setText(html,true);//可以发送html邮件
            //邮件发送时间
            mimeMessageHelper.setSentDate(new Date());

            //发送邮件
            MailSender.send(mimeMessageHelper.getMimeMessage());
            System.out.println("发送邮件成功："+sendMailer+"->"+to);
            return "邮件发送成功";
        } catch (MessagingException e) {
            e.printStackTrace();
            System.out.println("发送邮件失败："+e.getMessage());
            return "邮件发送失败";
        }
    }
}
