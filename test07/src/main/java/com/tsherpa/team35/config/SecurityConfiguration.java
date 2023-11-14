package com.tsherpa.team35.config;

import com.tsherpa.team35.biz.UserService;
import com.tsherpa.team35.domain.UserFailLogin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private UserService userService;

    // 인증 처리와 암호화
    @Bean
    public DaoAuthenticationProvider authenticationProvider(UserService userService) {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userService);
        authenticationProvider.setPasswordEncoder(bCryptPasswordEncoder);
        return authenticationProvider;
    }

    // 인증 관리자
    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(authenticationProvider(userService));
    }

    @Bean
    public UserFailLogin loginFailHandler(){
        return new UserFailLogin();
    }

    //접근 보안 설정 관리자
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/","/**","/login","/join").permitAll()
                .mvcMatchers("/css/**","/js/**","/image/**","/fonts/**","/clEditor/**", "/lib/**").permitAll()
                .antMatchers("/admin/home").hasAuthority("ADMIN") // ADMIN 권한의 유저만 /home 에 접근가능
                .anyRequest().authenticated()
                .and().formLogin()
                .loginPage("/login")
                .failureHandler(loginFailHandler())
                .defaultSuccessUrl("/")
                .usernameParameter("loginId")
                .passwordParameter("password")
                .and()
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/")
                .and()
                .exceptionHandling()
                .accessDeniedPage("/access-denied");
    }



}