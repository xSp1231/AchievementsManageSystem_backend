package com.example.infomanagesystem.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.infomanagesystem.entity.Notice;

import java.util.List;

/**
 * @Author xushupeng
 * @Date 2023-07-21 14:14
 */
public interface NoticeService extends IService<Notice> {
     boolean addNotice(Notice notice);

     boolean deleteNoticeById(int id);

     boolean updateNotice(Notice notice);

     List<Notice> getAllNotice();

}
