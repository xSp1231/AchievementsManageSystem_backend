package com.example.infomanagesystem.saToken;

import cn.dev33.satoken.stp.StpInterface;
import com.example.infomanagesystem.mapper.ManagerMapper;
import com.example.infomanagesystem.mapper.StudentMapper;
import com.example.infomanagesystem.service.ManagerService;
import com.example.infomanagesystem.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author xushupeng
 * @Date 2023-07-26 16:30
 */
@Component    // 保证此类被 SpringBoot 扫描，完成 Sa-Token 的自定义权限验证扩展
public class StpInterfaceImpl implements StpInterface {
    @Autowired
    private StudentService studentService;
    @Autowired
    private ManagerService managerService;
    /**
     * 返回一个账号所拥有的权限码集合
     */
    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        return null;
    }
    /**
     * 返回一个账号所拥有的角色标识集合 (权限与角色可分开校验)
     */
    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        System.out.println("里面的loginId 是 "+String.valueOf(loginId)); //loginId是登陆者的用户名

        List<String> list = new ArrayList<String>(); //角色表
        System.out.println("登陆者的角色是"+managerService.getRoleByUsername(String.valueOf(loginId)));
        if("student".equals(managerService.getRoleByUsername(String.valueOf(loginId)))){ //如果角色是学生 在管理员的表里面没有找到的话 =>为学生
           list.add("student");
        }
        else{
            list.add("admin");
        }
        return list;
    }

}