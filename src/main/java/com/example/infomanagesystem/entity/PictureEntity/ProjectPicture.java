package com.example.infomanagesystem.entity.PictureEntity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @Author xushupeng
 * @Date 2023-08-13 20:08
 */

@Data
@TableName("t_project_picture")
public class ProjectPicture {
    private Integer id;

    private String username;

    private String achievementName;

    private String url;

    public ProjectPicture(String username, String achievementName, String url) {
        this.username=username;
        this.achievementName=achievementName;
        this.url=url;
    }
}
