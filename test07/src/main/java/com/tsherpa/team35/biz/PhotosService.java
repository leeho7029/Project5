package com.tsherpa.team35.biz;

import com.tsherpa.team35.per.PhotosMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PhotosService {
    @Autowired
    private PhotosMapper photosMapper;

}
