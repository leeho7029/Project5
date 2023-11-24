package com.tsherpa.pro05.biz;

import com.tsherpa.pro05.entity.DetailVO;
import com.tsherpa.pro05.entity.MainVO;
import com.tsherpa.pro05.entity.Market;
import com.tsherpa.pro05.per.MainphotoMapper;
import com.tsherpa.pro05.per.MarketMapper;
import com.tsherpa.pro05.per.PhotosMapper;
import com.tsherpa.pro05.per.UserMapper;
import com.tsherpa.pro05.util.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MarketService {
    @Autowired
    private MarketMapper marketMapper;
    @Autowired
    private PhotosMapper photosMapper;
    @Autowired
    private MainphotoMapper mainphotoMapper;
    @Autowired
    private UserMapper userMapper;
    @Transactional
    public void marketInsert(Market market) throws Exception {
        marketMapper.marketInsert(market);
        photosMapper.photosInsert(market);
        mainphotoMapper.mainphotoInsert(market);
    }
    public List<MainVO> mainVOList(Page page) throws Exception{
       return marketMapper.mainVOList(page);
    }
    public DetailVO detailVOList(int marketNo) throws Exception{
        return marketMapper.detailVOList(marketNo);
    }
    public MainVO chatVOList(int marketNo) throws Exception{
        return marketMapper.chatVOList(marketNo);
    }
    public MainVO mainlistForDetailVOList(int marketNo) throws Exception{
        return marketMapper.mainlistForDetailVOList(marketNo);
    }
    @Transactional
    public void marketDelete(int marketNo) throws Exception{
        marketMapper.marketDelete(marketNo);
        photosMapper.photosDelete(marketNo);
        mainphotoMapper.mainphotoDelete(marketNo);
    }
    public Market marketDetail(int marketNo) throws Exception {
        return marketMapper.marketDetail(marketNo);
    }
    public void readable(int readable,int marketNo){
        marketMapper.readable(readable, marketNo);
    }

    @Transactional
    public void updateActive(int active,int marketNo,String loginId) {
        marketMapper.updateActive(active, marketNo);
        if(active == 1) {
            userMapper.addPt(loginId);
        } else if(active == 0) {
            userMapper.minusPt(loginId);
        }

    }

    public int getMarketCnt() throws Exception {
        return marketMapper.getMarketCnt();
    }
    public List<MainVO> getMarketListForMain() throws Exception {
        return marketMapper.getMarketListForMain();
    }
    @Transactional
    public void marketEdit(Market market) throws Exception{
        marketMapper.marketEdit(market);
        if(market.getFileInfoList().get(0).getSaveFolder() != null && market.getMainphotoList().get(0).getSaveFolder()!= null){
            photosMapper.photosDelete(market.getMarketNo());
            mainphotoMapper.mainphotoDelete(market.getMarketNo());
            photosMapper.photosInsert(market);
            mainphotoMapper.mainphotoInsert(market);
        }

    }
    public List<MainVO> getInfo(String loginId) throws Exception {
        return marketMapper.getInfo(loginId);
    }
    public int cntSell(String loginId) {
        return marketMapper.cntSell(loginId);
    }
    public List<MainVO> userMainVOList(String loginId) {
        return marketMapper.userMainVOList(loginId);
    }
    public List<MainVO> likeMarketList(String loginId) {
        return marketMapper.likeMarketList(loginId);
    }
    public int mainListCount(Page page){ return marketMapper.mainListCount(page); }
    public int getMarCount(Page page){ return marketMapper.getMarCount(page); }
}
