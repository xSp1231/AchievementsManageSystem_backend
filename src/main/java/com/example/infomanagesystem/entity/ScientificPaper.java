package com.example.infomanagesystem.entity;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @Author xushupeng
 * @Date 2023-07-24 12:58
 *科技论文表
 */

@Data
@TableName("t_scientific")
public class ScientificPaper {
    @ExcelProperty(value = "ID")
    @ExcelIgnore  //忽略 可以不读  excel文件里面可以不需要这一列
    @ColumnWidth(20)
    private Integer id;

    @ExcelProperty(value = "用户名")
    @ColumnWidth(20)
    private String username;//用户名

    @ExcelProperty(value = "标题")
    @ColumnWidth(20)
    private String title;

    @ExcelProperty(value = "期刊会议名称")
    @ColumnWidth(20)
    private String jcName;//期刊会议名称

    @ExcelProperty(value = "出版日期")
    @ColumnWidth(20)
    private String publicDate;//出版日期

    @ExcelProperty(value = "期号")
    @ColumnWidth(20)
    private String issueNumber;//期号

    @ExcelProperty(value = "卷号")
    @ColumnWidth(20)
    private String volumeNumber;//卷号

    @ExcelProperty(value = "页码范围")
    @ColumnWidth(20)
    private String pageRange;//页码范围

    @ExcelProperty(value = "作者位次")
    @ColumnWidth(20)
    private Integer  place;//作者位次

    @ExcelProperty(value = "全部作者")
    @ColumnWidth(20)
    private String allAuthors;//全部作者

    @ExcelProperty(value = "检索类型")
    @ColumnWidth(20)
    private String searchType;//检索类型

    @ExcelProperty(value = "检索号")
    @ColumnWidth(20)
    private String accessionNumber;//检索号


    @ExcelProperty(value = "拒绝详情")
    @ExcelIgnore  //忽略 可以不读  excel文件里面可以不需要这一列
    @ColumnWidth(20)
    private String refuseInfo;

    @ExcelProperty(value = "状态")
    @ColumnWidth(20)
    private String status;//当前成果审核的状态  审核  接收  打回
 }
