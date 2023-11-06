package com.chunjae.test07.controller;

import com.chunjae.test07.biz.NoticeService;
import com.chunjae.test07.entity.Notice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("notice")
public class NoticeController {
    @Autowired
    private NoticeService noticeService;

    @GetMapping("noticeList")
    public String noticeList(Model model)throws Exception{
        Notice notice =new Notice();
        List<Notice> noticeList =noticeService.NoticeList();
        model.addAttribute("noticeList",noticeList);

        return "notice/noticeList";
    }
    @GetMapping("noticeInsert")
    public String noticeInsert()throws Exception{
        return "notice/noticeInsert";
    }

    @PostMapping("noticeInsertPro")
    public String noticeInsertPro()throws Exception{
        noticeService.NoticeInsert();

        return "redirect:noticeList";
    }

    @GetMapping("noticeDetail")
    public String noticeDetail(@RequestParam("seq") int seq, Model model)throws Exception{
        Notice notice = noticeService.NoticeDetail(seq);
        model.addAttribute("notice",notice);
        System.out.println(seq);
        return "notice/noticeDetail";
    }

    @GetMapping("noticeUpdate")
    public String noticeUpdate()throws Exception{
        return "notice/noticeUpdate";
    }

    @GetMapping("noticeUpdatePro")
    public String noticeUpdatePro()throws Exception{
        noticeService.NoticeUpdate();

        return "redirect:noticeList";
    }

}
