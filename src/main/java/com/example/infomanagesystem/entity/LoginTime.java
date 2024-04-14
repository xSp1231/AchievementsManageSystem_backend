package com.example.infomanagesystem.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("t_login_time")
public class LoginTime {
    private Integer id;
    private String username;
    private String loginTime;
    private String num;
}
