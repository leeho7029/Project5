package com.tsherpa.pro05.per;

import com.tsherpa.pro05.entity.Notice;
import com.tsherpa.pro05.util.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface NoticeMapper {

    List<Notice> getList(Page page);
    Notice getNotice(@Param("no") int no);
    int getCount(Page page);
    void noticeInsert(@Param("param") Notice param);
    void noticeUpdate(@Param("param") Notice param);
    void noticeDelete(int no);
    void cntUpdate(int no);
}
