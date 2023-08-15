package com.example.infomanagesystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.infomanagesystem.entity.Monograph;
import com.example.infomanagesystem.entity.PictureEntity.MonographPicture;
import com.example.infomanagesystem.entity.PictureEntity.RewardPicture;
import com.example.infomanagesystem.entity.Reward;
import com.example.infomanagesystem.mapper.PictureMapper.RewardPictureMapper;
import com.example.infomanagesystem.mapper.RewardMapper;
import com.example.infomanagesystem.service.RewardService;
import com.example.infomanagesystem.service.StudentService;
import com.example.infomanagesystem.utils.UploadUtil;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author xushupeng
 * @Date 2023-07-25 20:32
 */
@Service
public class RewardServiceImpl extends ServiceImpl<RewardMapper, Reward> implements RewardService {
    @Autowired
    private RewardMapper rewardMapper;
    @Autowired
    private StudentService studentService;
    @Autowired
    private RewardPictureMapper rewardPictureMapper;

    @Override
    public List<Reward> getAllReward() {
        return rewardMapper.selectList(null);
    }
    //找到登录者的所有专著成果信息
    @Override
    public List<Reward> getUserReward( String username) {
        QueryWrapper<Reward> q=new QueryWrapper<>();
        q.eq("username",username);
        return rewardMapper.selectList(q);
    }

    @Override
    public Reward getRewardById(Integer id) {
        return rewardMapper.selectById(id);
    }

    @Override
    public boolean saveReward(Reward reward) { //如果相同用户名 相同标题已经存在 就不能上传 除非删除
        QueryWrapper<Reward> q=new QueryWrapper<>();
        q.eq("username",reward.getUsername());
        q.eq("rewardName",reward.getRewardName());
        if(studentService.selectStudentByUsername(reward.getUsername())==null){
            return false;
        }
        if(rewardMapper.selectOne(q)==null){ //相同用户 相同标题展示不存在
            return rewardMapper.insert(reward)>0;
        }
        else{
            return false;
        }
    }

    @Override
    public boolean deleteReward(Integer id) {
        Reward  reward= rewardMapper.selectById(id);//根据id搜索成果信息
        String username=reward.getUsername();
        String achievementName=reward.getRewardName();
        QueryWrapper<RewardPicture> q=new QueryWrapper<>();
        q.eq("username",username).eq("achievementName",achievementName);
        //一个成果可能对应多张图片  ----删除图片  //找到符合要求的
        List<RewardPicture> lis=rewardPictureMapper.selectList(q);
        if(lis!=null){
            for (RewardPicture it :lis){
                UploadUtil.deleteFile(it.getUrl());///删除oss上面对应的文件
                rewardPictureMapper.deleteById(it.getId());//删除整个对象
            }
        }
        return rewardMapper.deleteById(id)>0;//删除成果
    }

    @Override
    public void deleteBatch(List<Integer> ids) { //根据id批量删除
        for(Integer id:ids){//循环删除
            deleteReward(id);
        }
    }

    @Override  //编辑信息
    public boolean updateReward(Reward reward) {
        return rewardMapper.updateById(reward)>0;
    }

    @Override
    public IPage<Reward> getPage(int currentPage, int pageSize, Reward reward) {
        LambdaQueryWrapper<Reward> q = new LambdaQueryWrapper<>();
        //可以根据什么来查询 username  title  status
        q.eq(Strings.isNotEmpty(reward.getUsername()), Reward::getUsername, reward.getUsername());//student.getUsername()包含于Student::getUsername
        q.like(Strings.isNotEmpty(reward.getRewardName()), Reward::getRewardName, reward.getRewardName());//("title","理论力学")
        q.like(Strings.isNotEmpty(reward.getStatus()), Reward::getStatus, reward.getStatus());//student.getUsername()包含于Student::getUsername

        IPage page = new Page(currentPage, pageSize);
        rewardMapper.selectPage(page, q); //分页查询条件
        return page;
    }

    //获取
    @Override
    public List<Reward> getByids(List<Integer> ids) {
        QueryWrapper<Reward> q=new QueryWrapper<>();
        q.in("id",ids);
        return rewardMapper.selectList(q);
    }

    @Override
    public Boolean deleteAllRewardOfUsername(String username) {
        QueryWrapper<Reward> q=new QueryWrapper<>();
        q.eq("username",username);
        List<Reward> list=rewardMapper.selectList(q);//找到该用户所有的monograph成果
        for (Reward it:list){
            deleteReward(it.getId());
        }
        //找到要删除的username 对应的 monograph成果  之后找到这些成果对应的id 就可以完成 删除该成果的所有信息的功能
        return true;

    }


}
