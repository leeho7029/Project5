package com.tsherpa.pro05.entity;

import lombok.Data;

import java.util.Date;

@Data
public class Notice {
    private int no;
    private String title;
    private String content;
    private String author;
    private Date regdate;
    private int cnt;
}
