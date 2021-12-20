package com.cbkim.apiserver.config.security;

import java.util.Base64;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import com.cbkim.apiserver.model.code.ErrorCode;
import com.cbkim.apiserver.service.CustomUserDetailService;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Component
@Slf4j
public class JwtTokenProvider { // JWT 토큰을 생성 및 검증 모듈

    @Value("${spring.jwt.secret}")
    private String secretKey;
    private long tokenValidMilisecond = 1000L * 60 * 60 * 24 * 1; // 1일 토근 유효

    private final CustomUserDetailService customUserDetailService;

    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    // Jwt 토큰 생성
    public String createToken(String userPk, List<String> roles) {
        Claims claims = Jwts.claims().setSubject(userPk);
        claims.put("roles", roles);
        Date now = new Date();
        return Jwts.builder()
                .setClaims(claims) // 데이터
                .setIssuedAt(now) // 토큰 발행일자
                .setExpiration(new Date(now.getTime() + tokenValidMilisecond)) // set Expire Time
                .signWith(SignatureAlgorithm.HS256, secretKey) // 암호화 알고리즘, secret값 세팅
                .compact();
    }

    // Jwt 토큰으로 인증 정보를 조회
    public Authentication getAuthentication(String token) {
        UserDetails userDetails = customUserDetailService.loadUserByUsername(this.getUserPk(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    // Jwt 토큰에서 회원 구별 정보 추출
    public String getUserPk(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }

    // Request의 Header에서 token 파싱 : "X-AUTH-TOKEN: jwt토큰"
    public String resolveToken(HttpServletRequest req) {
        return req.getHeader("X-AUTH-TOKEN");
    }

    // Jwt 토큰의 유효성 + 만료일자 확인
    public boolean validateToken(ServletRequest request, String jwtToken) {
        boolean rtn = false;

        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwtToken);

            // 날짜 체크
            rtn = !claims.getBody().getExpiration().before(new Date());
        } catch (ExpiredJwtException e) {
            log.info("ExpiredJwtException");
            request.setAttribute("exception", ErrorCode.EXPIRED_TOKEN.getCode());

        } catch (JwtException e) {
            //log.info("JwtException");
           // e.printStackTrace();
            request.setAttribute("exception", ErrorCode.INVALID_TOKEN.getCode());
        }

        return rtn;
    }

    // Jwt 토큰의 유효성 + 만료일자 확인
    public boolean validateToken(String jwtToken) {
        boolean rtn = false;

        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwtToken);
            rtn = !claims.getBody().getExpiration().before(new Date());
        } catch (ExpiredJwtException e) {
            log.info("ExpiredJwtException");
            e.printStackTrace();
        } catch (JwtException e) {
            log.info("JwtException");
            e.printStackTrace();
        }

        return rtn;
    }

    // Jwt 토큰의 유효성만 체크
    public boolean validateTokenNoDate(String jwtToken) {
        boolean rtn = false;

        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwtToken);
            rtn = true;
        } catch (ExpiredJwtException e) {
            // 날짜가 종료된거면 정상 토큰임
            //log.info("ExpiredJwtException");
            //e.printStackTrace();
            rtn = true;
        } catch (JwtException e) {
            log.info("JwtException");
            e.printStackTrace();
        }

        return rtn;
    }
}
