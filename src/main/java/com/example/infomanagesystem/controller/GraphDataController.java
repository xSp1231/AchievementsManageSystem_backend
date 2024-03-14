package com.example.infomanagesystem.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.infomanagesystem.entity.*;
import com.example.infomanagesystem.mapper.*;
import com.example.infomanagesystem.result.R;
import com.example.infomanagesystem.utils.LoginNumRecord;
import org.apache.commons.collections.list.AbstractLinkedList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author xushupeng
 * @Date 2023-08-07 15:31
 */

@RestController
@CrossOrigin
public class GraphDataController {
    @Autowired
    private ScientificPaperMapper scientificPaperMapper;
    @Autowired
    private MonographMapper monographMapper;
    @Autowired
    private PatentSoftMapper patentSoftMapper;
    @Autowired
    private ProjectMapper projectMapper;
    @Autowired
    private RewardMapper rewardMapper;

    @SaCheckLogin
    @GetMapping("/totalPieData")
    public R getTotalData() {
        // 构造饼图数据
        List<Map<String, Object>> data = new ArrayList<>();
        data.add(createDataItem("科技论文", scientificPaperMapper.selectList(null).size()));
        data.add(createDataItem("专著", monographMapper.selectList(null).size()));
        data.add(createDataItem("专利软著", patentSoftMapper.selectList(null).size()));
        data.add(createDataItem("项目", projectMapper.selectList(null).size()));
        data.add(createDataItem("奖励", rewardMapper.selectList(null).size()));
        System.out.println("data is "+data);
        return new R(true,200,"总体饼图数据",data);
    }
    @SaCheckLogin
    @GetMapping("/personalPieData")
    public R getPersonalData() {
        String username= (String) StpUtil.getLoginId();
        QueryWrapper<ScientificPaper> q1= new QueryWrapper<>();
        q1.eq("username",username);
        QueryWrapper<Monograph> q2= new QueryWrapper<>();
        q2.eq("username",username);
        QueryWrapper<PatentSoft> q3= new QueryWrapper<>();
        q3.eq("username",username);
        QueryWrapper<Project> q4= new QueryWrapper<>();
        q4.eq("username",username);
        QueryWrapper<Reward> q5= new QueryWrapper<>();
        q5.eq("username",username);
        // 构造饼图数据
        List<Map<String, Object>> data = new ArrayList<>();
        data.add(createDataItem("科技论文", scientificPaperMapper.selectList(q1).size()));
        data.add(createDataItem("专著", monographMapper.selectList(q2).size()));
        data.add(createDataItem("专利软著", patentSoftMapper.selectList(q3).size()));
        data.add(createDataItem("项目", projectMapper.selectList(q4).size()));
        data.add(createDataItem("奖励", rewardMapper.selectList(q5).size()));
        System.out.println("personalData is "+data);
        return new R(true,200,"用户饼图数据",data);
    }

    private Map<String, Object> createDataItem(String name, int value) {
        Map<String, Object> item = new HashMap<>();
        item.put("name", name);
        item.put("value", value);
        return item;
    }

    @Autowired
    private StudentMapper studentMapper;
    @Autowired
    private ManagerMapper managerMapper;
    @SaCheckLogin
    @GetMapping("/countNum")
    public R countNum(){
        int studentNum=studentMapper.getStudentNum();
        int managerNum=managerMapper.getManagerNum();
        int monographNum= monographMapper.getMonographNum();
        int rewardNum= rewardMapper.getRewardNum();
        int projectNum= projectMapper.getProjectNum();
        int scientificNum=scientificPaperMapper.getScientificNum();
        int patentSoftNum=patentSoftMapper.getPatentsoftNum();
        System.out.println("学生用户数量"+studentNum);
        System.out.println("管理员数量"+managerNum);
        System.out.println("成果数量"+(monographNum+rewardNum+projectNum+scientificNum+patentSoftNum));
        List<Integer> data=new ArrayList<>();
        data.add(studentNum);
        data.add(managerNum);
        data.add((monographNum+rewardNum+projectNum+scientificNum+patentSoftNum));
        return new R(true,200,"统计面板数据",data);
    }

    @Autowired
    private LoginNumRecord loginNumRecord;

    @SaCheckLogin
    @GetMapping("/addLoginNum") //获取数量
    public R addLoginNum(){
        loginNumRecord.addNum();
        return new R(true,200,"今日登录次数+1");
    }

    @GetMapping("/getdata")
    public R getData(){
        return R.success();
    }
    @GetMapping("/LoginCountStatistics") //获取数量
    public R getPastSevenDaysLoginNum(){
        return new R(true,200,"获取近七天每日登录人数成功",loginNumRecord.getPastSevenDays());
    }

}
