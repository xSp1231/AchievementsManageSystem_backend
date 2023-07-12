package com.example.infomanagesystem.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("t_manager")
public class Manager {
    private Integer id;
    private String username; //用户名
    private String password; //密码
    private String name;  //姓名
    private String role;  //角色 学生
}
