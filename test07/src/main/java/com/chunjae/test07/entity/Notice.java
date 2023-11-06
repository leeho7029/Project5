package com.chunjae.test07.entity;

import lombok.Data;

import java.util.Date;

@Data
public class Notice {
    private int seq;
    private String title;
    private String id;
    private String content;
    private Date regdate;
}
