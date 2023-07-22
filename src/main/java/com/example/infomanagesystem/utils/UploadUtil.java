package com.example.infomanagesystem.utils;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
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
    public static final String ALI_DOMAIN= "https://xsp-datastore.oss-cn-chengdu.aliyuncs.com/"; //域名
    //生成文件名

    public static  String uploadImage(MultipartFile file) throws IOException {
       String originalFileName= file.getOriginalFilename();
       String ext="."+ FilenameUtils.getExtension(originalFileName);
       String uuid= UUID.randomUUID().toString().replace("-",""); //将uuid里面的”-“替代
       String fileName=uuid+ext; //最终文件名的组成
        //地域节点
       String endPoint="http://oss-cn-chengdu.aliyuncs.com";
        // ID // LTAI5t9zF4K3iz4GPFDjfVQB
        //KEY // UCtA3Zttf4LbFsW8hk8BsXQE0kGvhh
       String acessKeyID="LTAI5t9zF4K3iz4GPFDjfVQB";
       String acessKeySecret="UCtA3Zttf4LbFsW8hk8BsXQE0kGvhh";
        //oss客户端对象
        OSS ossClient=new OSSClientBuilder().build(endPoint,acessKeyID,acessKeySecret);
        ossClient.putObject("xsp-datastore",fileName,file.getInputStream()); //三个参数 "oss的bucket名字"
        ossClient.shutdown();//关闭

        // oss客户端对象
        return ALI_DOMAIN+fileName;
    }
}
