package com.example.infomanagesystem.entity.PictureEntity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @Author xushupeng
 * @Date 2023-08-13 17:56
 */
@Data
@TableName("t_scientificpaper_picture")
public class ScientificPaperPicture {

    private Integer id;

    private String username;

    private String achievementName;//成果名字

    private String url;

    public ScientificPaperPicture (String username, String achievementName, String url) {
        this.username=username;
        this.achievementName=achievementName;
        this.url=url;
    }
}
