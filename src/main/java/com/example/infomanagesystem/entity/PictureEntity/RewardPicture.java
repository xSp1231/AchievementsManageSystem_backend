package com.example.infomanagesystem.entity.PictureEntity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @Author xushupeng
 * @Date 2023-08-13 19:48
 */
@Data
@TableName("t_reward_picture")
public class RewardPicture {
    private Integer id;

    private String username;

    private String achievementName;

    private String url;

    public RewardPicture (String username, String achievementName, String url) {
        this.username=username;
        this.achievementName=achievementName;
        this.url=url;
    }
}
