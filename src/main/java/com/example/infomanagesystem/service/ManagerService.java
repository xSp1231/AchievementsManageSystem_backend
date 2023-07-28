package com.example.infomanagesystem.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.infomanagesystem.entity.Manager;
import com.example.infomanagesystem.entity.Student;
import com.example.infomanagesystem.mapper.ManagerMapper;

public interface ManagerService extends IService<Manager> {
    String getRoleByUsername(String username);
    Manager login(String username, String password); //登录  检测管理员是否存在
    boolean register(Manager manager);  //注册功能
}
