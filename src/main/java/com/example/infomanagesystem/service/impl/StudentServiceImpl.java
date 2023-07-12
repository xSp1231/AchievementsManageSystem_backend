package com.example.infomanagesystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.infomanagesystem.entity.Manager;
import com.example.infomanagesystem.entity.Student;
import com.example.infomanagesystem.mapper.StudentMapper;
import com.example.infomanagesystem.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

//学生登录 注册

@Service
public class StudentServiceImpl extends ServiceImpl<StudentMapper, Student> implements StudentService {
    @Autowired
    private StudentMapper studentMapper;
    @Override
    public Student login(String username ,String password) {
            QueryWrapper<Student> q=new QueryWrapper<>();
            //寻找用户
            q.eq("username",username).eq("password",password);

        return studentMapper.selectOne(q);
    }
    @Override
    public boolean register(Student student) { //注册成功  同时要判断用户是否已经存在
        String username=student.getUsername();
        QueryWrapper<Student> q=new QueryWrapper<>();
        q.eq("username",username);
        if(studentMapper.selectOne(q)!=null){ //用户存在
            return false;
        }
        //增加操作 //用户不存在
        return studentMapper.insert(student) > 0;
    }

    @Override
    public int checkStatus(String username) {
        QueryWrapper<Student> q=new QueryWrapper<>();
        q.eq("username",username);
        return studentMapper.selectOne(q).getStatus();//返回状态码
    }

}
