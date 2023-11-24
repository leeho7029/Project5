package com.tsherpa.pro05.ctrl;

import com.tsherpa.pro05.biz.*;
import com.tsherpa.pro05.entity.*;
import com.tsherpa.pro05.util.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class AdminCtrl {

    @Autowired
    private NoticeService noticeService;

    @Autowired
    private QnaService qnaService;

    @Autowired
    private ReportService reportService;

    @Autowired
    private UserService userService;

    @Autowired
    private MarketService marketService;

    @Autowired
    private RequestService requestService;

    @GetMapping("/admin/dashboard")
    public String getDashboard() {
        return "admin/adminDashboard";
    }

    @GetMapping("/admin/userList")
    public String getuserList(HttpServletRequest request, Model model){

        int curPage = request.getParameter("page") != null ? Integer.parseInt(request.getParameter("page")) : 1;

        Page page = new Page();
        page.setType(request.getParameter("type"));
        page.setKeyword(request.getParameter("keyword"));

        int total = userService.getCount(page);
        page.makeBlock(curPage, total);
        page.makeLastPageNum(total);
        page.makePostStart(curPage, total);

        List<User> list = userService.userList(page);

        model.addAttribute("list", list);
        model.addAttribute("curPage", curPage);
        model.addAttribute("page", page);

        return "admin/userMgmt";
    }

    @GetMapping("/admin/noticeAdmin")
    public String getNoticeList(HttpServletRequest request, Model model){
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

        return "admin/noticeMgmt";
    }

    @GetMapping("/admin/noticeInsert")
    public String NoticeInsertForm(Notice notice, Model model) {
        return "admin/noticeInsert";
    }

    @PostMapping("/admin/noticeInsert")
    public String NoticeInsertPro(Notice param) {
        noticeService.noticeInsert(param);
        return "redirect:/admin/noticeAdmin";
    }

    @GetMapping("/admin/noticeUpdate")
    public String noticeUpdateForm(@RequestParam("no") int no, Model model){
        Notice notice = noticeService.getNotice(no);
        model.addAttribute("notice", notice);
        return "/admin/noticeUpdate";
    }

    @PostMapping("/admin/noticeUpdate")
    public String noticeUpdate(Notice param, Model model){
        noticeService.noticeUpdate(param);
        return "redirect:/admin/noticeAdmin";
    }

    @GetMapping("/admin/noticeDelete")
    public String noticeDelete(@RequestParam("no") int no, Model model){
        noticeService.noticeDelete(no);
        return "redirect:/admin/noticeAdmin";
    }

    @GetMapping("/admin/questionList")
    public String questionList(HttpServletRequest request, Model model){

        int curPage = request.getParameter("page") != null ? Integer.parseInt(request.getParameter("page")) : 1;
        Page page = new Page();
        int total = qnaService.noAnswerCount(page);

        page.makeBlock(curPage, total);
        page.makeLastPageNum(total);
        page.makePostStart(curPage, total);
        model.addAttribute("curPage", curPage);     // 현재 페이지
        model.addAttribute("page", page);           // 페이징 데이터

        //QnaList
        List<Qna> noAnswerList = qnaService.noAnswerList(page);
        model.addAttribute("noAnswerList", noAnswerList);     //QnA 목록
        return "/admin/qnaMgmt";
    }

    @GetMapping("/admin/answerInsert")
    public String getAnswerInsert(HttpServletRequest request, Model model) {
        int qno = Integer.parseInt(request.getParameter("qno"));
        Qna detail = qnaService.qnaDetail(qno);
        model.addAttribute("detail", detail);
        return "admin/qnaAnswer";
    }


    @PostMapping("/admin/answerInsert")
    public String getAnswerInsertPro(Qna qna, HttpServletRequest request, Model model) {
        qnaService.answerInsert(qna);
        return "redirect:/admin/questionList";
    }

    @GetMapping("/admin/reportList")
    public String getReportList(HttpServletRequest request, Model model) {

        int curPage = request.getParameter("page") != null ? Integer.parseInt(request.getParameter("page")) : 1;

        Page page = new Page();
        int total = reportService.reportTotalReq(page);

        page.makeBlock(curPage, total);
        page.makeLastPageNum(total);
        page.makePostStart(curPage, total);
        model.addAttribute("curPage", curPage);     // 현재 페이지
        model.addAttribute("page", page);

        List<Report> market = reportService.reportMarketList(page);
        model.addAttribute("market", market);

        Page page2 = new Page();
        int total2 = reportService.reportTotalMar(page2);

        page2.makeBlock(curPage, total2);
        page2.makeLastPageNum(total2);
        page2.makePostStart(curPage, total2);
        model.addAttribute("curPage", curPage);     // 현재 페이지
        model.addAttribute("page2", page2);

        List<Report> req = reportService.reportReqList(page2);
        model.addAttribute("req", req);

        return "admin/reportMgmt";
    }

    @GetMapping("/admin/reportMarDetail")
    public String reportMarDetail(@RequestParam("marketNo") int marketNo, Model model) throws Exception {

        Market market = marketService.marketDetail(marketNo);
        model.addAttribute("market", market);

        List<Report> list = reportService.reasonMarList(marketNo);
        model.addAttribute("list", list);

        return "admin/reportMarDetail";
    }

    @GetMapping("/admin/reportReqDetail")
    public String reportReqDetail(@RequestParam("reqNo") int reqNo, Model model) throws Exception {

        Request request = requestService.requestDetail(reqNo);
        model.addAttribute("request", request);

        List<Report> list = reportService.reasonReqList(reqNo);
        model.addAttribute("list", list);


        return "admin/reportReqDetail";
    }


    @GetMapping("/admin/reportUser")
    public String reportUserList (HttpServletRequest request, Model model){

        int curPage = request.getParameter("page") != null ? Integer.parseInt(request.getParameter("page")) : 1;

        Page page = new Page();
        int total = reportService.reportUserCount(page);

        page.makeBlock(curPage, total);
        page.makeLastPageNum(total);
        page.makePostStart(curPage, total);
        model.addAttribute("curPage", curPage);     // 현재 페이지
        model.addAttribute("page", page);

        List<Report> list = reportService.reportUserList(page);
        model.addAttribute("list", list);

        return "admin/reportUserMgmt";
    }

    @PostMapping("/admin/activeUpdate")
    @ResponseBody
    public boolean activeUpdatePro(@RequestParam("active") int active, @RequestParam("loginId") String loginId) {

        User user = new User();
        user.setActive(active);
        user.setLoginId(loginId);

        reportService.activeUpdate(user);

        return true;

    }

    @PostMapping("/admin/readableUpdate")
    @ResponseBody
    public boolean readableUpdatePro(@RequestParam("readable") int readable, @RequestParam("reqNo") int reqNo){


        requestService.readable(readable, reqNo);

        return true;

    }

    @PostMapping("/admin/readableUpdateMar")
    @ResponseBody
    public boolean readableUpdateMarPro(@RequestParam("readable") int readable, @RequestParam("marketNo") int marketNo){


        marketService.readable(readable, marketNo);

        return true;

    }


}
