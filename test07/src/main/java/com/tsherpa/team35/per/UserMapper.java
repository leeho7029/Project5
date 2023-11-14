package com.tsherpa.team35.per;

import com.tsherpa.team35.entity.User;
import com.tsherpa.team35.entity.UserVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

@Component
@Mapper
public interface UserMapper {

    User getUserByLoginId(@Param("loginId") String loginId);
    UserVO findUserListByLoginId(@Param("loginId") String loginId);
    int userInsert(@Param("param") User param);

}