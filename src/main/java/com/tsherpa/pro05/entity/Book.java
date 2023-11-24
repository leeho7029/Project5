package com.tsherpa.pro05.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Book {
    private int book_no;
    private int product_id;
    private String isbn;
    private String image;
    private String title;
    private String author;
    private String publisher;
    private String pubdate;
}
