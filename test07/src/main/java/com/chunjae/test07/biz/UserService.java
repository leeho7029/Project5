package com.chunjae.test07.biz;

import com.chunjae.test07.domain.UserPrincipal;
import com.chunjae.test07.entity.Role;
import com.chunjae.test07.entity.User;
import com.chunjae.test07.entity.UserRole;
import com.chunjae.test07.persistence.RoleMapper;
import com.chunjae.test07.persistence.UserMapper;
import com.chunjae.test07.persistence.UserRoleMapper;
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
    private UserRoleMapper userRoleMapper;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public User findUserByLoginId(String loginId) {
        return userMapper.findUserByLoginId(loginId);
    }

    public void saveUser(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setActive(1);
        userMapper.setUserInfo(user);
        Role role = roleMapper.getRoleInfo("USER");
        System.out.println(role);
        UserRole userRole = new UserRole();
        System.out.println(user);
        userRole.setRoleId(role.getId());
        User user2 = userMapper.findUserByLoginId(user.getLoginId());
        userRole.setUserId(user2.getId());
        userRoleMapper.setUserRoleInfo(userRole);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userMapper.findUserByLoginId(username);
        return new UserPrincipal(user);
    }
}

