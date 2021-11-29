package com.cbkim.apiserver.service;

import java.util.List;

import com.cbkim.apiserver.config.security.JwtTokenProvider;
import com.cbkim.apiserver.entity.Users;
import com.cbkim.apiserver.model.response.CommonResult;
import com.cbkim.apiserver.model.response.ListResult;
import com.cbkim.apiserver.model.response.SingleResult;
import com.querydsl.core.QueryResults;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class ResponseService {
    @Autowired
    private UserService userService;
    
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    // enum으로 api 요청 결과에 대한 code, message를 정의합니다.
    public enum CommonResponse {
        SUCCESS(0, "성공하였습니다."),
        FAIL(-1, "실패하였습니다.");
        int code;
        String msg;
        CommonResponse(int code, String msg) {
            this.code = code;
            this.msg = msg;
        }
        public int getCode() {
            return code;
        }
        public String getMsg() {
            return msg;
        }
    }

    // 단일건 결과를 처리하는 메소드
    public <T> SingleResult<T> getSingleResult(T data) throws Exception {

        SingleResult<T> result = new SingleResult<>();
        result.setData(data);
        setSuccessResult(result);
        
        // 계정이 유효하면 토큰 재 발행
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(!authentication.getName().equals("anonymousUser"))
        {
            Users user = userService.findByUserId(authentication.getName());
            result.setRetoken(jwtTokenProvider.createToken(String.valueOf(user.getIdx()), user.getRoles()));            
        }
        
        return result;
    }

    // 단일건 결과를 코드로 전달하는 메소드
    public CommonResult getSingleResult(int code, String msg) throws Exception {

        CommonResult result = new CommonResult();
        result.setSuccess(true);
        result.setCode(code);
        result.setMsg(msg);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(!authentication.getName().equals("anonymousUser"))
        {
            Users user = userService.findByUserId(authentication.getName());
            result.setRetoken(jwtTokenProvider.createToken(String.valueOf(user.getIdx()), user.getRoles()));            
        }
        return result;
    }


    // 다중건 결과를 처리하는 메소드
    public <T> ListResult<T> getListResult(List<T> data) throws Exception {
        ListResult<T> result = new ListResult<>();
        result.setData(data);
        setSuccessResult(result);

        // 계정이 유효하면 토큰 재 발행
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(!authentication.getName().equals("anonymousUser"))
        {
            Users user = userService.findByUserId(authentication.getName());
            result.setRetoken(jwtTokenProvider.createToken(String.valueOf(user.getIdx()), user.getRoles()));            
        }

        return result;
    }

    // 다중건 결과를 처리하는 메소드
    public <T> ListResult<T> getPageResult(Page<T> data) throws Exception {
        ListResult<T> result = new ListResult<>();
        result.setData(data.getContent());
        result.setCount(data.getTotalElements());
        setSuccessResult(result);

        // 계정이 유효하면 토큰 재 발행
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(!authentication.getName().equals("anonymousUser"))
        {
            Users user = userService.findByUserId(authentication.getName());
            result.setRetoken(jwtTokenProvider.createToken(String.valueOf(user.getIdx()), user.getRoles()));            
        }

        return result;
    }


    // 다중건 결과를 처리하는 메소드
    public <T> ListResult<T> getQueryResults(QueryResults<T> data) throws Exception {
        ListResult<T> result = new ListResult<>();
        result.setData(data.getResults());
        result.setCount(data.getTotal());
        setSuccessResult(result);

        // 계정이 유효하면 토큰 재 발행
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(!authentication.getName().equals("anonymousUser"))
        {
            Users user = userService.findByUserId(authentication.getName());
            result.setRetoken(jwtTokenProvider.createToken(String.valueOf(user.getIdx()), user.getRoles()));            
        }

        return result;
    } 
    
    // 다중건 결과를 처리하는 메소드
    public <T> ListResult<T> getListCountResult(List<T> data, long count) throws Exception {
        ListResult<T> result = new ListResult<>();
        result.setData(data);
        result.setCount(count);
        setSuccessResult(result);

        // 계정이 유효하면 토큰 재 발행
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(!authentication.getName().equals("anonymousUser"))
        {
            Users user = userService.findByUserId(authentication.getName());
            result.setRetoken(jwtTokenProvider.createToken(String.valueOf(user.getIdx()), user.getRoles()));            
        }

        return result;
    }

    // 다중건 결과를 처리하는 메소드
    public <T> ListResult<T> getPageCountResult(Page<T> data) throws Exception {
        ListResult<T> result = new ListResult<>();
        result.setData(data.getContent());
        result.setCount(data.getTotalElements());
        setSuccessResult(result);

        // 계정이 유효하면 토큰 재 발행
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(!authentication.getName().equals("anonymousUser"))
        {
            Users user = userService.findByUserId(authentication.getName());
            result.setRetoken(jwtTokenProvider.createToken(String.valueOf(user.getIdx()), user.getRoles()));            
        }

        return result;
    }


    // 성공 결과만 처리하는 메소드
    public CommonResult getSuccessResult() throws Exception {
        CommonResult result = new CommonResult();
        setSuccessResult(result);

        // 계정이 유효하면 토큰 재 발행
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(!authentication.getName().equals("anonymousUser"))
        {
            Users user = userService.findByUserId(authentication.getName());
            result.setRetoken(jwtTokenProvider.createToken(String.valueOf(user.getIdx()), user.getRoles()));            
        }

        return result;
    }

    // 실패 결과만 처리하는 메소드
    public CommonResult getFailResult(int code, String msg) {
        CommonResult result = new CommonResult();
        result.setSuccess(false);
        result.setCode(code);
        result.setMsg(msg);
        return result;
    }

    // List형 실패 결과만 처리하는 메소드
    public <T> ListResult<T> getFailListResult(int code, String msg) {

        ListResult<T> result = new ListResult<>();
        result.setData(null);
        result.setCount(0);        
        result.setMsg(msg);
        result.setCode(code);
        return result;
    }
    
    // 단일건 실패 결과를 처리하는 메소드
    public <T> SingleResult<T> getFailSingleResult(int code, String msg) {
        SingleResult<T> result = new SingleResult<>();
        result.setData(null);
        result.setMsg(msg);
        result.setCode(code);
        return result;
    }

    // 결과 모델에 api 요청 성공 데이터를 세팅해주는 메소드
    private void setSuccessResult(CommonResult result) {
        result.setSuccess(true);
        result.setCode(CommonResponse.SUCCESS.getCode());
        result.setMsg(CommonResponse.SUCCESS.getMsg());
    }
    
}
