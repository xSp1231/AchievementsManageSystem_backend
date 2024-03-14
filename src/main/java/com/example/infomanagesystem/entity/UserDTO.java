package com.example.infomanagesystem.entity;

import lombok.Data;

@Data
public class UserDTO { //通用类 来存储前端传过来的数据
    private String role;
    private String username;
    private String password;
    private String verifyCode;//验证码存放
    private String name;
    private String major;
    private String email;
    private String avtar;
    private String area;
    private int status;
}
