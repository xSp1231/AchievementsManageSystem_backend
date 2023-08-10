package com.example.infomanagesystem.entity;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @Author xushupeng
 * @Date 2023-07-25 19:41
 */
@Data
@TableName("t_project")
public class Project {

    @ExcelProperty(value = "ID")
    @ExcelIgnore  //忽略 可以不读  excel文件里面可以不需要这一列
    @ColumnWidth(20)
    private Integer id; //每一个专著信息的id  very important

    @ExcelProperty(value = "用户名")
    @ColumnWidth(20)
    private String username;//用户名

    @ExcelProperty(value = "项目名")
    @ColumnWidth(20)
    private String projectName;

    @ExcelProperty(value = "项目负责人")
    @ColumnWidth(20)
    private String leader;

    @ExcelProperty(value = "指导教师")
    @ColumnWidth(20)
    private String teacher;

    @ExcelProperty(value = "项目类别")
    @ColumnWidth(20)
    private String category;


    @ExcelProperty(value = "研究周期")
    @ColumnWidth(20)
    private String period;

    @ExcelProperty(value = "本人位次")
    @ColumnWidth(20)
    private Integer place;

    @ExcelProperty(value = "项目全体成员")
    @ColumnWidth(20)
    private String members;

    @ExcelProperty(value = "项目积分")
    @ColumnWidth(20)
    private Float score;

    @ExcelProperty(value = "本人贡献")
    @ColumnWidth(20)
    private String contribution;//出版日期


    @ExcelProperty(value = "结题时间")
    @ColumnWidth(20)
    private String endingTime;

    @ExcelProperty(value = "拒绝信息")
    @ExcelIgnore  //忽略 可以不读  excel文件里面可以不需要这一列
    @ColumnWidth(20)
    private String refuseInfo;


    @ExcelProperty(value = "状态")
    @ColumnWidth(20)
    private String status;//当前专著成果的审核状态  审核  接收  打回

}
