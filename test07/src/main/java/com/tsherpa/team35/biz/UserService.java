package com.tsherpa.team35.biz;

import com.tsherpa.team35.domain.UserPrincipal;
import com.tsherpa.team35.entity.User;
import com.tsherpa.team35.entity.UserVO;
import com.tsherpa.team35.per.RoleMapper;
import com.tsherpa.team35.per.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;


    public User getUserByLoginId(String loginId) {
        return userMapper.getUserByLoginId(loginId);
    }

    public int saveUser(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setActive(1);
        return userMapper.userInsert(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserVO userVO = userMapper.findUserListByLoginId(username);

        if(userVO == null) {
            throw new UsernameNotFoundException("null");
        }

        return new UserPrincipal(userVO);
    }

}