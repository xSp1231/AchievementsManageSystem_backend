package com.example.infomanagesystem.entity;
import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import net.sf.jsqlparser.expression.DateTimeLiteralExpression;

@Data
@TableName("t_message")
public class Message {
    private Integer id;
    private String message;//消息内容
    private String username;//用户名
    private String status;//接收、拒绝
    private String  project;//哪一个成果表
    private String  name;//对应的成果名字
    private String  isreading;//消息是否已读  1/0
    private String  audittime;//审核时间
}
