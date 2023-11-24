package com.tsherpa.pro05.biz;

import com.tsherpa.pro05.domain.UserPrincipal;
import com.tsherpa.pro05.entity.User;
import com.tsherpa.pro05.entity.UserVO;
import com.tsherpa.pro05.per.RoleMapper;
import com.tsherpa.pro05.per.UserMapper;
import com.tsherpa.pro05.util.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Date;
import java.util.List;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private JavaMailSender mailSender;


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

    public List<User> userList(Page page) { return userMapper.userList(page); }
    public int getCount(Page page) { return userMapper.getCount(page); }

    //회원 정보 수정
    public void userEdit(User user) {
        userMapper.userEdit(user);
    }

    //패스워드 변경
    public void pwEdit(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
         userMapper.pwEdit(user);
    }


    public void sendTempPasswordEmail(String email, String tempPassword) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("비밀번호 재설정");
        message.setText("귀하의 새로운 임시 비밀번호는 " + tempPassword + " 입니다. 로그인 후 비밀번호를 변경해 주세요.");
        message.setFrom("juncheol08@naver.com");
        mailSender.send(message);

    }
    public String getRamdomPassword(int size) {
        char[] charSet = new char[] {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
                'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
                '!', '@', '#', '$', '%', '^', '&' };
        StringBuffer sb = new StringBuffer();
        SecureRandom sr = new SecureRandom();
        sr.setSeed(new Date().getTime());
        int idx = 0;
        int len = charSet.length;
        for (int i=0; i<size; i++) {
             idx = (int) (len * Math.random());
             idx = sr.nextInt(len);
            // 강력한 난수를 발생시키기 위해 SecureRandom을 사용한다.
             sb.append(charSet[idx]);
             }
             return sb.toString();    }

    public User findId(String email, String tel){
       return userMapper.findId(email, tel);
    }

    public int cntDeal(String loginId) {
        return userMapper.cntDeal(loginId);
    }

    public void addPt(String loginId) {
        userMapper.addPt(loginId);
    }
}