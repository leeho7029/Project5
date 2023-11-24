package com.tsherpa.pro05.per;

import com.tsherpa.pro05.entity.Likes;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface LikesMapper {

    int checkLikedMar(Likes likes);
    int checkLikedReq(Likes likes);
    void removeLikeMar(Likes likes);
    void removeLikeReq(Likes likes);
    void addLikeMar(Likes likes);
    void addLikeReq(Likes likes);
    List<Likes> marketLikeList(String loginId);
    List<Likes> requestLikeList(String loginId);

}
