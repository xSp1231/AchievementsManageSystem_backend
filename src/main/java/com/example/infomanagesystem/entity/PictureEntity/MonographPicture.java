package com.example.infomanagesystem.entity.PictureEntity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @Author xushupeng
 * @Date 2023-08-13 9:19
 */
@Data
@TableName("t_monograph_picture")
public class MonographPicture {

    private Integer id;

    private String username;

    private String achievementName;

    private String url;

    public MonographPicture(String username, String achievementName, String url) {
        this.username=username;
        this.achievementName=achievementName;
        this.url=url;
    }
}
