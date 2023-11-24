package com.tsherpa.pro05.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoomVO {

    private Long roomId;
    private int productId;
    private String productTable;
    private String productName = "";
    private String buyerId;
    private String buyerName;
    private String sellerName = "";
    private int buyerActive;
    private String regDate;

}