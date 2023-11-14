package com.tsherpa.team35.per;

import com.tsherpa.team35.entity.Market;
import com.tsherpa.team35.entity.Request;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper
public interface RequestMapper {
    public void requestInsert(Request request) throws Exception;
    public List<Request> requestList() throws Exception;

}
