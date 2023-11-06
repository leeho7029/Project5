package com.chunjae.test07.biz;

import com.chunjae.test07.entity.Notice;
import com.chunjae.test07.persistence.NoticeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoticeService {
    @Autowired
    private NoticeMapper noticeMapper;

    public List<Notice> NoticeList() throws Exception{
        return noticeMapper.NoticeList();
    }
    public Notice NoticeDetail(int seq)throws Exception{
        return noticeMapper.NoticeDetail(seq);
    }
    public void NoticeInsert()throws Exception{
       noticeMapper.NoticeInsert();
    }
    public void NoticeUpdate()throws Exception{
         noticeMapper.NoticeUpdate();
    }
}
