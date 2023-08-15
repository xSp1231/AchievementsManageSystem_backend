package com.example.infomanagesystem.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckRole;
import com.example.infomanagesystem.entity.Notice;
import com.example.infomanagesystem.result.R;
import com.example.infomanagesystem.service.NoticeService;
import com.example.infomanagesystem.utils.UploadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


/**
 * @Author xushupeng
 * @Date 2023-07-21 14:30
 */

@RestController
@CrossOrigin
public class NoticeController {

    @Autowired
    private NoticeService noticeService;

    //查看所有公告
    @SaCheckLogin
    @GetMapping("/getNotices")
    public R getNotices(){
        List<Notice> noticeSList=noticeService.getAllNotice();
        if(noticeSList!=null){
            return new R(true,200,"得到所有notice数据",noticeSList);
        }
        else{
            return new R(false,"查找失败!");
        }
    }
    @SaCheckLogin
    @SaCheckRole("admin")
    @PostMapping("/addNotice")
    public R addNotice(@RequestBody Notice notice){
        if(noticeService.addNotice(notice)){
            return new R(true,200,"notice添加成功");
        }
        else{
            return new R(false,400,"notice添加失败");
        }
    }

    @SaCheckLogin
    @SaCheckRole("admin")
    @PostMapping("/deleteNotice/{id}")
    public R deleteNotice(@PathVariable int id){
        System.out.println("获取的要删除的id是"+id);
        if(noticeService.deleteNoticeById(id)){
            return new R(true,200,"删除成功");
        }
        else{
            return new R(false,404,"删除失败");
        }
    }

    @SaCheckLogin
    @SaCheckRole("admin")
    @PostMapping("/updateNotice")  //暂无修改公告的功能
    public R updateNotice(@RequestBody Notice notice){
        if(noticeService.updateNotice(notice)){
            return new R(true,200,"notice修改成功");
        }
        else{
            return new R(false,400,"notice修改失败");
        }
    }

    //上传图片到阿里云

    @PostMapping("/uploadToOss")
    public R uploadToOss(@RequestParam(value = "file",required = false) MultipartFile file) throws IOException {
        return new R(true,200, UploadUtil.uploadImage(file),"存储到阿里云Oss");
    }
    //删除oss bucket里面的图片
    @PostMapping("/deleteFile/{filename}")
    public R deleteFile(@PathVariable String filename){
        System.out.println("要删除的filename is "+filename);//山川cd5b82e008714a2a931908429dbb2791.jpg
        return new R(true,200, UploadUtil.deleteFile(filename),"删除成功");
    }
}
