package com.example.infomanagesystem.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.infomanagesystem.entity.Message;
import com.example.infomanagesystem.entity.Monograph;

import java.util.List;

/**
 * @Author xushupeng
 * @Date 2023-07-25 15:05
 */
public interface MessageService extends IService<Message> {
     List<Message> getAllMessage(String username);
}
