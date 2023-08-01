package com.example.infomanagesystem.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.infomanagesystem.entity.Reward;

import java.util.List;

/**
 * @Author xushupeng
 * @Date 2023-07-25 20:31
 */
public interface RewardService  extends IService<Reward> {
    List<Reward> getAllReward();

    //得到登录者的奖励成果信息
    List<Reward> getUserReward(String username);

    //编辑的时候 根据成果的id来获取相应成果的填报信息  //用户确定更改 状态为审核
    Reward getRewardById(Integer id);

    boolean saveReward(Reward reward); //奖励信息的增加    管理员 学生

    boolean deleteReward(Integer id);//奖励信息的删除 注销 (管理员 学生)
    //批量删除
    void deleteBatch(List<Integer> ids); //批量删除 通过传入的一组ids 来删除
    //修改
    boolean updateReward(Reward reward); //奖励信息的修改 (管理员 学生)
    //分页
    IPage<Reward> getPage(int currentPage, int pageSize, Reward reward); //当前所在页 每页多少条(管理员)

    // 批量导出
    List<Reward> getByids(List<Integer> ids); //查询id在ids这个数组里面的所有数据

    Boolean deleteAllRewardOfUsername(String username);

}
