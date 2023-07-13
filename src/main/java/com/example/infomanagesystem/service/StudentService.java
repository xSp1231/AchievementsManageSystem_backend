package com.example.infomanagesystem.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.infomanagesystem.entity.Student;

import java.awt.print.Book;
import java.util.List;


public interface StudentService extends IService<Student> {
     Student login(String username, String password); //登录  检测学生用户是否存在
     boolean register(Student student);  //学生注册功能
     int checkStatus(String username);  //  通过用户名检测学生的状态

     //学生信息的增删改查
     boolean saveStudent(Student student); //学生信息的增加 管理员
     boolean deleteStudentByUsername(String username);//学生信息的删除 注销 (管理员 学生)

     boolean updateStudent(Student student); //学生信息的修改 (管理员 学生)

     IPage<Student> getPage(int currentPage, int pageSize,Student student); //当前所在页 每页多少条(管理员)

}
