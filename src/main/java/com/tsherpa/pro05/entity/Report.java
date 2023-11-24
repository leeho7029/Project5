package com.tsherpa.pro05.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Report {
    private int reportId;
    private int marketNo;
    private int reqNo;
    private String title;
    private String loginId;
    private String reporter;
    private String reason;
    private Date reportDate;
    private int reportCount;
    private int reasonCount;
    private int reporterCount;
    private String userName;
    private int active;
}
