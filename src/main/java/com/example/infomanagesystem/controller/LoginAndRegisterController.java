package com.example.infomanagesystem.controller;
import com.example.infomanagesystem.entity.Manager;
import com.example.infomanagesystem.entity.Student;
import com.example.infomanagesystem.entity.UserDTO;
import com.example.infomanagesystem.result.R;
import com.example.infomanagesystem.service.ManagerService;
import com.example.infomanagesystem.service.StudentService;
import com.example.infomanagesystem.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
//登录接口
@RestController
@CrossOrigin
public class LoginAndRegisterController {
    @Autowired
    private StudentService studentService;
    @Autowired
    private ManagerService managerService;

    @PostMapping("/login")
    public R login(@RequestBody UserDTO userDTO){
          String role=userDTO.getRole();//获取身份
          String username=userDTO.getUsername();
          String password=userDTO.getPassword();
          if(role.equals("学生")){//是学生
              if(studentService.login(username,password)!=null){//用户存在
                  if(studentService.checkStatus(username)==0){
                      return new R(true,404,"该账号不能使用","登录失败");
                  }
                  //能使用status==1
                  String token=JwtUtils.generateToken(username,role);
                  return new R(true,200,"学生用户存在",token,"登录成功");
              }
              else{
                  return new R(false,404,"学生用户不存在!请检查用户名或者密码是否正确");
              }
          }

          else{ //管理员
              if(managerService.login(username,password)!=null){
                  String token=JwtUtils.generateToken(username,role);
                  return new R(true,200,"管理员用户存在",token,"登录成功");
              }
              else{
                  return new R(false,404,"管理员用户不存在!请检查用户名或者密码是否正确");
              }
          }
    }

    @PostMapping("/register")
    public R register(@RequestBody UserDTO userDTO){
        String role=userDTO.getRole();//获取身份
        String username=userDTO.getUsername();
        String password=userDTO.getPassword();
        String name=userDTO.getName();
        String major=userDTO.getMajor();
        int status=userDTO.getStatus();
        if("学生".equals(role)){
            Student st=new Student();
            st.setUsername(username);
            st.setPassword(password);
            st.setName(name);
            st.setRole(role);
            st.setMajor(major);
            //st.setStatus(status);
            if(studentService.register(st)){ //true
                return new R(true,201,"学生用户添加成功");
            }
            else{ //false
                return new R(false,400,"该用户已存在");
            }
        }
        else{//管理员用户
            Manager ma=new Manager();
            ma.setUsername(username);
            ma.setPassword(password);
            ma.setName(name);
            ma.setRole(role);
            if(managerService.register(ma)){ //true
                return new R(true,201,"管理员用户添加成功");
            }
            else{ //false
                return new R(false,400,"该管理员用户已存在");
            }

        }





    }
}
