package com.tsherpa.team35.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Market {
    private int market_no;
    private String title;
    private int price;
    private String content;
    private String loginId;
    private String active;
    private String conditions;
    private String regdate;
    private List<Photos> fileInfoList;
}
