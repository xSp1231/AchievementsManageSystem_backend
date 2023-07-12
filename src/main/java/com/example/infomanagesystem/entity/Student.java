package com.example.infomanagesystem.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.springframework.boot.context.properties.bind.DefaultValue;

//学生实体类
@Data
@TableName("t_student") //绑定数据库的表名
public class Student {
private Integer id;
private String username; //用户名
private String password; //密码
private String name;  //姓名
private String major; //专业 计科2009班
private String role;  //角色 学生
private int status=1;   //账号状态 1 可用 0 不可用
}
