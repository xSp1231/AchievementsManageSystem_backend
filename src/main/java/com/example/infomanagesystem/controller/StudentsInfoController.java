package com.example.infomanagesystem.controller;

import com.example.infomanagesystem.entity.Student;
import com.example.infomanagesystem.result.R;
import com.example.infomanagesystem.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author xushupeng
 * @Date 2023-07-12 21:00
 */

@RestController
@CrossOrigin  //跨域
public class StudentsInfoController {

    @Autowired
    private StudentService studentService;

    @GetMapping("/{currentPage}/{pageSize}") //http://localhost:8080/2/3
    public R getPage(@PathVariable int currentPage,@PathVariable int pageSize,@RequestBody Student student){
        System.out.println("分页查询中的student is"+student);
      return new R(true,200,"分页信息",studentService.getPage(currentPage,pageSize,student));
    }

    @PostMapping("/student") //管理员添加学生
    public R addStudent(@RequestBody Student student){
        if(studentService.saveStudent(student)){
            return new R(true,201,"学生用户创建成功");
        }
        else{
            return new R(false,409,"该学生用户已存在,用户名唯一");//409 Conflict：表示请求与服务器上现有的资源冲突，例如请求创建一个已经存在的资源。
        }
    }
    @DeleteMapping("/student/{username}") //管理员通过用户名删除 //http://localhost:8080/student/test6
    public R deleteStudent(@PathVariable String username){ // @RequestParam 是针对于前端param进行传参
        System.out.println("username is "+username);       // @PathVarible  是针对于在url上面进行传参  url拼接
       if(studentService.deleteStudentByUsername(username)){ //删除成功的情况下，通常会返回状态码 204 No Content。这表示请求已成功完成
           return new R(true,204,"学生"+username+"删除成功!");
       }
       else{
           return  new R(false,404,"学生"+username+"删除失败!");
       }
    }
    //修改学生信息
    @PutMapping("/student")
    public R updateStudent(@RequestBody Student student){

        //用户名 角色 不能修改
        if(studentService.updateStudent(student)){
            return new R(true,200,"修改学生信息成功");
        }
        else{
            return new R(false,403,"修改学生信息失败");
        }
    }

}
