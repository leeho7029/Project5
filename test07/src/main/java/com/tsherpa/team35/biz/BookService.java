package com.tsherpa.team35.biz;

import com.tsherpa.team35.entity.Market;
import com.tsherpa.team35.entity.Request;
import com.tsherpa.team35.per.BookMapper;
import com.tsherpa.team35.per.RequestMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BookService {
    @Autowired
    private RequestMapper requestMapper;
    @Autowired
    private BookMapper bookMapper;

    @Transactional
    public void requestInsert(Request request) throws Exception {
        requestMapper.requestInsert(request);
        bookMapper.BookInsert(request);
    }
}
