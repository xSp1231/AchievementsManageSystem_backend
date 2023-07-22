package com.example.infomanagesystem.controller;

import com.example.infomanagesystem.entity.Notice;
import com.example.infomanagesystem.result.R;
import com.example.infomanagesystem.service.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
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
    @PostMapping("/addNotice")
    public R addNotice(@RequestBody Notice notice){
        if(noticeService.addNotice(notice)){
            return new R(true,200,"notice添加成功");
        }
        else{
            return new R(false,400,"notice添加失败");
        }
    }

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

    @PostMapping("/updateNotice")
    public R updateNotice(@RequestBody Notice notice){
        if(noticeService.updateNotice(notice)){
            return new R(true,200,"notice修改成功");
        }
        else{
            return new R(false,400,"notice修改失败");
        }
    }



}
