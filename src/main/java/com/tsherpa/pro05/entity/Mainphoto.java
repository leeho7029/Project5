package com.tsherpa.pro05.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Mainphoto {
    private int mainphotoNo;
    private int marketNo;
    private String saveFolder;
    private String originFile;
    private String saveFile;
}
