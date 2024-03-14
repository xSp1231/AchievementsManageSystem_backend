package com.example.infomanagesystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.infomanagesystem.entity.Monograph;
import com.example.infomanagesystem.entity.PatentSoft;
import com.example.infomanagesystem.entity.PictureEntity.MonographPicture;
import com.example.infomanagesystem.entity.PictureEntity.PatentSoftPicture;
import com.example.infomanagesystem.mapper.PatentSoftMapper;
import com.example.infomanagesystem.mapper.PictureMapper.PatentSoftPictureMapper;
import com.example.infomanagesystem.service.PatentSoftService;
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
 * @Date 2023-07-25 17:47
 */
@Service
public class PatentSoftServiceImpl extends ServiceImpl<PatentSoftMapper, PatentSoft>  implements PatentSoftService {
    @Autowired
    private PatentSoftMapper patentSoftMapper;
    @Autowired
    private StudentService studentService;
    @Autowired
    private PatentSoftPictureMapper patentSoftPictureMapper;
    @Override
    public List<PatentSoft> getAllPatentSoft(){
        return patentSoftMapper.selectList(null);
    }
    //找到登录者的所有专著成果信息
    @Override
    public List<PatentSoft> getUserPatentSoft( String username) {
        QueryWrapper<PatentSoft> q=new QueryWrapper<>();
        q.eq("username",username);
        return patentSoftMapper.selectList(q);
    }

    @Override
    @Cacheable(cacheNames = "PatentSoft",key = "#id")  //编辑的时候 查询用户成果信息
    public PatentSoft getPatentSoftById(Integer id) {
        return patentSoftMapper.selectById(id);
    }

    @Override
    @CacheEvict(cacheNames = "PSPage", allEntries = true)  //新增的时候 删除SCpage缓存目录下面的所有缓存
    public boolean savePatentSoft(PatentSoft patentSoft) { //如果相同用户名 相同标题已经存在 就不能上传 除非删除
        QueryWrapper<PatentSoft> q=new QueryWrapper<>();
        q.eq("username",patentSoft.getUsername());
        q.eq("name",patentSoft.getName());
        if(studentService.selectStudentByUsername(patentSoft.getUsername())==null){
            return false;
        }
        if(patentSoftMapper.selectOne(q)==null){ //相同用户 相同标题展示不存在
            return patentSoftMapper.insert(patentSoft)>0;
        }
        else{
            return false;
        }
    }

    @Override
    @Caching(evict = {
            @CacheEvict(cacheNames = "PatentSoft", key = "#id"), //执行删除用户操作时
            @CacheEvict(cacheNames = "PSPage", allEntries = true)  //删除getPage缓存目录下面的所有缓存
    })
    public boolean deletePatentSoft(Integer id) {
        PatentSoft  patentSoft= patentSoftMapper.selectById(id);//根据id搜索成果信息
        //根据成果的username monoName(成果名字) 删除对应的成果信息
        String username=patentSoft.getUsername();
        String achievementName=patentSoft.getName();
        QueryWrapper<PatentSoftPicture> q=new QueryWrapper<>();
        q.eq("username",username).eq("achievementName",achievementName);
        //一个成果可能对应多张图片  ----删除图片  //找到符合要求的
        List<PatentSoftPicture> lis=patentSoftPictureMapper.selectList(q);
        if(lis!=null){
            for (PatentSoftPicture it :lis){
                UploadUtil.deleteFile(it.getUrl());///删除oss上面对应的文件
                patentSoftPictureMapper.deleteById(it.getId());//删除整个对象
            }
        }
        return patentSoftMapper.deleteById(id)>0;//删除成果
    }

    @Override
    @CacheEvict(cacheNames = "PSPage", allEntries = true)  //删除getPage缓存目录下面的所有缓存
    public void deleteBatch(List<Integer> ids) { //根据id批量删除
        for(Integer id:ids){//循环删除
            deletePatentSoft(id);
        }
    }

    @Override  //编辑信息
    @Caching(evict = {
            @CacheEvict(cacheNames = "PatentSoft", key = "#patentSoft.id"), //执行删除用户操作时
            @CacheEvict(cacheNames = "PSPage", allEntries = true)  //删除getPage缓存目录下面的所有缓存
    })
    public boolean updatePatentSoft(PatentSoft patentSoft) {
        return patentSoftMapper.updateById(patentSoft)>0;
    }

    @Override
    @Cacheable(cacheNames = "PSPage",key = "#currentPage+'_'+#pageSize+'_'+#patentSoft.username+'_'+#patentSoft.name+'_'+#patentSoft.status")
    public IPage<PatentSoft> getPage(int currentPage, int pageSize, PatentSoft patentSoft) {
        LambdaQueryWrapper<PatentSoft> q = new LambdaQueryWrapper<>();
        //可以根据什么来查询 username  name  status
        q.eq(Strings.isNotEmpty(patentSoft.getUsername()), PatentSoft::getUsername, patentSoft.getUsername());//student.getUsername()包含于Student::getUsername
        q.like(Strings.isNotEmpty(patentSoft.getName()), PatentSoft::getName, patentSoft.getName());//("name","理论力学")
        q.like(Strings.isNotEmpty(patentSoft.getStatus()), PatentSoft::getStatus, patentSoft.getStatus());//student.getUsername()包含于Student::getUsername

        IPage page = new Page(currentPage, pageSize);
        patentSoftMapper.selectPage(page, q); //分页查询条件
        return page;
    }

    //获取
    @Override
    public List<PatentSoft> getByids(List<Integer> ids) {
        QueryWrapper<PatentSoft> q=new QueryWrapper<>();
        q.in("id",ids);
        return patentSoftMapper.selectList(q);
    }

    @Override
    public Boolean deleteAllPatentSoftOfUsername(String username) {
        QueryWrapper<PatentSoft> q=new QueryWrapper<>();
        q.eq("username",username);
        List<PatentSoft> list=patentSoftMapper.selectList(q);//找到该用户所有的monograph成果
        for (PatentSoft it:list){
            deletePatentSoft(it.getId());
        }
        //找到要删除的username 对应的 monograph成果  之后找到这些成果对应的id 就可以完成 删除该成果的所有信息的功能
        return true;
    }

}
