package com.example.infomanagesystem.entity;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @Author xushupeng
 * @Date 2023-07-25 20:00
 * 奖项表 参加的一些比赛
 */
@Data
@TableName("t_reward")
public class Reward {

    @ExcelProperty(value = "ID")
    @ExcelIgnore  //忽略 可以不读  excel文件里面可以不需要这一列
    @ColumnWidth(20)
    private Integer id; //每一个专著信息的id  very important

    @ExcelProperty(value = "用户名")
    @ColumnWidth(20)
    private String username;//用户名

    @ExcelProperty(value = "奖项名称")
    @ColumnWidth(20)
    private String rewardName;

    @ExcelProperty(value = "证书编号")
    @ColumnWidth(20)
    private String rewardNumber ;

    @ExcelProperty(value = "获奖区域")
    @ColumnWidth(20)
    private String rewardArea;

    @ExcelProperty(value = "等级")
    @ColumnWidth(20)
    private int rewardGrade;//1,2,3级

    @ExcelProperty(value = "授予单位")
    @ColumnWidth(20)
    private String grantingUnit;//授予单位

    @ExcelProperty(value = "日期")
    @ColumnWidth(20)
    private String dateTime;

    @ExcelProperty(value = "成员")
    @ColumnWidth(20)
    private String members;//当前专著成果的审核状态  审核  接收  打回

    @ExcelProperty(value = "本人位次")
    @ColumnWidth(20)
    private String place;

    @ExcelProperty(value = "成果填报状态")
    @ColumnWidth(20)
    private String status;

}
