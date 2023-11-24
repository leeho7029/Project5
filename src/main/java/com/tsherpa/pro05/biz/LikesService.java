package com.tsherpa.pro05.biz;

import com.tsherpa.pro05.entity.Likes;
import com.tsherpa.pro05.per.LikesMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LikesService {

    @Autowired
    private LikesMapper likesMapper;

    public int checkLikedMar(Likes likes) { return likesMapper.checkLikedMar(likes);}
    public int checkLikedReq(Likes likes) { return likesMapper.checkLikedReq(likes); }
    public void removeLikeMar(Likes likes) { likesMapper.removeLikeMar(likes); }
    public void removeLikeReq(Likes likes) { likesMapper.removeLikeReq(likes); }
    public void addLikeMar(Likes likes) { likesMapper.addLikeMar(likes); }
    public void addLikeReq(Likes likes) { likesMapper.addLikeReq(likes); }
    public List<Likes> marketLikeList(String loginId) { return marketLikeList(loginId); }
    public List<Likes> requestLikeList(String loginId) { return requestLikeList(loginId); }

}
