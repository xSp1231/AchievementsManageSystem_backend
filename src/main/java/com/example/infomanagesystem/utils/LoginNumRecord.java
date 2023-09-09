package com.example.infomanagesystem.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @Author xushupeng
 * @Date 2023-09-09 20:58
 */

@Component
public class LoginNumRecord {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    private static  final  int expireTime=15;//设置15天过期时间
    public void addNum() {//+1操作
        LocalDateTime now = LocalDateTime.now();
        String date = now.format(DateTimeFormatter.ofPattern("yyyyMMdd")); //时间格式化 //生成键 当天的日期为键
        String key = "NumOf" + date;
        if (Boolean.TRUE.equals(stringRedisTemplate.hasKey(key))) {//如果key存在 +1
            System.out.println("登录次数+1");
            stringRedisTemplate.opsForValue().increment(key);//对键进行自增操作  如果键不存在 则创建一个   每个用户登陆时 就调用
        } else { //不存在的话 创建key 设置过期时间
            System.out.println("不存在 设置键 登录次数+1  设置过期时间");
            stringRedisTemplate.opsForValue().set(key, "1", expireTime, TimeUnit.DAYS);
        }
    }

    public List<Map<String, Integer>> getPastSevenDays() { //得到近七天的登录数据
        List<Map<String, Integer>>res=new ArrayList<>();
        LocalDate today = LocalDate.now(); //今天的日期
        for (int i = 0; i < 7; i++) { //获取过去七天的日期 减去指定天数 获得过去的日期
            Map<String,Integer> map=new HashMap<>();
            LocalDate pastDate = today.minusDays(i);
            String date=pastDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            String key = "NumOf" + pastDate.format(DateTimeFormatter.ofPattern("yyyyMMdd")); //生成键=>之后根据键 来获得对应的天数
            if (stringRedisTemplate.opsForValue().get(key) == null) { //如果某一天没有登录 没有设置 键 值 。那么直接 设置 其键值为0
                stringRedisTemplate.opsForValue().set(key, "0",expireTime,TimeUnit.DAYS);
                map.put(date,0);
            }
            else {
                map.put(date,Integer.parseInt(stringRedisTemplate.opsForValue().get(key)));
            }
            res.add(map);
        }
        return res;
    }

}
