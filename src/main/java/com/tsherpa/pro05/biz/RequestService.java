package com.tsherpa.pro05.biz;

import com.tsherpa.pro05.entity.Request;
import com.tsherpa.pro05.per.RequestMapper;
import com.tsherpa.pro05.per.UserMapper;
import com.tsherpa.pro05.util.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RequestService {
    @Autowired
    private RequestMapper requestMapper;

    @Autowired
    private UserMapper userMapper;

    public void reqInsert(Request request) throws Exception{
        requestMapper.requestInsert(request);
    }
    public List<Request> requestList(Page page) throws Exception{
        return requestMapper.requestList(page);
    }
    public Request requestDetail(int reqNo) throws Exception{
        return requestMapper.requestDetail(reqNo);
    }
    public List<Request> getMoreRequests(int offset, int limit) {
        return requestMapper.getMoreRequests(offset, limit);
    }
    public void requestEdit(Request request) throws Exception {
        requestMapper.requestEdit(request);
    }

    public void requestDelete(int reqNo) throws Exception{
        requestMapper.requestDelete(reqNo);
    }

    public List<Request> allRequest() throws Exception{
        return requestMapper.allRequest();
    }

    public void requestEditAll(Request request) throws Exception {
        requestMapper.requestEditAll(request);
    }

    public void readable(int readable,int reqNo){
        requestMapper.readable(readable, reqNo);
    }


    public List<Request> userRequestList(String loginId) {
        return requestMapper.userRequestList(loginId);
    }

    public int getRequestCnt() throws Exception {
        return requestMapper.getRequestCnt();
    }

    public List<Request> getRequestListForMain() throws Exception {
        return requestMapper.getRequestListForMain();
    }
    public List<Request> likeRequestList(String loginId) {
        return requestMapper.likeRequestList(loginId);
    }

    public List<Request> getInfo(String loginId) throws Exception {
        return requestMapper.getInfo(loginId);
    }

    public int getReqCount(Page page){
        return requestMapper.getReqCount(page);
    }

    @Transactional
    public void updateActive(int active,int reqNo,String loginId) {
        requestMapper.updateActive(active, reqNo);
        if(active == 1) {
            userMapper.addPt(loginId);
        } else if(active == 0) {
            userMapper.minusPt(loginId);
        }
    }

}
