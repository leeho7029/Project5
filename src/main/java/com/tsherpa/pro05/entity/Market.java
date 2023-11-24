package com.tsherpa.pro05.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Market {
    private int marketNo;
    private String title;
    private int price;
    private String content;
    private String loginId;
    private String active;
    private String conditions;
    private int readable;
    private String regdate;
    private List<Photos> fileInfoList;
    private List<Mainphoto> mainphotoList;
    private String selectedAddress;
    private String detailAddress;
    private double xdata;
    private double ydata;

}
