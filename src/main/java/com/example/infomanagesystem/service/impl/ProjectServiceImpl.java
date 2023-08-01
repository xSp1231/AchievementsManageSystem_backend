package com.example.infomanagesystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.infomanagesystem.entity.Monograph;
import com.example.infomanagesystem.entity.PatentSoft;
import com.example.infomanagesystem.entity.Project;
import com.example.infomanagesystem.mapper.PatentSoftMapper;
import com.example.infomanagesystem.mapper.ProjectMapper;
import com.example.infomanagesystem.service.ProjectService;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author xushupeng
 * @Date 2023-07-26 11:10
 */
@Service
public class ProjectServiceImpl extends ServiceImpl<ProjectMapper, Project> implements ProjectService {
    @Autowired
    private ProjectMapper projectMapper;

    @Override
    public List<Project> getAllProject() {
        return projectMapper.selectList(null);
    }
    //找到登录者的所有专著成果信息
    @Override
    public List<Project> getUserProject( String username) {
        QueryWrapper<Project> q=new QueryWrapper<>();
        q.eq("username",username);
        return projectMapper.selectList(q);
    }

    @Override
    public Project getProjectById(Integer id) {
        return projectMapper.selectById(id);
    }

    @Override
    public boolean saveProject(Project project) { //如果相同用户名 相同项目名已经存在 就不能上传 除非删除
        QueryWrapper<Project> q=new QueryWrapper<>();
        q.eq("username",project.getUsername());
        q.eq("projectName",project.getProjectName());

        if(projectMapper.selectOne(q)==null){ //相同用户 相同标题展示不存在
            return projectMapper.insert(project)>0;
        }
        else{
            return false;
        }
    }

    @Override
    public boolean deleteProject(Integer id) {
        return projectMapper.deleteById(id)>0;
    }

    @Override
    public void deleteBatch(List<Integer> ids) { //根据id批量删除
        QueryWrapper<Project> q=new QueryWrapper<>();
        q.in("id",ids);
        projectMapper.delete(q);
        //  remove(q); //移除满足条件的所有元素 二者等价
    }

    @Override  //编辑信息
    public boolean updateProject(Project project) {
        return projectMapper.updateById(project)>0;
    }

    @Override
    public IPage<Project> getPage(int currentPage, int pageSize, Project project) {
        LambdaQueryWrapper<Project> q = new LambdaQueryWrapper<>();
        //可以根据什么来查询 username  category  status
        q.like(Strings.isNotEmpty(project.getUsername()), Project::getUsername, project.getUsername());//student.getUsername()包含于Student::getUsername
        q.like(Strings.isNotEmpty(project.getCategory()), Project::getCategory, project.getCategory());//("title","理论力学")
        q.like(Strings.isNotEmpty(project.getStatus()), Project::getStatus, project.getStatus());//student.getUsername()包含于Student::getUsername
        q.like(Strings.isNotEmpty(project.getProjectName()), Project::getProjectName, project.getProjectName());//student.getUsername()包含于Student::getUsername

        IPage page = new Page(currentPage, pageSize);
        projectMapper.selectPage(page, q); //分页查询条件
        return page;
    }

    //获取
    @Override
    public List<Project> getByids(List<Integer> ids) {
        QueryWrapper<Project> q=new QueryWrapper<>();
        q.in("id",ids);
        return projectMapper.selectList(q);
    }

    @Override
    public Boolean deleteAllProjectOfUsername(String username) {
        QueryWrapper<Project> q=new QueryWrapper<>();
        q.eq("username",username);
        return projectMapper.delete(q)>0;
    }


}
