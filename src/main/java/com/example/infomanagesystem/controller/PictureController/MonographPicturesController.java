package com.example.infomanagesystem.controller.PictureController;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.infomanagesystem.entity.PictureEntity.MonographPicture;
import com.example.infomanagesystem.mapper.PictureMapper.MonographPictureMapper;
import com.example.infomanagesystem.result.R;
import com.example.infomanagesystem.utils.UploadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

/**
 * @Author xushupeng
 * @Date 2023-08-12 17:58
 */
@RestController
@CrossOrigin
@RequestMapping("/MonographPicture")
public class MonographPicturesController {
    @Autowired
    private MonographPictureMapper monographPictureMapper;
    //public static  final String pre="https://xsp-datastore.oss-cn-chengdu.aliyuncs.com/";
    public static  final String pre="https://xspfile.yougi.top/";
    //图片的上传
    @SaCheckLogin
    @PostMapping("/uploadPictures")
    public R uploadPictures(@RequestParam("files") MultipartFile[] files,@RequestParam("username") String username ,@RequestParam("achievementName") String achievementName) throws IOException {

        System.out.println("图片所对应的成果名字 "+achievementName);
        System.out.println("上传图片的username is  "+username);
        System.out.println("list is "+ files.length);
        List<Map<String,String>> pictureList=new ArrayList<>();
        for (MultipartFile file : files) {
            String filename = file.getOriginalFilename();  //获取上传图片的文件名，包含后缀
            System.out.println("filename is "+filename);

            String url=UploadUtil.uploadImage(file);
            //url 应该去掉前缀

            url=url.substring(pre.length());
            System.out.println("url is"+url);
            Map<String,String> picture=new HashMap<>();
            picture.put("pictureName",filename);
            picture.put("pictureUrl",url);
            pictureList.add(picture);
            monographPictureMapper.insert(new MonographPicture(username,achievementName,url));
            //同时数据表里面也应该加入信息  uername  成果名字  url
        }
        return  new R(true,200,"多组文件上传成功",pictureList);
    }

    //查询对应用户 对应成果 的图片列表
    @SaCheckLogin
    @GetMapping("/picturesList/{username}/{achievementName}")
    public R getPicturesList(@PathVariable String username,@PathVariable String achievementName){
        System.out.println("成果的名字 is "+achievementName);
        System.out.println("获取列表的userame is "+username);
        QueryWrapper<MonographPicture> q=new QueryWrapper<>();
        q.eq("username",username).eq("achievementName",achievementName);
        return new R(true,200,"得到的成果数据",monographPictureMapper.selectList(q));
    }

    //图片的删除  删除oss对应内容 也删除 数据表对应内容
    @SaCheckLogin
    @PostMapping("/deleteImg")
    public R deleteImg(@RequestBody MonographPicture monographPicture){
        String username=monographPicture.getUsername();
        String achievementName=monographPicture.getAchievementName();
        String url=monographPicture.getUrl();
        QueryWrapper<MonographPicture> q=new QueryWrapper<>();
        q.eq("username",username).eq("achievementName",achievementName).eq("url",url);
        if(monographPictureMapper.delete(q)>0){ //删除单个图片
            System.out.println("要删除的oss的url is "+url);
            System.out.println( UploadUtil.deleteFile(url));
            return new R(true,200,"删除图片成功");
        }
        return new R(false,404,"删除图片失败");
    }
}
