package com.tsherpa.pro05.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private int readable;
    private String regdate;
    private String addr;
    private String isbn;
    private String bookImage;
    private String bookTitle;
    private String bookAuthor;
    private String publisher;
    private String pubdate;
    private String discount;

}


