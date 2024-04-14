package com.example.infomanagesystem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.infomanagesystem.entity.Manager;
import com.example.infomanagesystem.entity.Message;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Mapper
public interface MessageMapper extends BaseMapper<Message> {
    @Select("select * from t_message where username =#{username}")
    List<Message> getAllMessage(@RequestParam String username);  //获取发给用户的所有消息
}
