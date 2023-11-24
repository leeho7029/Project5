package com.tsherpa.pro05.per;

import com.tsherpa.pro05.entity.User;
import com.tsherpa.pro05.entity.UserVO;
import com.tsherpa.pro05.util.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper
public interface UserMapper {

    User getUserByLoginId(@Param("loginId") String loginId);
    UserVO findUserListByLoginId(@Param("loginId") String loginId);
    int userInsert(@Param("param") User param);
    List<User> userList(Page page);
    int getCount(Page page);

    //회원 정보 수정
    void userEdit(User user);
    //비번 변경
    void pwEdit(User user);
    User findId(String email, String tel);
    int cntDeal(String loginId);

    void addPt(String loginId);
    void minusPt(String loginId);

}