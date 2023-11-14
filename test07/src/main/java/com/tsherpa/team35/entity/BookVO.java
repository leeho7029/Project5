package com.tsherpa.team35.entity;

import lombok.*;

@ToString
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Data
public class BookVO {
    private String title;
    private String link;
    private String image;
    private String author;
    private String discount;
    private String publisher;
    private String pubdate;
    private String isbn;
    private String description;
}