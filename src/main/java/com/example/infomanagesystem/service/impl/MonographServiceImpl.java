package com.example.infomanagesystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.infomanagesystem.entity.Monograph;
import com.example.infomanagesystem.entity.ScientificPaper;
import com.example.infomanagesystem.mapper.MonographMapper;
import com.example.infomanagesystem.mapper.ScientificPaperMapper;
import com.example.infomanagesystem.service.ManagerService;
import com.example.infomanagesystem.service.MonographService;
import com.example.infomanagesystem.service.StudentService;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Override
    public Monograph getMonographById(Integer id) {
        return monographMapper.selectById(id);
    }

    @Override
    public boolean saveMonograph(Monograph monograph) { //如果相同用户名 相同标题已经存在 就不能上传 除非删除
        QueryWrapper<Monograph> q=new QueryWrapper<>();
        q.eq("username",monograph.getUsername());
        q.eq("monoName",monograph.getMonoName());
        //看增加的成果的用户名是否存在
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
    public boolean deleteMonograph(Integer id) {
        return monographMapper.deleteById(id)>0;
    }

    @Override
    public void deleteBatch(List<Integer> ids) { //根据id批量删除
        QueryWrapper<Monograph> q=new QueryWrapper<>();
        q.in("id",ids);
        monographMapper.delete(q);
        //  remove(q); //移除满足条件的所有元素 二者等价
    }

    @Override  //编辑信息
    public boolean updateMonograph(Monograph monograph) {
        return monographMapper.updateById(monograph)>0;
    }

    @Override
    public IPage<Monograph> getPage(int currentPage, int pageSize, Monograph monograph) {
        LambdaQueryWrapper<Monograph> q = new LambdaQueryWrapper<>();
        //可以根据什么来查询 username  title  status
        q.like(Strings.isNotEmpty(monograph.getUsername()), Monograph::getUsername, monograph.getUsername());//student.getUsername()包含于Student::getUsername
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
        return monographMapper.delete(q)>0;
    }
}
