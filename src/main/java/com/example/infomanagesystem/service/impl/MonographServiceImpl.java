package com.example.infomanagesystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.infomanagesystem.entity.Monograph;
import com.example.infomanagesystem.entity.PictureEntity.MonographPicture;
import com.example.infomanagesystem.entity.ScientificPaper;
import com.example.infomanagesystem.mapper.MonographMapper;
import com.example.infomanagesystem.mapper.PictureMapper.MonographPictureMapper;
import com.example.infomanagesystem.mapper.ScientificPaperMapper;
import com.example.infomanagesystem.service.ManagerService;
import com.example.infomanagesystem.service.MonographService;
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
 * @Date 2023-07-25 15:05
 */
@Service
public class MonographServiceImpl extends ServiceImpl<MonographMapper, Monograph>  implements MonographService {
    @Autowired
    private MonographMapper monographMapper;
    @Autowired
    private StudentService studentService;

    @Autowired
    private  MonographPictureMapper monographPictureMapper;

    @Override
    public List<Monograph> getAllMonograph() {
        return monographMapper.selectList(null);
    }
    //找到登录者的所有专著成果信息
    @Override
    public List<Monograph> getUserMonograph( String username) {
        QueryWrapper<Monograph> q=new QueryWrapper<>();
        q.eq("username",username);
        return monographMapper.selectList(q);
    }

    //删除monograph成果
    @Override
    @Cacheable(cacheNames = "Monograph",key = "#id")  //编辑的时候 查询用户成果信息
    public Monograph getMonographById(Integer id) {
        return monographMapper.selectById(id);
    }

    @Override
    @CacheEvict(cacheNames = "MonoPage", allEntries = true)  //新增的时候 删除SCpage缓存目录下面的所有缓存
    public boolean saveMonograph(Monograph monograph) { //如果相同用户名 相同标题已经存在 就不能上传 除非删除
        QueryWrapper<Monograph> q=new QueryWrapper<>();
        q.eq("username",monograph.getUsername());
        q.eq("monoName",monograph.getMonoName());
        //看增加的成果的用户名是否存在   //同名 同成果
        if(studentService.selectStudentByUsername(monograph.getUsername())==null){
            return false;
        }

        if(monographMapper.selectOne(q)==null){ //相同用户 相同标题展示不存在
            return monographMapper.insert(monograph)>0;
        }
        else{
            return false;
        }
    }

    @Override
    @Caching(evict = {
            @CacheEvict(cacheNames = "Monograph", key = "#id"), //执行删除用户操作时
            @CacheEvict(cacheNames = "MonoPage", allEntries = true)  //删除getPage缓存目录下面的所有缓存
    })
    public boolean deleteMonograph(Integer id) {  //删除某一项成果---也需要将对应的img删除
        Monograph  monograph= monographMapper.selectById(id);//根据id搜索成果信息
        //根据成果的username monoName(成果名字) 删除对应的成果信息
        String username=monograph.getUsername();
        String achievementName=monograph.getMonoName();
        QueryWrapper<MonographPicture> q=new QueryWrapper<>();
        q.eq("username",username).eq("achievementName",achievementName);
        //一个成果可能对应多张图片  ----删除图片  //找到符合要求的
        List<MonographPicture> lis=monographPictureMapper.selectList(q);
        if(lis!=null){
            for (MonographPicture it :lis){
                UploadUtil.deleteFile(it.getUrl());///删除oss上面对应的文件
                monographPictureMapper.deleteById(it.getId());//删除整个对象
            }
        }
        return monographMapper.deleteById(id)>0;//删除成果
    }
    //批量删除成果
    @Override
    @CacheEvict(cacheNames = "MonoPage", allEntries = true)  //删除getPage缓存目录下面的所有缓存
    public void deleteBatch(List<Integer> ids) { //根据id批量删除
//        QueryWrapper<Monograph> q=new QueryWrapper<>();
//        q.in("id",ids);
//        monographMapper.delete(q);
        for(Integer id:ids){//循环删除
            deleteMonograph(id);
        }
        //  remove(q); //移除满足条件的所有元素 二者等价
    }
    @Override  //编辑信息
    @Caching(evict = {
            @CacheEvict(cacheNames = "Monograph", key = "#monograph.id"), //执行删除用户操作时
            @CacheEvict(cacheNames = "MonoPage", allEntries = true)  //删除getPage缓存目录下面的所有缓存
    })
    public boolean updateMonograph(Monograph monograph) {
        return monographMapper.updateById(monograph)>0;
    }

    @Override
    @Cacheable(cacheNames = "MonoPage",key = "#currentPage+'_'+#pageSize+'_'+#monograph.username+'_'+#monograph.monoName+'_'+#monograph.status")
    public IPage<Monograph> getPage(int currentPage, int pageSize, Monograph monograph) {
        LambdaQueryWrapper<Monograph> q = new LambdaQueryWrapper<>();
        //可以根据什么来查询 username  title  status
        //username查询一定要精确 防止用户名为test的人登录账号后 得到用户名为test1的用户的信息  这个时候应该避免模糊查询 使用精确查询
        q.eq(Strings.isNotEmpty(monograph.getUsername()), Monograph::getUsername, monograph.getUsername());//student.getUsername()包含于Student::getUsername
        q.like(Strings.isNotEmpty(monograph.getMonoName()), Monograph::getMonoName, monograph.getMonoName());//("title","理论力学")
        q.like(Strings.isNotEmpty(monograph.getStatus()), Monograph::getStatus, monograph.getStatus());//student.getUsername()包含于Student::getUsername

        IPage page = new Page(currentPage, pageSize);
        monographMapper.selectPage(page, q); //分页查询条件
        return page;
    }

    //获取
    @Override
    public List<Monograph> getByids(List<Integer> ids) {
        QueryWrapper<Monograph> q=new QueryWrapper<>();
        q.in("id",ids);
        return monographMapper.selectList(q);
    }

    @Override
    public Boolean deleteAllMonographOfUsername(String username) {
        QueryWrapper<Monograph> q=new QueryWrapper<>();
        q.eq("username",username);
        List<Monograph> list=monographMapper.selectList(q);//找到该用户所有的monograph成果
        for (Monograph it:list){
            deleteMonograph(it.getId());
        }
        //找到要删除的username 对应的 monograph成果  之后找到这些成果对应的id 就可以完成 删除该成果的所有信息的功能
        return true;
    }
}
