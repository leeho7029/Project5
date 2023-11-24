package com.tsherpa.pro05.per;

import com.tsherpa.pro05.entity.DetailVO;
import com.tsherpa.pro05.entity.MainVO;
import com.tsherpa.pro05.entity.Market;
import com.tsherpa.pro05.util.Page;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper
public interface MarketMapper {
    public void marketInsert(Market market) throws Exception;
    public List<MainVO> mainVOList(Page page) throws Exception;
    public Market marketDetail(int marketNo) throws Exception;
    public DetailVO detailVOList(int marketNo) throws Exception;
    public MainVO chatVOList(int marketNo) throws Exception;
    public MainVO mainlistForDetailVOList(int marketNo) throws Exception;
    public void marketDelete(int marketNo) throws Exception;
    public void marketEdit(Market market) throws Exception;
    public void readable(int readable,int marketNo);
    public void updateActive(int active,int marketNo);
    public int cntSell(String loginId);
    public List<MainVO> userMainVOList(String loginId);
    public List<MainVO> getInfo(String loginId) throws Exception;
    public int getMarketCnt() throws Exception;
    public List<MainVO> getMarketListForMain() throws Exception;
    public int mainListCount(Page page);
    public List<MainVO> likeMarketList(String loginId);
    public int getMarCount(Page page);


}
