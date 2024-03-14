package com.example.infomanagesystem.controller;
import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import com.example.infomanagesystem.entity.Manager;
import com.example.infomanagesystem.entity.Student;
import com.example.infomanagesystem.entity.UserDTO;
import com.example.infomanagesystem.result.R;
import com.example.infomanagesystem.service.ManagerService;
import com.example.infomanagesystem.service.StudentService;
import com.example.infomanagesystem.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.TimeUnit;

//登录接口
@RestController
@CrossOrigin
public class LoginAndRegisterController {
    @Autowired
    private StudentService studentService;
    @Autowired
    private ManagerService managerService;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    @GetMapping("/info")
    public String  info(){
        System.out.println("用户是否登录"+StpUtil.isLogin());
        System.out.println("用户的角色"+StpUtil.getRoleList());
        return "角色列表";
    }
    @PostMapping("/login")
    public R login(@RequestBody UserDTO userDTO){
        //System.out.println("登录者输入的验证码 is "+userDTO.getVerifyCode().toLowerCase());
        //验证码错误 或者失效
        String UserVerifyCodeKey="verifyCode"+userDTO.getVerifyCode().toLowerCase(); //得到用户的验证码的键
        ValueOperations<String,String> ops=stringRedisTemplate.opsForValue();
        if(ops.get(UserVerifyCodeKey)==null){//验证码错误 或者失效
            return new R(false,404,"验证码错误或失效,请重新尝试","登录失败");
        }
        else{//验证码有效
          String role=userDTO.getRole();//获取身份
          String username=userDTO.getUsername();
          String password=userDTO.getPassword();
          if("学生".equals(role)){// 登陆者身份是学生
              if(studentService.login(username,password)!=null){//用户存在
                  if(studentService.checkStatus(username)==0){
                      return new R(false,404,"该账号不能使用,请联系管理员","登录失败");
                  }
                  //账号能使用  status==1  进行登录  删除验证码
                  stringRedisTemplate.delete(UserVerifyCodeKey);
                  StpUtil.login(username);
                  SaTokenInfo tokenInfo = StpUtil.getTokenInfo();
                  System.out.println("登录成功得到的token 是"+tokenInfo);
                 // String token=JwtUtils.generateToken(username,role);
                  return new R(true,200,"student",tokenInfo);
              }
              else{
                  return new R(false,404,"学生用户不存在!请检查用户名或者密码是否正确");
              }
          }
          else{ //管理员
              if(managerService.login(username,password)!=null){
//                  String token=JwtUtils.generateToken(username,role);
                  stringRedisTemplate.delete(UserVerifyCodeKey);
                  StpUtil.login(username); //登录 开启会话
                  SaTokenInfo tokenInfo = StpUtil.getTokenInfo();
                  System.out.println("登录成功得到的token 是"+tokenInfo);
                return new R(true,200,"admin",tokenInfo);
              }
              else{
                  return new R(false,404,"管理员用户不存在!请检查用户名或者密码是否正确");
              }
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
        String email=userDTO.getEmail();
        String avtar = userDTO.getAvtar();
        String area = userDTO.getArea();
        int status=userDTO.getStatus();
        if("学生".equals(role)){
            Student st=new Student(); //状态默认为1 账号可以使用
            st.setUsername(username);
            st.setPassword(password);
            st.setName(name);
            st.setRole(role);
            st.setMajor(major);
            st.setEmail(email);
            st.setAvtar(avtar);
            st.setArea(area);
            System.out.println(st);
            //st.setStatus(status);
            if(studentService.register(st)){ //true
                return new R(true,201,"学生用户添加成功");
            }
            else{ //false
                return new R(false,400,"该用户已存在");
            }
        }
//        else{//管理员用户
//            Manager ma=new Manager();
//            ma.setUsername(username);
//            ma.setPassword(password);
//            ma.setName(name);
//            ma.setRole(role);
//            if(managerService.register(ma)){ //true
//                return new R(true,201,"管理员用户添加成功");
//            }
//            else{ //false
//                return new R(false,400,"该管理员用户已存在");
//            }
//
//        }
        else{
           return R.error();
        }
    }
}
