package com.example.infomanagesystem.entity;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @Author xushupeng
 * @Date 2023-07-25 17:29
 *专利软著
 */
@Data
@TableName("t_patentsoft")
public class PatentSoft {

    @ExcelProperty(value = "ID")
    @ExcelIgnore  //忽略 可以不读  excel文件里面可以不需要这一列
    @ColumnWidth(20)
    private Integer id; //每一个专著信息的id  very important

    @ExcelProperty(value = "用户名")
    @ColumnWidth(20)
    private String username;//用户名

    @ExcelProperty(value = "专利/软著名称")
    @ColumnWidth(20)
    private String name;

    @ExcelProperty(value = "专利/软著号")
    @ColumnWidth(20)
    private String number;

    @ExcelProperty(value = "专利类别")
    @ColumnWidth(20)
    private String category;


    @ExcelProperty(value = "专利/软著所有者")
    @ColumnWidth(20)
    private String owners;

    @ExcelProperty(value = "授权国家")
    @ColumnWidth(20)
    private String country;//出版日期


    @ExcelProperty(value = "状态")
    @ColumnWidth(20)
    private String status;//当前专著成果的审核状态  审核  接收  打回

}
