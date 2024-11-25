package com.example.infomanagesystem.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.infomanagesystem.entity.Notice;
import com.example.infomanagesystem.mapper.NoticeMapper;
import com.example.infomanagesystem.service.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * @Author xushupeng
 * @Date 2023-07-21 14:16
 */
@Service
public class NoticeServiceImpl extends ServiceImpl<NoticeMapper,Notice> implements NoticeService {

    @Autowired
    private NoticeMapper noticeMapper;

    @Override
    public boolean addNotice(Notice notice) {
        return noticeMapper.insert(notice) > 0;
    }
    @Override
    public boolean deleteNoticeById(int id) {
        return noticeMapper.deleteById(id)>0;
    }
    @Override
    public boolean updateNotice(Notice notice) {
        return noticeMapper.updateById(notice)>0;
    }
    @Override
    public List<Notice> getAllNotice() {
        List<Notice> noticeList = noticeMapper.selectList(null);

        // 使用Collections.sort按时间字段进行排序
        noticeList.sort((notice2, notice1) -> notice1.getTime().compareTo(notice2.getTime()));

        return noticeList;
    }
}
