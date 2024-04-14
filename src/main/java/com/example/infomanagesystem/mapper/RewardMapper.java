package com.example.infomanagesystem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.infomanagesystem.entity.Reward;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * @Author xushupeng
 * @Date 2023-07-25 20:30
 */
@Mapper
public interface RewardMapper extends BaseMapper<Reward> {
    @Select("select count(*) from t_reward") //sql语句应该为双引号
    int getRewardNum();
    @Select("select count(*) from t_reward where status = #{status} and username = #{username}")
    int getStatus(String status,String username);
}
