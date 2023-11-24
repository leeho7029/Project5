package com.tsherpa.pro05.biz;

import com.tsherpa.pro05.entity.Report;
import com.tsherpa.pro05.entity.User;
import com.tsherpa.pro05.per.ReportMapper;
import com.tsherpa.pro05.util.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReportService {

    @Autowired
    private ReportMapper reportMapper;

    public void reportMarInsert(Report report) { reportMapper.reportMarInsert(report); }
    public void reportReqInsert(Report report) { reportMapper.reportReqInsert(report); }
    public List<Report> reportMarketList(Page page) { return reportMapper.reportMarketList(page); }
    public List<Report> reportReqList(Page page) { return reportMapper.reportReqList(page); }
    public int reportTotalMar(Page page) { return reportMapper.reportTotalMar(page); }
    public int reportTotalReq(Page page) { return reportMapper.reportTotalReq(page); }
    public int reportCountMar(int marketNo) { return reportMapper.reportCountMar(marketNo); }
    public int reportCountReq(int reqNo) { return reportMapper.reportCountReq(reqNo); }
    public List<Report> reasonReqList(int reqNo) { return reportMapper.reasonReqList(reqNo); }
    public List<Report> reasonMarList(int marketNo) { return reportMapper.reasonMarList(marketNo); }
    public List<Report> reportUserList(Page page){ return reportMapper.reportUserList(page); }
    public int reportUserCount(Page page) { return reportMapper.reportUserCount(page); }
    public void activeUpdate(User user) { reportMapper.activeUpdate(user); }
    public int reportchkReq(int reqNo, String loginId) { return reportMapper.reportchkReq(reqNo, loginId); }
    public int reportchkMar(int marketNo, String loginId) { return reportMapper.reportchkMar(marketNo, loginId); }
    public List<Report> userReportList(String loginId) {return reportMapper.userReportList(loginId);    }
    public void reportCancel(int reportId) { reportMapper.reportCancel(reportId); }
}
