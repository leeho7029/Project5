package com.tsherpa.pro05.ctrl;

import com.tsherpa.pro05.biz.NoticeService;
import com.tsherpa.pro05.entity.Notice;
import com.tsherpa.pro05.util.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.List;

@Controller
public class NoticeCtrl {

    @Autowired
    private NoticeService noticeService;

    @Autowired
    HttpSession session;

    @GetMapping("/notice/list")
    public String NoticeList(HttpServletRequest request, Model model) {

        int curPage = request.getParameter("page") != null ? Integer.parseInt(request.getParameter("page")) : 1;

        Page page = new Page();

        int total = noticeService.getCount(page);
        page.makeBlock(curPage, total);
        page.makeLastPageNum(total);
        page.makePostStart(curPage, total);


        List<Notice> list = noticeService.getList(page);
        model.addAttribute("list", list);
        model.addAttribute("curPage", curPage);
        model.addAttribute("page", page);
        return "notice/noticeList";
    }

    @GetMapping("/notice/detail")
    public String NoticeDetail (@RequestParam("no") int no,  HttpServletRequest request, Principal principal, Model model) {

        String sid = principal != null ? principal.getName() : "";

        session.setAttribute("sid", sid);

        Cookie[] cookieFromRequest = request.getCookies();
        String cookieValue = null;
        for(int i=0; i<cookieFromRequest.length; i++) {
            // 요청 정보로부터 쿠키를 가져옴
            cookieValue = cookieFromRequest[0].getValue();
        }
        // 쿠키 세션 입력
        if(session.getAttribute(no + ":cookie") == null) {
            session.setAttribute(no + ":cookie", no + ":" + cookieValue);
        } else {
            session.setAttribute(no + ":cookie ex", session.getAttribute(no + ":cookie"));
            if(!session.getAttribute(no + ":cookie").equals(no + ":" + cookieValue)) {
                session.setAttribute(no+":cookie", no + ":" + cookieValue);
            }
        }

        // 쿠키와 세션이 없는 경우 조회수 카운트
        if(!session.getAttribute(no + ":cookie").equals(session.getAttribute(no + ":cookie ex"))) {
            noticeService.cntUpdate(no);
        }


        Notice notice = noticeService.getNotice(no);
        model.addAttribute("notice", notice);

        return "notice/noticeDetail";
    }


}
