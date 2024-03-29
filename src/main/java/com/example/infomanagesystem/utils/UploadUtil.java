package com.example.infomanagesystem.utils;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.ObjectMetadata;
import org.apache.commons.io.FilenameUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

/**
 * @Author xushupeng
 * @Date 2023-07-20 15:31
 */
///图片上传到oss的工具类
public class UploadUtil {
    //阿里oss域名
    public static final String ALI_DOMAIN = "https://xspfile.yougi.top/";//访问域名


    //生成文件名
    public static final String endPoint = "http://oss-cn-chengdu.aliyuncs.com";
    // ID // LTAI5t9zF4K3iz4GPFDjfVQB
    //KEY // UCtA3Zttf4LbFsW8hk8BsXQE0kGvhh
    public static final String acessKeyID = "LTAI5t9zF4K3iz4GPFDjfVQB";
    public static final String acessKeySecret = "UCtA3Zttf4LbFsW8hk8BsXQE0kGvhh";



    public static String uploadImage(MultipartFile file) throws IOException {
        String originalFileName = file.getOriginalFilename();
        String pre = originalFileName.substring(0, originalFileName.lastIndexOf('.'));//左闭右开区间
        String ext = "." + FilenameUtils.getExtension(originalFileName);//文件后缀
        String uuid = UUID.randomUUID().toString().replace("-", ""); //将uuid里面的”-“替代
        String fileName = pre + uuid + ext; //最终文件名的组成
        //oss客户端对象
        OSS ossClient = new OSSClientBuilder().build(endPoint, acessKeyID, acessKeySecret);
        ossClient.putObject("xsp-datastore", fileName, file.getInputStream()); //三个参数 "oss的bucket名字"
        ossClient.shutdown();//关闭
        // oss客户端对象
        return ALI_DOMAIN + fileName;//访问路径
    }
    public static String deleteFile(String fileName) {
        //oss客户端对象
        OSS ossClient = new OSSClientBuilder().build(endPoint, acessKeyID, acessKeySecret);
        ossClient.deleteObject("xsp-datastore", fileName);//filename  xxxx.jpg
        return fileName + "删除成功";

    }
}
