package com.tsherpa.team35.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Book {
    private int bookNo;
    private int productId;
    private String isbn;
    private String image;
    private String title;
    private String author;
    private String publisher;
    private String pubdate;
    private String discount;
}
