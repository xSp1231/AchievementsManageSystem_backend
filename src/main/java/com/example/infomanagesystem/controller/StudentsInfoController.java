package com.example.infomanagesystem.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckRole;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.read.metadata.ReadSheet;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.example.infomanagesystem.entity.Student;
import com.example.infomanagesystem.result.R;
import com.example.infomanagesystem.service.StudentService;
import com.example.infomanagesystem.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

/**
 * @Author xushupeng
 * @Date 2023-07-12 21:00
 */

@RestController
@CrossOrigin  //跨域
public class StudentsInfoController {
    @Autowired
    private StudentService studentService;
    @SaCheckLogin
    @SaCheckRole("admin")
    @GetMapping("/getAll") //得到所有学生信息
    public R getAll(){
        List<Student> studentList=studentService.getAll();
        return new R(true ,200,"所有学生",studentList);
    }
    @SaCheckLogin
    @GetMapping("student/{currentPage}/{pageSize}") //http://localhost:8080/2/3
    public R getPage(@PathVariable int currentPage,@PathVariable int pageSize, Student student){
        System.out.println("分页查询中的student is"+student);
      return new R(true,200,"分页信息",studentService.getPage(currentPage,pageSize,student));
    }

    @SaCheckLogin   // 用户页面  编辑个人信息的时候 要先找到信息使用
    @GetMapping("/selectStudentByUsername/{username}")
    public R selectStudentByUsername(@PathVariable String username){
        System.out.println("按照姓名搜索用户的username is "+username);
        return new R(true,200,"用户已找到",studentService.selectStudentByUsername(username));
    }


    @SaCheckLogin
    @GetMapping("/getUserInfo")   //个人中心页面 学生修改个人信息的时候 先要获取该学生的信息 前端携带token
    public R getUserInfo(HttpServletRequest request){
        // 获取 Authorization 头部的值
        String token = request.getHeader("Authorization").substring(7);
        if(JwtUtils.validateToken(token)){
            System.out.println("jwt正确");
            String username=JwtUtils.getUsernameFromToken(token);
            String role=JwtUtils.getRoleFromToken(token);
            System.out.println(" jwt  username "+username);
            System.out.println(" jwt  role "+role);
            return new R(true,200,"获得登录者信息",studentService.selectStudentByUsername(username));
        }
        else{
            System.out.println("jwt过期或错误");
            return new R(false,400,"jwt过期或错误");
        }
    }
    @SaCheckLogin
    @SaCheckRole("admin")
    @PostMapping("/student") //管理员添加学生
    public R addStudent(@RequestBody Student student){
        if(studentService.saveStudent(student)){ //前端传过来的数据必须全面
            return new R(true,201,"学生用户创建成功");
        }
        else{
            return new R(false,409,"该学生用户已存在,用户名唯一");//409 Conflict：表示请求与服务器上现有的资源冲突，例如请求创建一个已经存在的资源。
        }
    }

    @SaCheckLogin
    @DeleteMapping("/student/{username}") //个人中心页面学生注销  用户页面管理员通过用户名删除 //http://localhost:8080/student/test6
    public R deleteStudent(@PathVariable String username){ // @RequestParam 是针对于前端param进行传参
        System.out.println("username is "+username);       // @PathVarible  是针对于在url上面进行传参  url拼接
       if(studentService.deleteStudentByUsername(username)){ //删除成功的情况下，通常会返回状态码 204 No Content。这表示请求已成功完成
           return new R(true,204,"学生"+username+"删除成功!");
       }
       else{
           return  new R(false,404,"学生"+username+"删除失败!");
       }
    }


    @SaCheckLogin
    @SaCheckRole("admin")
    //批量删除学生信息
    @PostMapping("/deleteUsersByUsernames")
    public R deleteUsersByUsernames(@RequestBody List<String> usernames){
        System.out.println("前端传过来的usernames is "+usernames);
        studentService.deleteUsers(usernames);
        return new R(true,204,"批量删除成功");
    }
    //管理员用户管理页面修改学生信息
    @SaCheckLogin
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
    @SaCheckLogin
    @PostMapping("/editStudentInfo") //在个人页面修改学生信息
    public R edidStudentInfo(@RequestBody Student student){
        if(studentService.editStudent(student)){
            return new R(true,"修改成功");
        }
        else{
            return new R(true,"修改成功");
        }

    }

    //excel文件导入接口 基于easyExcel

    @PostMapping("/importStudentInfo")
    public R importData(@RequestParam("file") MultipartFile file) throws IOException {
        // 使用EasyExcel读取Excel文件 //输入流读取数据
        ExcelReader excelReader = EasyExcel.read(file.getInputStream(), Student.class, new StudentListener()).build();
        ReadSheet readSheet = EasyExcel.readSheet(0).build();
        excelReader.read(readSheet);
        excelReader.finish();
        return new R(true,200,"数据导入成功(用户名重复数据将自动排除)");
    }
    //监听器
    private class StudentListener extends AnalysisEventListener<Student> {
        @Override
        public void invoke(Student student, AnalysisContext analysisContext) {
            // 读取到一条数据时的回调 //子类继承父类并重写父类中的方法时，返回类型必须是父类方法返回类型的子类型或相同类型
            System.out.println("读取一行"+student);
            boolean add=studentService.saveStudent(student); //插入数据库  如果存在 就不添加
        }
        @Override
        public void doAfterAllAnalysed(AnalysisContext analysisContext) {
            // 读取完成时的回调
            System.out.println("excel文件读取完成");
        }
    }



    @GetMapping("/exportAll")
    public void exportData(HttpServletResponse response) throws IOException {

        // 设置响应头
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        response.setHeader("Content-Disposition", "attachment;filename=StudentInfo.xlsx");
        // 查询数据库中的数据
        List<Student> userList = studentService.getAll(); //获取所有学生
        //创建 ExcelWriter 对象  输出流 // 输出流 向文件中写入
        OutputStream out = response.getOutputStream();
        ExcelWriter excelWriter = EasyExcel.write(out, Student.class).build();

        // 创建 Sheet 对象
        WriteSheet writeSheet = EasyExcel.writerSheet("Sheet0").build();

        // 写入数据
        excelWriter.write(userList, writeSheet);
        // 关闭 ExcelWriter 对象
        excelWriter.finish();
    }

    //根据用户名 实现批量导出
    @SaCheckLogin
    @SaCheckRole("admin")
    @PostMapping("/exportByUsername")
    public void exportAll(@RequestBody List<String> usernames ,HttpServletResponse response) throws IOException { //apifox 直接json传递一个数组 ["username1","username2","username3"]
        List<Student> studentList =studentService.getByUsernames(usernames);
        System.out.println("接受到的列表数据"+studentList);
        // 导出数据到 Excel 文件并写入响应体
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment;filename=StudentInfo.xlsx");
        OutputStream outputStream = response.getOutputStream();
        ExcelWriter excelWriter = EasyExcel.write(outputStream, Student.class).build();
        WriteSheet writeSheet = EasyExcel.writerSheet("Sheet0").build();
        excelWriter.write(studentList, writeSheet);
        excelWriter.finish();
        outputStream.flush();
    }

}
