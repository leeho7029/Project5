package com.tsherpa.team35.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Request {
    private int reqNo;
    private String title;
    private String price;
    private String content;
    private String loginId;
    private String active;
    private String regdate;
    private String addr;

    public void setReqNo(int reqNo) {
        this.reqNo = reqNo;
    }
}


