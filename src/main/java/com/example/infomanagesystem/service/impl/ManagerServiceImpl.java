package com.example.infomanagesystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.infomanagesystem.entity.Manager;
import com.example.infomanagesystem.entity.Student;
import com.example.infomanagesystem.mapper.ManagerMapper;
import com.example.infomanagesystem.service.ManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ManagerServiceImpl extends ServiceImpl<ManagerMapper, Manager> implements ManagerService {
    @Autowired
    private ManagerMapper managerMapper;

    @Override
    public String getRoleByUsername(String username) {
        QueryWrapper<Manager> q=new QueryWrapper<>();
        q.eq("username",username);
        Manager t=managerMapper.selectOne(q);
        if (t!=null){
            return t.getRole();
        }
        else{
            return "";
        }
    }

    //管理员登录
    @Override
    public Manager login(String username, String password) {
        QueryWrapper<Manager> q=new QueryWrapper<>();
        //寻找管理员
        q.eq("username",username).eq("password",password);
        return managerMapper.selectOne(q);
    }

    @Override
    public boolean register(Manager manager) {
        String username=manager.getUsername();
        QueryWrapper<Manager> q=new QueryWrapper<>();
        q.eq("username",username);
        if(managerMapper.selectOne(q)!=null){ //用户存在
            return false;
        }
        //增加操作 //用户不存在
        return managerMapper.insert(manager) > 0;
    }
}
