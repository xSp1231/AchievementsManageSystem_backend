package com.example.infomanagesystem.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @Author xushupeng
 * @Date 2023-07-19 19:50
 */

//公告实体类的创建
@Data
@TableName("t_notice")
public class Notice {
    private Integer id;
    private String  title; //标题
    private String  comment;//内容
    private String  status;//公告状态  紧急  一般
    private String  time; //公告发布时间
}
