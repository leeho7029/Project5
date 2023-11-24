package com.tsherpa.pro05.biz;

import com.tsherpa.pro05.entity.Mainphoto;
import com.tsherpa.pro05.per.MainphotoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service

public class MainPhotoService {
    @Autowired
    private MainphotoMapper mainphotoMapper;
    public List<Mainphoto> mainphotoList(int marketNo) throws Exception{
        return mainphotoMapper.mainphotoList(marketNo);
    }
}
