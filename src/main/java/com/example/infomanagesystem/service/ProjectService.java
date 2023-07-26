package com.example.infomanagesystem.service;

import com.baomidou.mybatisplus.core.metadata.IPage;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.infomanagesystem.entity.Project;

import java.util.List;

/**
 * @Author xushupeng
 * @Date 2023-07-26 11:08
 */
public interface ProjectService extends IService<Project> {
    List<Project> getAllProject();

    //得到登录者的Project成果信息
    List<Project> getUserProject(String username);

    //编辑的时候 根据成果的id来获取相应成果的填报信息  //用户确定更改 状态为审核
    Project getProjectById(Integer id);

    boolean saveProject(Project project); //Project信息的增加    管理员 学生

    boolean deleteProject(Integer id);//Project信息的删除 注销 (管理员 学生)
    //批量删除
    void deleteBatch(List<Integer> ids); //批量删除 通过传入的一组ids 来删除
    //修改
    boolean updateProject(Project project); //Project信息的修改 (管理员 学生)
    //分页
    IPage<Project> getPage(int currentPage, int pageSize, Project project); //当前所在页 每页多少条(管理员)

    // 批量导出
    List<Project> getByids(List<Integer> ids); //查询id在ids这个数组里面的所有数据



}
