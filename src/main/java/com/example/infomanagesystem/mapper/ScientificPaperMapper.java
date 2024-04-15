package com.example.infomanagesystem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.infomanagesystem.entity.ScientificPaper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * @Author xushupeng
 * @Date 2023-07-24 13:46
 */
//科技论文
@Mapper
public interface ScientificPaperMapper extends BaseMapper<ScientificPaper> {
    @Select("select count(*) from t_scientific") //sql语句应该为双引号
    int getScientificNum();
    @Select("select count(*) from t_scientific where status = #{status} and username = #{username}")
    int getStatus(String status,String username);
    @Select("select count(*) from t_scientific where status = #{status}")
    int getAllStatus(String status);
}
