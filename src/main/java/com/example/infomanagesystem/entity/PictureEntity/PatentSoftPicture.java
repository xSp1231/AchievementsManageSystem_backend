package com.example.infomanagesystem.entity.PictureEntity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @Author xushupeng
 * @Date 2023-08-13 19:23
 */
@Data
@TableName("t_patentsoft_picture") //专利软著
public class PatentSoftPicture {
    private Integer id;

    private String username;

    private String achievementName;//成果名字

    private String url;

    public PatentSoftPicture (String username, String achievementName, String url) {
        this.username=username;
        this.achievementName=achievementName;
        this.url=url;
    }


}
