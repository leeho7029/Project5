package com.tsherpa.pro05.ctrl;

import com.tsherpa.pro05.biz.MarketService;
import com.tsherpa.pro05.biz.RequestService;
import com.tsherpa.pro05.biz.UserService;
import com.tsherpa.pro05.entity.MainVO;
import com.tsherpa.pro05.entity.Request;
import com.tsherpa.pro05.util.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeCtrl {

    @Autowired
    private UserService userService;

    @Autowired
    private MarketService marketService;

    @Autowired
    private RequestService requestService;

    @GetMapping( "/")
    public String home(Model model) throws Exception {

        Page page = new Page();
        Integer userNum = userService.getCount(page);
        if(userNum == 0) userNum = 0;
        model.addAttribute("userNum", userNum);

        Integer marketNum = marketService.getMarketCnt();
        if(marketNum == 0) marketNum = 0;
        model.addAttribute("marketNum", marketNum);

        Integer requestNum = requestService.getRequestCnt();
        if(requestNum == 0) requestNum = 0;
        model.addAttribute("requestNum", requestNum);

        List<MainVO> marketList = marketService.getMarketListForMain();
        model.addAttribute("marketList", marketList);

        List<Request> requestList = requestService.getRequestListForMain();
        model.addAttribute("requestList", requestList);


        return "index";
    }

}