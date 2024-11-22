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

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    @GetMapping("/personalStatusPieData")
    public R getPersonalData() {
        String refuse = "拒绝";
        String accept = "接收";
        String audit  = "审核";
        String username= (String) StpUtil.getLoginId();
        // 构造饼图数据
        List<Map<String, Object>> data = new ArrayList<>();
        System.out.println("personalData is "+data);
        data.add(createDataItem("拒绝的成果", rewardMapper.getStatus(refuse,username)+scientificPaperMapper.getStatus(refuse,username)+monographMapper.getStatus(refuse,username)+patentSoftMapper.getStatus(refuse,username)+projectMapper.getStatus(refuse,username)));
        data.add(createDataItem("接受的成果", rewardMapper.getStatus(accept,username)+scientificPaperMapper.getStatus(accept,username)+monographMapper.getStatus(accept,username)+patentSoftMapper.getStatus(accept,username)+projectMapper.getStatus(accept,username)));;
        data.add(createDataItem("审核的成果", rewardMapper.getStatus(audit,username)+scientificPaperMapper.getStatus(audit,username)+monographMapper.getStatus(audit,username)+patentSoftMapper.getStatus(audit,username)+projectMapper.getStatus(audit,username)));;
        return new R(true,200,"用户饼图数据",data);
    }

    @SaCheckLogin
    @GetMapping("/personalPieData")
    public R getPersonalStatusData() {
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
    private Map<String, Object> createDataItem(String name, long value) {
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

    //总体可视化相关接口
    //1 获取各个地区人数分布数据 {name: "北京",value: 20,},
    @SaCheckLogin
    @GetMapping("/getAreaNum")
    public R getAreaNum(){
        List<Student> list=studentMapper.selectList(null);
        Map<String, Long> areaCountMap = list.stream().collect(Collectors.groupingBy(Student::getArea, Collectors.counting()));
        List<Map<String, Object>> data = new ArrayList<>();
        for(String it:areaCountMap.keySet()){
           data.add(createDataItem(it,areaCountMap.get(it)));
        }
        return new R(true,200,"地区数据获取成功",data);
    }

    //2 获取总体成果状态分布数据
//    @SaCheckLogin
    @GetMapping("/totalStatusPieData")
    public R getTotalPieData() {
        String refuse = "拒绝";
        String accept = "接收";
        String audit  = "审核";
        // 构造饼图数据
        List<Map<String, Object>> data = new ArrayList<>();
        System.out.println("personalData is "+data);
        data.add(createDataItem("拒绝的成果", rewardMapper.getAllStatus(refuse)+scientificPaperMapper.getAllStatus(refuse)+monographMapper.getAllStatus(refuse)+patentSoftMapper.getAllStatus(refuse)+projectMapper.getAllStatus(refuse)));
        data.add(createDataItem("接受的成果", rewardMapper.getAllStatus(accept)+scientificPaperMapper.getAllStatus(accept)+monographMapper.getAllStatus(accept)+patentSoftMapper.getAllStatus(accept)+projectMapper.getAllStatus(accept)));;
        data.add(createDataItem("审核的成果", rewardMapper.getAllStatus(audit)+scientificPaperMapper.getAllStatus(audit)+monographMapper.getAllStatus(audit)+patentSoftMapper.getAllStatus(audit)+projectMapper.getAllStatus(audit)));;
        return new R(true,200,"总体饼图数据",data);
    }

    @GetMapping("/gradesNumData")
    public R getGradesNum(){
        int grade1=0;
        int grade2=0;
        int grade3=0;
        int grade4=0;
        int gradex=0;//其他 填写班级错误的人
        List<Student> list=studentMapper.selectList(null);
        for(Student student:list){//土木2101
            if(student.getMajor().length()<6){
                continue;
            }
            LocalDate currentDate = LocalDate.now();
            int year = currentDate.getYear();
            String t=student.getMajor();
            String grade=t.substring(t.length()-4);//2101
            try {
                int studentGrade=year%100-Integer.parseInt(grade.substring(0,2));
                if(studentGrade==1){
                    grade1++;
                }
                if(studentGrade==2){
                    grade2++;
                }
                if(studentGrade==3){
                    grade3++;
                }
                if(studentGrade==4){
                    grade4++;
                }
            } catch (NumberFormatException e) {
                System.out.println("该专业名称无法转换为数字");
            }
        }

        gradex=list.size()-(grade1+grade2+grade3+grade4);
        List<Integer>gradesList=new ArrayList<>();
        gradesList.add(grade1);
        gradesList.add(grade2);
        gradesList.add(grade3);
        gradesList.add(grade4);
        gradesList.add(gradex);
        return new R(true,200,"各个年级人数分布数据",gradesList);
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
