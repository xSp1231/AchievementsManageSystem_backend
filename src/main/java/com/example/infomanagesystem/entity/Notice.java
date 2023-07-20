package com.example.infomanagesystem.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @Author xushupeng
 * @Date 2023-07-19 19:50
 */
@Data
@TableName("t_notice")
public class Notice {
    private int id;
    private String  title;

}
