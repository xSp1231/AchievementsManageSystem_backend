package com.example.infomanagesystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.infomanagesystem.entity.Message;
import com.example.infomanagesystem.entity.Monograph;
import com.example.infomanagesystem.entity.PictureEntity.MonographPicture;
import com.example.infomanagesystem.mapper.MessageMapper;
import com.example.infomanagesystem.mapper.MonographMapper;
import com.example.infomanagesystem.mapper.PictureMapper.MonographPictureMapper;
import com.example.infomanagesystem.service.MessageService;
import com.example.infomanagesystem.service.MonographService;
import com.example.infomanagesystem.service.StudentService;
import com.example.infomanagesystem.utils.UploadUtil;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author xushupeng
 * @Date 2023-07-25 15:05
 */
@Service
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message>  implements MessageService {
   @Autowired
   MessageMapper messageMapper;

    @Override
    public List<Message> getAllMessage(String username) {
        return messageMapper.getAllMessage(username);
    }
}
