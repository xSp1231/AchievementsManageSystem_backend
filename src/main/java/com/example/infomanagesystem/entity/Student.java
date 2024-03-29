package com.example.infomanagesystem.entity;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.springframework.boot.context.properties.bind.DefaultValue;

//学生实体类
@Data
@TableName("t_student") //绑定数据库的表名
public class Student {

    @ExcelProperty(value = "ID")
    @ExcelIgnore  //忽略 可以不读  excel文件里面可以不需要这一列
    @ColumnWidth(10)
    private Integer id;

    @ExcelProperty(value = "用户名")
    @ColumnWidth(10)
    private String username; //用户名

    @ExcelProperty(value = "密码")
    @ColumnWidth(10)
    private String password; //密码

    @ExcelProperty(value = "姓名")
    @ColumnWidth(10)
    private String name;  //姓名

    @ExcelProperty(value = "专业班级")
    @ColumnWidth(10)
    private String major; //专业 计科2009班

    @ExcelProperty(value = "角色")
    @ColumnWidth(10)
    private String role="学生";  //角色 学生

    //qq邮箱  导入导出的时候需要
    @ExcelProperty(value = "qq号")
    @ColumnWidth(10)
    private String email;

    @ExcelProperty(value = "地区")
    @ColumnWidth(10)
    private String  area ="";  //地区


    @ExcelProperty(value = "帐号状态")
    @ColumnWidth(10)
    private int status=1;   //账号状态 1 可用 0 不可用

    private String avtar="https://xspfile.yougi.top/%E5%B1%B1%E5%B7%9D7bb4d9cbbc8441c99ecdabf1346ac001.jpg";   // 头像
}
