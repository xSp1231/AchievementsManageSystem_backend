package com.example.infomanagesystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.example.infomanagesystem.entity.Monograph;
import com.example.infomanagesystem.entity.PictureEntity.ScientificPaperPicture;
import com.example.infomanagesystem.entity.ScientificPaper;
import com.example.infomanagesystem.entity.Student;
import com.example.infomanagesystem.mapper.PictureMapper.ScientificPaperPictureMapper;
import com.example.infomanagesystem.mapper.ScientificPaperMapper;
import com.example.infomanagesystem.service.ScientificPaperService;
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
 * @Date 2023-07-24 14:39
 * 编辑功能增加缓存 删除功能删除缓存
 *
 */

@Service
public class ScientificPaperImpl extends ServiceImpl<ScientificPaperMapper,ScientificPaper> implements ScientificPaperService  {
    @Autowired
    private ScientificPaperMapper scientificPaperMapper;
    @Autowired
    private StudentService studentService;
    @Autowired
    private ScientificPaperPictureMapper scientificPaperPictureMapper;
    @Override
    public List<ScientificPaper> getAllScientificPaper() {
       return scientificPaperMapper.selectList(null);
   }

    //找到登录者的所有科技论文成果信息
    @Override
    public List<ScientificPaper> getUserScientificPaper( String username) {
       QueryWrapper<ScientificPaper> q=new QueryWrapper<>();
       q.eq("username",username);
       return scientificPaperMapper.selectList(q);
    }

    @Override
    @Cacheable(cacheNames = "ScientificPaper",key = "#id")  //编辑的时候 查询用户成果信息
    public ScientificPaper getScientificPaperById(Integer id) {
        return scientificPaperMapper.selectById(id);
    }

    @Override
    @CacheEvict(cacheNames = "SCPage", allEntries = true)  //新增的时候 删除SCpage缓存目录下面的所有缓存
    public boolean saveScientificPaper(ScientificPaper scientificPaper) { //如果相同用户名 相同标题已经存在 就不能上传 除非删除
       QueryWrapper<ScientificPaper> q=new QueryWrapper<>();
       q.eq("username",scientificPaper.getUsername());
       q.eq("title",scientificPaper.getTitle());
        if(studentService.selectStudentByUsername(scientificPaper.getUsername())==null){
            return false;
        }
       if(scientificPaperMapper.selectOne(q)==null){ //相同用户 相同标题展示不存在
           return scientificPaperMapper.insert(scientificPaper)>0;
       }
       else{
           return false;
       }
    }

    @Override
    @Caching(evict = {
            @CacheEvict(cacheNames = "ScientificPaper", key = "#id"), //执行删除用户操作时
            @CacheEvict(cacheNames = "SCPage", allEntries = true)  //删除getPage缓存目录下面的所有缓存
    })
    public boolean deleteScientificPaper(Integer id) { //根据id删除单个成果
        ScientificPaper scientificPaper= scientificPaperMapper.selectById(id);//根据id搜索成果信息
        String username=scientificPaper.getUsername();
        String achievementName=scientificPaper.getTitle();
        QueryWrapper<ScientificPaperPicture> q=new QueryWrapper<>();
        q.eq("username",username).eq("achievementName",achievementName); //删除成果的时候 也要删除相关成果文件
        //一个成果可能对应多张图片  ----删除图片  //找到符合要求的
        List<ScientificPaperPicture> lis=scientificPaperPictureMapper.selectList(q);
        if(lis!=null){
            for (ScientificPaperPicture it :lis){
                UploadUtil.deleteFile(it.getUrl());///删除oss上面对应的文件
                scientificPaperPictureMapper.deleteById(it.getId());//删除整个对象
            }
        }
        return scientificPaperMapper.deleteById(id)>0;//删除成果
    }

    @Override
    @CacheEvict(cacheNames = "SCPage", allEntries = true)  //删除getPage缓存目录下面的所有缓存
    public void deleteBatch(List<Integer> ids) { //根据id批量删除
        for(Integer id:ids){//循环删除
            deleteScientificPaper(id);
        }
    }

    @Override  //编辑信息
    @Caching(evict = {
            @CacheEvict(cacheNames = "ScientificPaper", key = "#scientificPaper.id"), //执行删除用户操作时
            @CacheEvict(cacheNames = "SCPage", allEntries = true)  //删除getPage缓存目录下面的所有缓存
    })
    public boolean updateScientificPaper(ScientificPaper scientificPaper) {
        return scientificPaperMapper.updateById(scientificPaper)>0;
    }

    @Override
    @Cacheable(cacheNames = "SCPage",key = "#currentPage+'_'+#pageSize+'_'+#scientificPaper.username+'_'+#scientificPaper.title+'_'+#scientificPaper.status")
    public IPage<ScientificPaper> getPage(int currentPage, int pageSize, ScientificPaper scientificPaper) {
        System.out.println("分页缓存");
        LambdaQueryWrapper<ScientificPaper> q = new LambdaQueryWrapper<>();
        //可以根据什么来查询 username  title  status
        q.eq(Strings.isNotEmpty(scientificPaper.getUsername()), ScientificPaper::getUsername, scientificPaper.getUsername());//student.getUsername()包含于Student::getUsername
        q.like(Strings.isNotEmpty(scientificPaper.getTitle()), ScientificPaper::getTitle, scientificPaper.getTitle());//("title","理论力学")
        q.like(Strings.isNotEmpty(scientificPaper.getStatus()), ScientificPaper::getStatus, scientificPaper.getStatus());//student.getUsername()包含于Student::getUsername

        IPage page = new Page(currentPage, pageSize);
        scientificPaperMapper.selectPage(page, q); //分页查询条件
        return page;
    }

    //获取
    @Override
    public List<ScientificPaper> getByids(List<Integer> ids) {
        QueryWrapper<ScientificPaper> q=new QueryWrapper<>();
        q.in("id",ids);
        return scientificPaperMapper.selectList(q);
    }

    @Override
    public Boolean deleteAllScientificPaperOfUsername(String username) {
        QueryWrapper<ScientificPaper> q=new QueryWrapper<>();
        q.eq("username",username);
        List<ScientificPaper> list=scientificPaperMapper.selectList(q);//找到该用户所有的monograph成果
        for (ScientificPaper it:list){
            deleteScientificPaper(it.getId());
        }
        //找到要删除的username 对应的 monograph成果  之后找到这些成果对应的id 就可以完成 删除该成果的所有信息的功能
        return true;
    }
}
