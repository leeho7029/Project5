package com.tsherpa.pro05.ctrl;

import com.tsherpa.pro05.biz.MarketService;
import com.tsherpa.pro05.biz.ReportService;
import com.tsherpa.pro05.biz.RequestService;
import com.tsherpa.pro05.entity.Market;
import com.tsherpa.pro05.entity.Report;
import com.tsherpa.pro05.entity.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

@Controller
public class ReportCtrl {

    @Autowired
    private ReportService reportService;

    @Autowired
    private RequestService requestService;

    @Autowired
    private MarketService marketService;

    @RequestMapping(value = "/report/repCheck", method = RequestMethod.POST)
    @ResponseBody
    public boolean repCheck(HttpServletRequest request, Principal principal) throws Exception {

        String sid = principal != null ? principal.getName() : "";
        int reqNo = Integer.parseInt(request.getParameter("reqNo"));

        int chk = reportService.reportchkReq(reqNo, sid);

        boolean result;
        if(chk == 0) {
            result = true;

        } else {
            result= false;
        }

        return result;
    }

    @RequestMapping(value = "/report/repCheckMar", method = RequestMethod.POST)
    @ResponseBody
    public boolean repCheckMar(HttpServletRequest request, Principal principal) throws Exception {

        String sid = principal != null ? principal.getName() : "";
        int marketNo = Integer.parseInt(request.getParameter("marketNo"));

        int chk = reportService.reportchkMar(marketNo, sid);

        boolean result;
        if(chk == 0) {
            result = true;

        } else {
            result= false;
        }

        return result;
    }

    @GetMapping("/report/getReportMar")
    public String getReportMarForm (@RequestParam("marketNo")int marketNo, Principal principal, Model model) throws Exception {

        String sid = principal != null ? principal.getName() : "";
        Market market = marketService.marketDetail(marketNo);

        model.addAttribute("marketNo", marketNo);
        model.addAttribute("market", market);

        int chk1 = reportService.reportchkMar(marketNo, sid );
        if (chk1 == 0) {
            return "report/reportMarInsert";
        } else {
            model.addAttribute("msg", "이미 신고한 회원입니다.");
            model.addAttribute("url", "/layout/alert");
            return "layout/alert";
        }

    }

    @GetMapping("/report/getReportReq")
    public String getReportReqForm (@RequestParam("reqNo")int reqNo, Principal principal, Model model) throws Exception {

        String sid = principal != null ? principal.getName() : "";
        Request req =requestService.requestDetail(reqNo);

        model.addAttribute("reqNo", reqNo);
        model.addAttribute("req", req);

        int chk1 = reportService.reportchkReq(reqNo, sid );
        if (chk1 == 0) {
            return "report/reportReqInsert";
        } else {
            model.addAttribute("msg", "이미 신고한 회원입니다.");
            model.addAttribute("url", "/layout/alert");
            return "layout/alert";
        }


    }

    @PostMapping("/report/reportMarPro")
    public String reportMarPro (HttpServletRequest request, Principal principal){

        String sid = principal != null ? principal.getName() : "";
        int marketNo = Integer.parseInt(request.getParameter("marketNo"));
        String reporter = request.getParameter("reporter");
        String reason = request.getParameter("reason");
        String title=request.getParameter("title");

        Report report = new Report();
        report.setReporter(reporter);
        report.setLoginId(sid);
        report.setTitle(title);
        report.setReason(reason);
        report.setMarketNo(marketNo);

        reportService.reportMarInsert(report);
        return "report/reportSuc";


    }

    @PostMapping("/report/reportReqPro")
    public String reportReqPro (HttpServletRequest request, Principal principal){

        String sid = principal != null ? principal.getName() : "";
        int reqNo = Integer.parseInt(request.getParameter("reqNo"));
        String reporter = request.getParameter("reporter");
        String reason = request.getParameter("reason");
        String title = request.getParameter("title");

        Report report = new Report();
        report.setReporter(reporter);
        report.setLoginId(sid);
        report.setTitle(title);
        report.setReason(reason);
        report.setReqNo(reqNo);

        reportService.reportReqInsert(report);
        return "report/reportSuc";

    }

    @GetMapping("/report/reportSuc")
    public String reportSuc(){
        return "report/reportSuc";
    }

    @GetMapping("/report/reportCancel")
    public String reportCancel(@RequestParam("reportId") int reportId) {
        reportService.reportCancel(reportId);
        return "redirect:/mypage";
    }

}
