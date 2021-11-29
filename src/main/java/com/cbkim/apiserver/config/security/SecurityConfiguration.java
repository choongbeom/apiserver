package com.cbkim.apiserver.config.security;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final JwtTokenProvider jwtTokenProvider;

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http

            // https://sas-study.tistory.com/298
            .cors().and()                                                                           // CORS 이슈 해결
            .csrf().disable()                                                                       // rest api이므로 csrf 보안이 필요없으므로 disable처리.
            .authorizeRequests().requestMatchers(CorsUtils::isPreFlightRequest).permitAll().and()   
            .httpBasic().disable()                                                                  // rest api 이므로 기본설정 사용안함. 기본설정은 비인증시 로그인폼 화면으로 리다이렉트 된다.
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)             // jwt token으로 인증할것이므로 세션필요없으므로 생성안함.
            .and()
                .authorizeRequests()                                                    // 다음 리퀘스트에 대한 사용권한 체크            
                    .antMatchers(HttpMethod.GET, "/*/user", "/*/user/**").permitAll()       // USER 조회는 누구나 접근가능
                    .antMatchers(HttpMethod.POST,"/*/user", "/*/user/**").permitAll()       // .hasAnyAuthority("ROLE_ROOT")  // 관리자만 가능
                    .antMatchers(HttpMethod.GET, "/*/check", "/*/check/**").permitAll()     // 조회는 누구나 접근가능
                    .antMatchers(HttpMethod.POST,"/*/check", "/*/check/**").permitAll()     // .hasAnyAuthority("ROLE_ROOT")  // 관리자만 가능
                    .antMatchers(HttpMethod.GET, "/*/signin", "/*/signin/**").permitAll()   // 조회는 누구나 접근가능
                    .antMatchers(HttpMethod.POST,"/*/signin", "/*/signin/**").permitAll()   // .hasAnyAuthority("ROLE_ROOT")  // 관리자만 가능
                    .antMatchers(HttpMethod.GET, "/*/signup", "/*/signup/**").permitAll()   // 조회는 누구나 접근가능
                    .antMatchers(HttpMethod.POST,"/*/signup", "/*/signup/**").permitAll()   // .hasAnyAuthority("ROLE_ROOT")  // 관리자만 가능
                    .anyRequest().hasRole("USER")                                           // 그외 나머지 요청은 모두 인증된 회원만 접근 가능
            .and()
                .exceptionHandling().authenticationEntryPoint(new CustomAuthenticationEntryPoint())
            .and()
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class); // jwt token 필터를 id/password 인증 필터 전에 넣어라.
    }
    
    @Override // ignore check swagger resource
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers("/v2/api-docs", "/swagger-resources/**",
                "/swagger-ui.html", "/webjars/**", "/swagger/**");
    }

    @Override // 인메모리 기반의 인증을 위해 ID/PW를 CBKIM/cbkim 만들어 저장하였고 Role을 USER로 만듬.
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("CBKIM")
                .password(passwordEncoder().encode("cbkim"))
                .roles("USER");
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        List<String> allowedOrigins = new ArrayList<String>();
        allowedOrigins.add("http://localhost:3002");
        allowedOrigins.add("http://localhost:3000");
        allowedOrigins.add("http://localhost:5500");
        allowedOrigins.add("http://localhost:8085");
        
        allowedOrigins.add("http://127.0.0.1:3002");
        allowedOrigins.add("http://127.0.0.1:3000");
        allowedOrigins.add("http://127.0.0.1:5500");
        allowedOrigins.add("http://127.0.0.1:8085");
        
        //allowedOrigins.add("https://www.facebook.com");
        //allowedOrigins.add("https://nid.naver.com");
        //allowedOrigins.add("https://accounts.google.com");
        //allowedOrigins.add("https://kauth.kakao.com");       

        //allowedOrigins.add("https://www.inicis.com");
        //allowedOrigins.add("https://stdpay.inicis.com");
        //allowedOrigins.add("https://stgstdpay.inicis.com");
        
        // todo list 전부 허용하면 안되는데
        //configuration.setAllowedOrigins(allowedOrigins);
        configuration.addAllowedOrigin("*");
        configuration.addAllowedMethod("*");
        configuration.addAllowedHeader("*");
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
    
    @Bean // 패스워드 암호화를 위해 필요하여 Bean으로 선언함.
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
