package com.tsherpa.team35.ctrl;

import com.tsherpa.team35.biz.UserService;
import com.tsherpa.team35.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class UserCtrl {

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public ModelAndView login(@RequestParam(value = "exception", required = false) String exception){

        ModelAndView modelAndView = new ModelAndView();

        modelAndView.addObject("exception", exception);
        modelAndView.setViewName("user/login");
        return modelAndView;
    }

    @GetMapping("/join")
    public ModelAndView join(){

        ModelAndView modelAndView = new ModelAndView();
        User user = new User();

        modelAndView.addObject("user", user);
        modelAndView.setViewName("user/join");
        return modelAndView;
    }

    @PostMapping("/join")
    public ModelAndView joinPro(User user, BindingResult bindingResult) {
        ModelAndView modelAndView = new ModelAndView();
        User userExists = userService.getUserByLoginId(user.getLoginId());
        if (userExists != null) {
            bindingResult
                    .rejectValue("loginId", "error.loginId","사용이 불가한 아이디입니다.");
        }

        if(!user.getPassword().equals(user.getPasswordConfirm())) {
            bindingResult
                    .rejectValue("password", "error.password","비밀번호와 비밀번호 확인이 다릅니다.");
        }

        if (bindingResult.hasErrors()) {
            modelAndView.setViewName("/user/join");
        } else {
            int cnt = userService.saveUser(user);
            if(cnt == 1) {
                modelAndView.setViewName("redirect:/");
            } else {
                modelAndView.setViewName("/user/join");
            }
        }

        return modelAndView;
    }

}