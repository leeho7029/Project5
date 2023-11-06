package com.chunjae.test07.persistence;

import com.chunjae.test07.entity.Notice;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper
public interface NoticeMapper {
    public List<Notice> NoticeList() throws Exception;
    public Notice NoticeDetail(@Param("seq") int seq) throws Exception;
    public void NoticeInsert() throws Exception;
    public void NoticeUpdate() throws Exception;
}
