package com.chunjae.test07.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("shop")
public class ShopController {
    @GetMapping("shopList")
    public String shopList()throws Exception{
        return "shop/shop";
    }
}
