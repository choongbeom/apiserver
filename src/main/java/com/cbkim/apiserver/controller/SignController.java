package com.cbkim.apiserver.controller;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cbkim.apiserver.config.security.JwtTokenProvider;
import com.cbkim.apiserver.entity.Users;
import com.cbkim.apiserver.model.code.ErrorCode;
import com.cbkim.apiserver.model.response.CommonResult;
import com.cbkim.apiserver.service.ResponseService;
import com.cbkim.apiserver.service.UserService;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Api(tags = { "001. Sign" })
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping(value = "/v1")
public class SignController {
    
    private final ResponseService responseService; 
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;
    
    @ApiOperation(value = "세션 정상 여부 확인", notes = "세션 정상 여부 확인")
    @PostMapping(value = "/check/signin")
    public CommonResult checkSignin( @ApiParam(value = "token") @RequestParam String token,
                                    @ApiParam(value = "returnUrl") @RequestParam String returnUrl) throws Exception {
 
        HashMap<String, String> retrunData = new HashMap<String, String>();
        String signinYn = "N";
        
        retrunData.put("returnUrl", returnUrl);

        try {
            // 토큰 검증하고 
            if (token != null && jwtTokenProvider.validateToken(token)) 
            {
                Authentication auth = jwtTokenProvider.getAuthentication(token);
                
                Users user = userService.findByUserId(auth.getName());
                if(!user.getStatus().getCode().equals("USC001")) return responseService.getFailResult(ErrorCode.NOT_USING_ACCOUNT.getStatus(), ErrorCode.NOT_USING_ACCOUNT.getMessage());

                SecurityContextHolder.getContext().setAuthentication(auth);
                signinYn = "Y";
            }

        } catch (Exception e) {
            log.error("checkSignin : " + e);
        }
        
        retrunData.put("signinYn", signinYn);
        return responseService.getSingleResult(retrunData);
    }

    @ApiOperation(value = "로그인", notes = "회원 로그인을 한다.")
    @PostMapping(value = "/signin")
    public CommonResult signin(
            HttpServletRequest request, 
            HttpServletResponse response,
            @ApiParam(value = "회원ID", required = true) @RequestParam String userId,
            @ApiParam(value = "비밀번호", required = true) @RequestParam String userPw, 
            @ApiParam(value = "자동로그인여부") @RequestParam Optional <String> autoSigninYn) throws Exception {
        
            HashMap<String, String> userData = new HashMap<String, String>();
            String sautoSigninYn = autoSigninYn.orElse("");
        try {

            userData = userService.signin(userId, userPw, sautoSigninYn);
            
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Fail signin :", e);

            userData.put("TOKEN", "");
            userData.put("CODE",    ErrorCode.FAILURE_SIGNIN.getCode());
            userData.put("MESSAGE", ErrorCode.FAILURE_SIGNIN.getMessage() + " " + e.getMessage());
        }

        return responseService.getSingleResult(userData);
    }

    @ApiOperation(value = "회원가입", notes = "회원가입을 한다.")
    @PostMapping(value = "/signup")
    public CommonResult signup(
        @ApiParam(value = "회원ID : 이메일", required = true) @RequestParam String userId,
        @ApiParam(value = "비밀번호", required = true) @RequestParam String userPw,
        @ApiParam(value = "이름", required = true) @RequestParam String name,
        @ApiParam(value = "닉네임") @RequestParam Optional<String> nickName,
        @ApiParam(value = "추천인코드") @RequestParam Optional<String> recommentdationCode,
        @ApiParam(value = "이용약관 동의 여부") @RequestParam String termsConditionsYn,
        @ApiParam(value = "개인정보 수집 및 이용에 대한 동의 여부") @RequestParam String usePersonalInformationYn,
        @ApiParam(value = "마케팅 정보 수신 동의 여부 - 이메일") @RequestParam String receiveMarketingEmailYn,
        @ApiParam(value = "마케팅 정보 수신 동의 여부 - 문자") @RequestParam String receiveMarketingSmsYn,
        @ApiParam(value = "마케팅 정보 수신 동의 여부 - 카카오톡") @RequestParam String receiveMarketingKakaotalkYn,
        @ApiParam(value = "userIdx") @RequestParam Optional<String> userIdx) throws Exception {
        
            HashMap<String, String> userData = new HashMap<String, String>();

            String snickName = nickName.orElse(""); // 닉네임 필수 아님
            String srecommentdationCode = recommentdationCode.orElse("");
            String suserIdx = userIdx.orElse("");
        try {

            userData = userService.signup(  userId, userPw, name, snickName, srecommentdationCode, 
                                            termsConditionsYn, usePersonalInformationYn, receiveMarketingEmailYn, 
                                            receiveMarketingSmsYn, receiveMarketingKakaotalkYn, suserIdx);
            
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Fail signup :", e);

            userData.put("TOKEN", "");
            userData.put("CODE",    ErrorCode.FAILURE_SIGNUP.getCode());
            userData.put("MESSAGE", ErrorCode.FAILURE_SIGNUP.getMessage() + " " + e.getMessage());
        }

        return responseService.getSingleResult(userData);
    }
}
