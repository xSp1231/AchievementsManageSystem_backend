package com.example.infomanagesystem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.infomanagesystem.entity.Manager;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface ManagerMapper extends BaseMapper<Manager> {

    @Select("select count(*) from t_manager") //sql语句应该为双引号
    int getManagerNum();
}
