package com.tsherpa.pro05.per;

import com.tsherpa.pro05.entity.Report;
import com.tsherpa.pro05.entity.User;
import com.tsherpa.pro05.util.Page;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper
public interface ReportMapper {
    void reportMarInsert(Report report);
    void reportReqInsert(Report report);
    List<Report> reportMarketList(Page page);
    List<Report> reportReqList(Page page);
    int reportTotalMar(Page page);
    int reportTotalReq(Page page);
    int reportCountMar(int marketNo);
    int reportCountReq(int reqNo);
    List<Report> reasonReqList(int reqNo);
    List<Report> reasonMarList(int marketNo);
    List<Report> reportUserList(Page page);
    int reportUserCount(Page page);
    void activeUpdate(User user);
    int reportchkReq(int reqNo, String loginId);
    int reportchkMar(int marketNo,String loginId);
    List<Report> userReportList(String loginId);
    public void reportCancel(int reportId);
}
