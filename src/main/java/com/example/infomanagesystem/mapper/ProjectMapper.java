package com.example.infomanagesystem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.infomanagesystem.entity.Project;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * @Author xushupeng
 * @Date 2023-07-26 11:08
 */
@Mapper
public interface ProjectMapper  extends BaseMapper<Project> {
    @Select("select count(*) from t_project") //sql语句应该为双引号
    int getProjectNum();
}
