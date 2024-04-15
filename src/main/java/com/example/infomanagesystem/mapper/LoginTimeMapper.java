package com.example.infomanagesystem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.infomanagesystem.entity.LoginTime;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface LoginTimeMapper extends BaseMapper<LoginTime> {
    @Select("select count(*) from t_login_time where username=#{username} and loginTime = #{loginTime}") //sql语句应该为双引号
    int GetLoginNumber(String username,String loginTime);
    @Update("update t_login_time set num = #{num} where username = #{username} and loginTime = #{loginTime}")
    Boolean updateByNum(String username, String num,String loginTime);
    @Select("select num from t_login_time where username = #{username} and loginTime = #{loginTime}") //sql语句应该为双引号
    int GetTrueLoginNumber(String username,String loginTime);
    @Select("select * from t_login_time where username = #{username}")
    List<LoginTime> GetLoginMessage(String username);
}
