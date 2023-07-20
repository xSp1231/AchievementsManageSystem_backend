package com.example.infomanagesystem.controller;

import com.example.infomanagesystem.result.R;
import com.example.infomanagesystem.utils.UploadUtil;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

/**
 * @Author xushupeng
 * @Date 2023-07-20 13:59
 */

//关于一些功能的测试使用
@RestController
@CrossOrigin
public class testController {
    //文件上传接口 上传到本地
    @PostMapping("/upload")
    public R uploadFile(@RequestParam(value = "file",required = false) MultipartFile file){
        if(file.isEmpty()){
            return new R(false,400,"文件为空");
        }
        String OriginalFilename=file.getOriginalFilename();
        //使用时间戳 防止文件重名 构造文件名 hello.png
        String fileName=System.currentTimeMillis()+"."+OriginalFilename.substring(OriginalFilename.lastIndexOf(".")+1); //获取最后一个字符为”.“的下标再+1 之后字符串截取
        System.out.println("得到的filename is "+fileName);
        String path="C:\\Users\\86187\\Desktop\\testimgs\\"; //设置图片的保存位置
        File dest=new File(path+fileName);//根据路径创建一个新的文件对象
        if(!dest.getParentFile().exists()){
            dest.mkdirs();
        }
        try {
              file.transferTo(dest);////file.transferTo(dest)是Java中java.io.File类的一个方法，它的作用是将当前文件对象表示的文件内容传输到指定的目标位置。
              return new R(true,200,"图片上床成功",path+fileName);
        } catch (IOException e) {
            e.printStackTrace();
            return new R(false,400,"图片上传失败");
        }
    }

    //文件上传到阿里云
    @PostMapping("/uploadToOss")
    public R uploadToOss(@RequestParam(value = "file",required = false) MultipartFile file) throws IOException{
        return new R(true,200, UploadUtil.uploadImage(file),"存储到阿里云Oss");
    }
}
