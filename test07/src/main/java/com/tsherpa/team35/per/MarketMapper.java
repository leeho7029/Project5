package com.tsherpa.team35.per;

import com.tsherpa.team35.entity.Market;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper
public interface MarketMapper {
    public void marketInsert(Market market) throws Exception;
    public List<Market> dataRoomList() throws Exception;
}
