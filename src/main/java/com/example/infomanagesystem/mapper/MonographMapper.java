package com.example.infomanagesystem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.infomanagesystem.entity.Monograph;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * @Author xushupeng
 * @Date 2023-07-25 15:04
 */
@Mapper
public interface MonographMapper extends BaseMapper<Monograph> {
    @Select("select count(*) from t_monograph") //sql语句应该为双引号
    int getMonographNum();
    @Select("select count(*) from t_monograph where status = #{status} and username = #{username}")
    int getStatus(String status,String username);
}
