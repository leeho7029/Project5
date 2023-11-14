package com.tsherpa.team35.biz;

import com.tsherpa.team35.entity.Market;
import com.tsherpa.team35.per.MarketMapper;
import com.tsherpa.team35.per.PhotosMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MarketService {
    @Autowired
    private MarketMapper marketMapper;
    @Autowired
    private PhotosMapper photosMapper;
    @Transactional
    public void marketInsert(Market market) throws Exception {
        marketMapper.marketInsert(market);
        photosMapper.photosInsert(market);
    }

}
