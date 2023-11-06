package com.chunjae.test07.entity;

import lombok.Data;

@Data
public class User {
    private int userId;
    private String password;
    private int active;
    private String loginId;
    private String userName;
}
