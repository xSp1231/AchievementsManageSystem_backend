package com.example.infomanagesystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.infomanagesystem.entity.Monograph;
import com.example.infomanagesystem.entity.PatentSoft;
import com.example.infomanagesystem.entity.PictureEntity.MonographPicture;
import com.example.infomanagesystem.entity.PictureEntity.ProjectPicture;
import com.example.infomanagesystem.entity.Project;
import com.example.infomanagesystem.mapper.PatentSoftMapper;
import com.example.infomanagesystem.mapper.PictureMapper.ProjectPictureMapper;
import com.example.infomanagesystem.mapper.ProjectMapper;
import com.example.infomanagesystem.service.ProjectService;
import com.example.infomanagesystem.service.StudentService;
import com.example.infomanagesystem.utils.UploadUtil;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
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
    @Autowired
    private StudentService studentService;
    @Autowired
    private ProjectPictureMapper projectPictureMapper;
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
    @Cacheable(cacheNames = "Project",key = "#id")  //编辑的时候 查询用户成果信息
    public Project getProjectById(Integer id) {
        return projectMapper.selectById(id);
    }

    @Override
    @CacheEvict(cacheNames = "ProjectPage", allEntries = true)  //新增的时候 删除SCpage缓存目录下面的所有缓存
    public boolean saveProject(Project project) { //如果相同用户名 相同项目名已经存在 就不能上传 除非删除
        QueryWrapper<Project> q=new QueryWrapper<>();
        q.eq("username",project.getUsername());
        q.eq("projectName",project.getProjectName());
        if(studentService.selectStudentByUsername(project.getUsername())==null){
            return false;
        }
        if(projectMapper.selectOne(q)==null){ //相同用户 相同标题展示不存在
            return projectMapper.insert(project)>0;
        }
        else{
            return false;
        }
    }

    @Override
    @Caching(evict = {
            @CacheEvict(cacheNames = "Project", key = "#id"), //执行删除用户操作时
            @CacheEvict(cacheNames = "ProjectPage", allEntries = true)  //删除getPage缓存目录下面的所有缓存
    })
    public boolean deleteProject(Integer id) {
        Project  project= projectMapper.selectById(id);//根据id搜索成果信息
        //根据成果的username monoName(成果名字) 删除对应的成果信息
        String username=project.getUsername();
        String achievementName=project.getProjectName();
        QueryWrapper<ProjectPicture> q=new QueryWrapper<>();
        q.eq("username",username).eq("achievementName",achievementName);
        //一个成果可能对应多张图片  ----删除图片  //找到符合要求的
        List<ProjectPicture> lis=projectPictureMapper.selectList(q);
        if(lis!=null){
            for (ProjectPicture it :lis){
                UploadUtil.deleteFile(it.getUrl());///删除oss上面对应的文件
                projectPictureMapper.deleteById(it.getId());//删除整个对象
            }
        }
        return projectMapper.deleteById(id)>0;//删除成果
    }

    @Override
    @CacheEvict(cacheNames = "ProjectPage", allEntries = true)  //删除getPage缓存目录下面的所有缓存
    public void deleteBatch(List<Integer> ids) { //根据id批量删除
        for(Integer id:ids){//循环删除
            deleteProject(id);
        }
        //  remove(q); //移除满足条件的所有元素 二者等价
    }

    @Override  //编辑信息
    @Caching(evict = {
            @CacheEvict(cacheNames = "Project", key = "#project.id"), //执行删除用户操作时
            @CacheEvict(cacheNames = "ProjectPage", allEntries = true)  //删除getPage缓存目录下面的所有缓存
    })
    public boolean updateProject(Project project) {
        return projectMapper.updateById(project)>0;
    }

    @Override
    @Cacheable(cacheNames = "ProjectPage",key = "#currentPage+'_'+#pageSize+'_'+#project.username+'_'+#project.category+'_'+#project.status+'_'+#project.projectName")
    public IPage<Project> getPage(int currentPage, int pageSize, Project project) {
        LambdaQueryWrapper<Project> q = new LambdaQueryWrapper<>();
        //可以根据什么来查询 username  category  status
        q.eq(Strings.isNotEmpty(project.getUsername()), Project::getUsername, project.getUsername());//student.getUsername()包含于Student::getUsername
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
        List<Project> list=projectMapper.selectList(q);//找到该用户所有的monograph成果
        for (Project it:list){
            deleteProject(it.getId());
        }
        //找到要删除的username 对应的 monograph成果  之后找到这些成果对应的id 就可以完成 删除该成果的所有信息的功能
        return true;
    }


}
