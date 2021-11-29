package com.cbkim.apiserver.config.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cbkim.apiserver.model.code.ErrorCode;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException ex) throws IOException, ServletException {

        //log.info("commence call");
        String exception = "";
        if(request.getAttribute("exception") != null) exception = request.getAttribute("exception").toString();

        /**
         * 토큰 없는 경우
         */
        if(exception == null) {
            log.info("errorCode = " + ErrorCode.NON_SIGNIN);
            setResponse(response, ErrorCode.NON_SIGNIN);
            return;
        }

        /**
         * 토큰 만료된 경우
         */
        if(exception.equals(ErrorCode.EXPIRED_TOKEN.getCode())) {
            log.info("errorCode = " + ErrorCode.EXPIRED_TOKEN);
            setResponse(response, ErrorCode.EXPIRED_TOKEN);
            return;
        }

        /**
         * 토큰 시그니처가 다른 경우
         */
        if(exception.equals(ErrorCode.INVALID_TOKEN.getCode())) {
            log.info("errorCode = " + ErrorCode.INVALID_TOKEN);
            setResponse(response, ErrorCode.INVALID_TOKEN);
        }
    }
   
    /**
     * 한글 출력을 위해 getWriter() 사용
     */
    private void setResponse(HttpServletResponse response, ErrorCode errorCode) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().println("{ \"msg\" : \"" + errorCode.getMessage()
                + "\", \"code\" : \"" +  errorCode.getCode()
                + "\", \"success\" : false"
                + ", \"retoken\" : null }");
    }
}