package com.example.infomanagesystem.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.infomanagesystem.entity.Student;


public interface StudentService extends IService<Student> {
     Student login(String username, String password); //登录  检测学生用户是否存在
     boolean register(Student student);  //注册功能

     int checkStatus(String username);  //  通过用户名检测学生的状态
}
