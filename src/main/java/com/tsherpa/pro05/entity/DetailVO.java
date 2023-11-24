package com.tsherpa.pro05.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DetailVO {
    private int marketNo;
    private String title;
    private int price;
    private String content;
    private String loginId;
    private String active;
    private String conditions;
    private String regdate;
    private String selectedAddress;
    private String detailAddress;
    private double xdata;
    private double ydata;
    private int photo_no;
    private String saveFolder;
    private String originFile;
    private String saveFile;
}
