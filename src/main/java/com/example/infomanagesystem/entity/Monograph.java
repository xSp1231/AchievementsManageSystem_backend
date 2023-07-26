package com.example.infomanagesystem.entity;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @Author xushupeng
 * @Date 2023-07-25 14:32
 *专著表
 */
@Data
@TableName("t_monograph")
public class Monograph {
    @ExcelProperty(value = "ID")
    @ExcelIgnore  //忽略 可以不读  excel文件里面可以不需要这一列
    @ColumnWidth(20)
    private Integer id; //每一个专著信息的id  very important

    @ExcelProperty(value = "用户名")
    @ColumnWidth(20)
    private String username;//用户名

    @ExcelProperty(value = "专著名称")
    @ColumnWidth(20)
    private String monoName;

    @ExcelProperty(value = "主编")
    @ColumnWidth(20)
    private String chiefEditor;
    @ExcelProperty(value = "参编")
    @ColumnWidth(20)
    private String associateEditor;

    @ExcelProperty(value = "出版社")
    @ColumnWidth(20)
    private String publication;

    @ExcelProperty(value = "出版日期")
    @ColumnWidth(20)
    private String publicDate;//出版日期


    @ExcelProperty(value = "isbn")
    @ColumnWidth(20)
    private String isbn;//卷号

    @ExcelProperty(value = "状态")
    @ColumnWidth(20)
    private String status;//当前专著成果的审核状态  审核  接收  打回

}
