package com.chunjae.test07.domain;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;

public class UserFailLogin extends SimpleUrlAuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {

        String errorType = "";
        String errorMessage = "";

        if (exception instanceof BadCredentialsException){

            errorType = "error01";
            errorMessage="아이디 또는 비밀번호가 맞지 않습니다.";

        } else if(exception instanceof InternalAuthenticationServiceException) {

            errorType = "error02";
            errorMessage="아이디 또는 비밀번호가 맞지 않습니다.";

        } else if(exception instanceof UsernameNotFoundException) {

            errorType = "error03";
            errorMessage="아이디 또는 비밀번호가 맞지 않습니다.";

        } else {

            errorType = "error04";
            errorMessage="알수없는 오류";

        }

        //errorMessage= URLEncoder.encode(errorMessage,"UTF-8");
        setDefaultFailureUrl("/login?error=true&exception=" + errorType);
        super.onAuthenticationFailure(request, response, exception);

    }

}