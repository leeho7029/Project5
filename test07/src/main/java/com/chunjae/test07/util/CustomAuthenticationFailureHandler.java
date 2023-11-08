package com.chunjae.test07.util;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        // 여기에 실패 시 보여줄 메시지 설정
        String errorMessage = "로그인에 실패하였습니다. 유효한 아이디와 패스워드를 입력해주세요.";

        // 에러 메시지 전달
        request.getSession().setAttribute("error_message", errorMessage);

        // 로그인 페이지로 리디렉션
        response.sendRedirect(request.getContextPath() + "/login");
    }
}