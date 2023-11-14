package com.tsherpa.team35.per;

import com.tsherpa.team35.entity.Market;
import com.tsherpa.team35.entity.Photos;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper
public interface PhotosMapper {
    public List<Photos> photosList(int no) throws Exception;
    public void photosInsert(Market market) throws Exception;
    public void photosDelete(int no) throws Exception;

}
