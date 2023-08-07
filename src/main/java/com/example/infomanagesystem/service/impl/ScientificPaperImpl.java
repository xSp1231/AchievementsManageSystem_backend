package com.example.infomanagesystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.infomanagesystem.entity.Monograph;
import com.example.infomanagesystem.entity.ScientificPaper;
import com.example.infomanagesystem.entity.Student;
import com.example.infomanagesystem.mapper.ScientificPaperMapper;
import com.example.infomanagesystem.service.ScientificPaperService;
import com.example.infomanagesystem.service.StudentService;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author xushupeng
 * @Date 2023-07-24 14:39
 */
@Service
public class ScientificPaperImpl extends ServiceImpl<ScientificPaperMapper,ScientificPaper> implements ScientificPaperService  {
   @Autowired
   private ScientificPaperMapper scientificPaperMapper;
    @Autowired
    private StudentService studentService;
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
    public ScientificPaper getScientificPaperById(Integer id) {
        return scientificPaperMapper.selectById(id);
    }

    @Override
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
    public boolean deleteScientificPaper(Integer id) {
        return scientificPaperMapper.deleteById(id)>0;
    }

    @Override
    public void deleteBatch(List<Integer> ids) { //根据id批量删除
        QueryWrapper<ScientificPaper> q=new QueryWrapper<>();
        q.in("id",ids);
        scientificPaperMapper.delete(q);
      //  remove(q); //移除满足条件的所有元素 二者等价
    }

    @Override  //编辑信息
    public boolean updateScientificPaper(ScientificPaper scientificPaper) {
        return scientificPaperMapper.updateById(scientificPaper)>0;
    }

    @Override
    public IPage<ScientificPaper> getPage(int currentPage, int pageSize, ScientificPaper scientificPaper) {
        LambdaQueryWrapper<ScientificPaper> q = new LambdaQueryWrapper<>();
        //可以根据什么来查询 username  title  status
        q.like(Strings.isNotEmpty(scientificPaper.getUsername()), ScientificPaper::getUsername, scientificPaper.getUsername());//student.getUsername()包含于Student::getUsername
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
        return scientificPaperMapper.delete(q)>0;
    }
}
