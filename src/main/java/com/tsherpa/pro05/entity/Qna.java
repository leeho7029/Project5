package com.tsherpa.pro05.entity;

import lombok.Data;

import java.util.Date;

@Data
public class Qna {
    private int qno;
    private String title;
    private String content;
    private String author;
    private Date resdate;
    private int lev;
    private int par;
}
