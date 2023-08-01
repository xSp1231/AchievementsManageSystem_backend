package com.example.infomanagesystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.infomanagesystem.entity.Monograph;
import com.example.infomanagesystem.entity.PatentSoft;
import com.example.infomanagesystem.mapper.PatentSoftMapper;
import com.example.infomanagesystem.service.PatentSoftService;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
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
    public PatentSoft getPatentSoftById(Integer id) {
        return patentSoftMapper.selectById(id);
    }

    @Override
    public boolean savePatentSoft(PatentSoft patentSoft) { //如果相同用户名 相同标题已经存在 就不能上传 除非删除
        QueryWrapper<PatentSoft> q=new QueryWrapper<>();
        q.eq("username",patentSoft.getUsername());
        q.eq("name",patentSoft.getName());

        if(patentSoftMapper.selectOne(q)==null){ //相同用户 相同标题展示不存在
            return patentSoftMapper.insert(patentSoft)>0;
        }
        else{
            return false;
        }
    }

    @Override
    public boolean deletePatentSoft(Integer id) {
        return patentSoftMapper.deleteById(id)>0;
    }

    @Override
    public void deleteBatch(List<Integer> ids) { //根据id批量删除
        QueryWrapper<PatentSoft> q=new QueryWrapper<>();
        q.in("id",ids);
        patentSoftMapper.delete(q);
        //  remove(q); //移除满足条件的所有元素 二者等价
    }

    @Override  //编辑信息
    public boolean updatePatentSoft(PatentSoft patentSoft) {
        return patentSoftMapper.updateById(patentSoft)>0;
    }

    @Override
    public IPage<PatentSoft> getPage(int currentPage, int pageSize, PatentSoft patentSoft) {
        LambdaQueryWrapper<PatentSoft> q = new LambdaQueryWrapper<>();
        //可以根据什么来查询 username  name  status
        q.like(Strings.isNotEmpty(patentSoft.getUsername()), PatentSoft::getUsername, patentSoft.getUsername());//student.getUsername()包含于Student::getUsername
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
        return patentSoftMapper.delete(q)>0;
    }

}
