package com.example.infomanagesystem.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.infomanagesystem.entity.Student;
import com.example.infomanagesystem.mapper.MonographMapper;
import com.example.infomanagesystem.mapper.StudentMapper;
import com.example.infomanagesystem.service.StudentService;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import java.util.List;

//学生登录 注册

@Service
public class StudentServiceImpl extends ServiceImpl<StudentMapper, Student> implements StudentService {
    @Autowired
    private StudentMapper studentMapper;
    @Override
    public String getRoleByUsername(String username) {
        QueryWrapper<Student> q=new QueryWrapper<>();
        q.eq("username",username);
        return studentMapper.selectOne(q).getRole();
    }

    @Override
    public Student login(String username ,String password) {
            QueryWrapper<Student> q=new QueryWrapper<>();
            //寻找用户
            q.eq("username",username).eq("password",password);

        return studentMapper.selectOne(q);
    }
    @Override
    public boolean register(Student student) { //注册成功  同时要判断用户是否已经存在
        String username=student.getUsername();
        QueryWrapper<Student> q=new QueryWrapper<>();
        q.eq("username",username);
        if(studentMapper.selectOne(q)!=null){ //用户存在
            return false;
        }
        //增加操作 //用户不存在
        return studentMapper.insert(student) > 0;
    }

    @Override
    public int checkStatus(String username) {
        QueryWrapper<Student> q=new QueryWrapper<>();
        q.eq("username",username);
        Student t=studentMapper.selectOne(q);
        if(t!=null){ //找到了这个用户
            return t.getStatus();
        }//返回状态码
        else{//没找到这个用户
            return 0;
        }
    }

    @Override
    public List<Student> getAll() {
        return studentMapper.selectList(null);
    }
    @Override
    @CacheEvict(cacheNames = "getPage", allEntries = true)  //增加用户 删除原有的getPage缓存目录下面的所有缓存
    public boolean saveStudent(Student student) { //学生添加 不能使得用户名重复
        String username=student.getUsername();
        QueryWrapper<Student> q=new QueryWrapper<>();
        q.eq("username",username);
        if(studentMapper.selectOne(q)!=null){ //管理员增加学生用户  不能重用户名
            return false;
        }
        else{
        return studentMapper.insert(student)>0; //添加学生
        }
    }
    //删除用户
    @Override
    @Caching(evict = {
            @CacheEvict(cacheNames = "username", key = "#username"), //执行删除用户操作时
            @CacheEvict(cacheNames = "getPage", allEntries = true)  //删除getPage缓存目录下面的所有缓存
    })
    public boolean deleteStudentByUsername(String username) {
        QueryWrapper<Student> q=new QueryWrapper<>();
        q.eq("username",username);
        //先删除用户的成果 再删除用户
        StpUtil.logout(username);//强制使得token失效
        return studentMapper.delete(q) > 0;
    }

    @Override  //前端表格会传递 用户名(不可以改动 unique)  密码  姓名 专业信息 账号状态
    @Caching(evict = {
            @CacheEvict(cacheNames = "username", key = "#student.username"), //执行删除用户更新时
            @CacheEvict(cacheNames = "getPage", allEntries = true)  //删除getPage缓存目录下面的所有缓存
    })
    public boolean updateStudent(Student student) {  //修改学生信息  班级 用户名 账号 密码 专业班级  账号状态
        QueryWrapper<Student> q=new QueryWrapper<>();
        q.eq("username",student.getUsername());
        Student origin=studentMapper.selectOne(q);
        System.out.println("原来的的学生类"+origin);//找到原来的学生信息 我们需要获取他的id 和role 因为前端修改模块那里不会显示这些
        student.setId(origin.getId());
        student.setRole(origin.getRole());
        System.out.println("修改后的学生类"+student);
        //管理员修改学生用户  不能重用户名  //前端表格将用户名修改为禁止状态
        return studentMapper.updateById(student)>0;
    }


    //个人页面修改信息 修改完 删除缓存
    @Override
    @Caching(evict = {
            @CacheEvict(cacheNames = "username", key = "#student.username"), //执行删除用户更新时
            @CacheEvict(cacheNames = "getPage", allEntries = true)  //删除getPage缓存目录下面的所有缓存
    })
    public boolean editStudent(Student student) {
        return studentMapper.updateById(student)>0;
    }

    @Override
    @Cacheable(cacheNames = "username",key = "#username")  //设置缓存空间 以及键的名字  编辑的时候 回显用户信息
    public Student selectStudentByUsername(String username) {
        QueryWrapper<Student> q=new QueryWrapper<>();
        q.eq("username",username); //编辑学生信息的时候 先要根据username 搜寻用户
        System.out.println("得到的学生信息"+studentMapper.selectOne(q));
        return studentMapper.selectOne(q);
    }

    @Override
    @Cacheable(cacheNames = "getPage",key="#currentPage+'_'+#pageSize+'_'+#student.username+'_'+#student.major+'_'+#student.name")
    public IPage<Student> getPage(int currentPage, int pageSize,Student student) { //里面写查询条件
        System.out.println("条件查询");
        LambdaQueryWrapper<Student> q = new LambdaQueryWrapper<>();
        //模糊查询查的是字串 而不是子序列  xs 可以查到xsp sp可以查到xsp  但是xp查不到xsp
        //用户名 major status name  如果前端传递的查询字符串不为空，则在 Student 实体的 username 属性中查找包含该字符串的记录。如果前端没有传递查询字符串，则不添加该查询条件。
        q.like(Strings.isNotEmpty(student.getUsername()), Student::getUsername, student.getUsername());//student.getUsername()包含于Student::getUsername
        q.like(Strings.isNotEmpty(student.getMajor()), Student::getMajor, student.getMajor());
        q.like(Strings.isNotEmpty(student.getName()), Student::getName, student.getName());
      //  q.eq(student.getStatus() ==1||student.getStatus()==0, Student::getStatus, student.getStatus());
        IPage page = new Page(currentPage, pageSize);
        studentMapper.selectPage(page, q); //分页查询条件
        return page;
    }

    //批量删除
    @Override
    @CacheEvict(cacheNames = "getPage", allEntries = true)  //删除getPage缓存目录下面的所有缓存
    public void deleteUsers(List<String> usernames) {
        QueryWrapper<Student> q=new QueryWrapper<>();
        q.in("username",usernames);
        for(String it:usernames){
            deleteStudentByUsername(it); //方法互相调用
        }
        //remove 方法是 IService 接口中定义的方法，实现了该接口的服务类可以直接使用该方法。
        //remove 方法可以删除符合条件的一组对象，也可以删除单个对象。
        //具体来说，如果传入的参数是一个对象，则会删除这个对象；如果传入的参数是一个查询条件对象，则会删除符合条件的所有对象。
        //remove(q);
    }

    @Override
    public List<Student> getByUsernames(List<String> usernames) {
        QueryWrapper<Student> q=new QueryWrapper<>();
        q.in("username",usernames);
        return studentMapper.selectList(q);
    }

    @Override
    public int checkUsernameAndEmail(String username,String email) {
        QueryWrapper<Student> q=new QueryWrapper<>();
        q.eq("username",username);
        Student temp=studentMapper.selectOne(q);
        if(temp!=null){//该用户存在
            if(email.equals(temp.getEmail())){
                return 1;// "用户名,邮箱号匹配成功";
            }
            else{
                return 2;//"邮箱号填写错误(可能是添加了@qq.com后缀)";
            }
        }
        else{
           return  0;//"用户名输入错误!(用户不存在)";
        }
    }
}
