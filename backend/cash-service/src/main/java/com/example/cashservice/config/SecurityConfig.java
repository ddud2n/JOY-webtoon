package com.example.cashservice.config;


import com.example.cashservice.security.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity( //보안 어노테이션
        securedEnabled = true, //@Secured 가 붙은 클래스나 인터페이스의 메소드 액세스 제한
        jsr250Enabled = true, //@RolesAllowed
        prePostEnabled = true //@PreAuthorize
)

public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public JwtAuthenticationFilter JwtAuthenticationFilter(){
        return new JwtAuthenticationFilter();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors() //Cross Origin Resource Sharing
                .and()
                .csrf()
                .disable() //rest api이므로 csrf 보안이 필요 없으므로 disable 처리
                .exceptionHandling() //예외처리
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // jwt token으로 인증하므로 세션은 필요없으므로 생성안함
                .and()
                .authorizeRequests() //다음 리퀘스트에 대한 사용권한 체크
                .antMatchers("/actuator/**").permitAll()
                .anyRequest()
                .permitAll(); //그 외 나머지 요청은 모두 인증된 회원만 접근가능

    }


}
