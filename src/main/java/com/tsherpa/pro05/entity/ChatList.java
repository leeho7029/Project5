package com.tsherpa.pro05.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatList {

    private Long chatId;
    private String senderId;
    private String sendDate;
    private String message;
    private boolean readYn;
    private Long roomId;

}