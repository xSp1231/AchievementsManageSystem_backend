package com.example.infomanagesystem.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.example.infomanagesystem.entity.Message;
import com.example.infomanagesystem.result.R;
import com.example.infomanagesystem.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/Message")
public class MessageController {
    @Autowired
    private MessageService messageService;
    @GetMapping("/getUserMessage") // 获取用户接收的消息  前端携带token
    public R getUserScientificPaper() {
        System.out.println("个人信息会话号码(账号)"+ StpUtil.getLoginId());
        String username= (String) StpUtil.getLoginId();
        List<Message> mlist=messageService.getAllMessage(username);
        for(Message message:mlist){
            System.out.println(message);
        }
        //之后根据username 获取相应的列表
        return new R(true,200,"获得登录者的专著信息",messageService.getAllMessage(username));
    }
}
