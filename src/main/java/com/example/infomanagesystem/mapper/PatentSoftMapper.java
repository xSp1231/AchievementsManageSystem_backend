package com.example.infomanagesystem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.infomanagesystem.entity.PatentSoft;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * @Author xushupeng
 * @Date 2023-07-25 17:45
 */
@Mapper
public interface PatentSoftMapper extends BaseMapper<PatentSoft> {
    @Select("select count(*) from t_patentsoft") //sql语句应该为双引号
    int getPatentsoftNum();
    @Select("select count(*) from t_patentsoft where status = #{status} and username = #{username}")
    int getStatus(String status,String username);
    @Select("select count(*) from t_patentsoft  where status = #{status}")
    int getAllStatus(String status);
}
