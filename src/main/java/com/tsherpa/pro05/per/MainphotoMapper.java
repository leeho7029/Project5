package com.tsherpa.pro05.per;

import com.tsherpa.pro05.entity.Mainphoto;
import com.tsherpa.pro05.entity.Market;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper
public interface MainphotoMapper {
    public List<Mainphoto> mainphotoList(int marketNo) throws Exception;
    public void mainphotoInsert(Market market) throws Exception;
    public void mainphotoDelete(int marketNo) throws Exception;
    public void mainphotoEdit(Market market) throws Exception;
}
