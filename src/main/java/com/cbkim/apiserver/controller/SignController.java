package com.cbkim.apiserver.controller;

import java.util.HashMap;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cbkim.apiserver.config.security.JwtTokenProvider;
import com.cbkim.apiserver.entity.Users;
import com.cbkim.apiserver.model.SignResult;
import com.cbkim.apiserver.model.code.ErrorCode;
import com.cbkim.apiserver.model.response.CommonResult;
import com.cbkim.apiserver.model.sns.ProfileApiResponse;
import com.cbkim.apiserver.service.ResponseService;
import com.cbkim.apiserver.service.SNSService;
import com.cbkim.apiserver.service.UserService;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.google.gson.Gson;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
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
    private final SNSService snsService;

    @ApiOperation(value = "세션 정상 여부 확인", notes = "세션 정상 여부 확인")
    @PostMapping(value = "/check/signin")
    public CommonResult checkSignin(@ApiParam(value = "token") @RequestParam String token,
                                    @ApiParam(value = "returnUrl") @RequestParam String returnUrl) throws Exception {

        HashMap<String, String> retrunData = new HashMap<String, String>();
        String signinYn = "N";

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

        }
        catch (Exception e) {
            log.error("checkSignin : " + e);
        }

        retrunData.put("RETURNURL", returnUrl);
        retrunData.put("SIGNINYN", signinYn);

        return responseService.getSingleResult(retrunData);
    }

    @ApiOperation(value = "로그인", notes = "회원 로그인을 한다.")
    @PostMapping(value = "/signin")
    public CommonResult signin(
            HttpServletRequest request,
            HttpServletResponse response,
            @ApiParam(value = "회원ID(이메일)", required = true) @RequestParam String userId,
            @ApiParam(value = "비밀번호", required = true) @RequestParam String userPw,
            @ApiParam(value = "자동로그인여부") @RequestParam Optional <String> autoSigninYn) throws Exception {

            SignResult signResult = new SignResult();
            String sautoSigninYn = autoSigninYn.orElse("");

        try {

            signResult = userService.signin(userId, userPw, sautoSigninYn);

        }
        catch (Exception e) {
            e.printStackTrace();
            log.error("Fail signin :", e);

            signResult.setMessage(e.getMessage());
            signResult.setCode(ErrorCode.FAILURE_SIGNIN.getCode());
        }

        if (signResult.getCode().equals(ErrorCode.SUCCESS.getCode())) {
            return responseService.getSingleResult(signResult);
        }
        else {
            return responseService.getFailResult(Integer.parseInt(signResult.getCode()), signResult.getMessage());
        }
    }

    // 대부분의 커머스 사이트는 기본 회원정보 입력 후 휴대폰인증 또는 휴대폰인증 후 회원정보입력을 진행함.
    // 휴대폰인증(PASS 등등..)을 통해 정상 인증 시 획득되는 사용자 정보를 바탕으로 DB에서 정보를 검색하여 사용함.
    // 인증은
    @ApiOperation(value = "회원가입", notes = "회원가입을 한다.")
    @PostMapping(value = "/signup")
    public CommonResult signup(
        @ApiParam(value = "회원ID(이메일)", required = true) @RequestParam String userId,
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

            SignResult signResult = new SignResult();

            String snickName = nickName.orElse(""); // 닉네임 필수 아님
            String srecommentdationCode = recommentdationCode.orElse("");
            String suserIdx = userIdx.orElse("");

        try {

            signResult = userService.signup(  userId, userPw, name, snickName, srecommentdationCode,
                                            termsConditionsYn, usePersonalInformationYn, receiveMarketingEmailYn,
                                            receiveMarketingSmsYn, receiveMarketingKakaotalkYn, suserIdx);

        }
        catch (Exception e) {
            e.printStackTrace();
            log.error("Fail signup :", e);

            signResult.setMessage(e.getMessage());
            signResult.setCode(ErrorCode.FAILURE_SIGNUP.getCode());
        }

        if (signResult.getCode().equals(ErrorCode.SUCCESS.getCode())) {
            return responseService.getSingleResult(signResult);
        }
        else {
            return responseService.getFailResult(Integer.parseInt(signResult.getCode()), signResult.getMessage());
        }
    }

    @ApiOperation(value = "소셜 회원 가입 및 로그인", notes = "소셜 계정 회원가입을 한다.")
    @GetMapping(value = "/signin/provider")
    public CommonResult signinProvider(
            @ApiParam(value = "서비스 제공자(google, kakao, naver)", required = true) @RequestParam String provider,
            HttpServletRequest request,
            HttpServletResponse response) {

        try
        {
            return responseService.getSingleResult(snsService.getAuthorizationUrl(provider));
        }
        catch(Exception e)
        {
            e.printStackTrace();
            log.error("authProvider :", e);
            return responseService.getFailSingleResult(ErrorCode.FAILURE.getStatus(), e.getMessage());
        }
    }

    // 구글 인증 결과로 계정 정보 가져오기
    @ApiOperation(value = "google", notes = "google")
    @GetMapping(value = "/signin/google")
    public CommonResult signinGoogle(
        @ApiParam(value = "google code") @RequestParam String code,
        @ApiParam(value = "google scope") @RequestParam String scope,
        HttpServletRequest request,
        HttpServletResponse response) {

        try
        {
            OAuth2AccessToken accessToken = snsService.getAccessToken("google", scope, code);
            //로그인 사용자 정보를 읽어온다.
            String apiResult = snsService.getGoogleUserProfile(scope, accessToken);
            System.out.println(apiResult);

            Gson gson = new Gson();
            ProfileApiResponse profileApiResponse = gson.fromJson(apiResult, ProfileApiResponse.class);
            String email = profileApiResponse.getEmail();
            String id = profileApiResponse.getId();

            log.info("/signin/google=" + email + ", id=" + id);

            HashMap<String, String> retrunData = new HashMap<String, String>();
            retrunData.put("EMAIL", email);
            retrunData.put("ID", id);
            retrunData.put("TOKEN", accessToken.getAccessToken());

            return responseService.getSingleResult(retrunData);

        }
        catch(Exception e)
        {
            e.printStackTrace();
            log.error("Fail signinGoogle :", e);
            return responseService.getFailSingleResult(ErrorCode.FAILURE.getStatus(), e.getMessage());
        }
    }

    // 네이버 인증 결과로 계정 정보 가져오기
    @ApiOperation(value = "naver", notes = "naver")
    @GetMapping(value = "/signin/naver")
    public CommonResult signinNaver(
        @ApiParam(value = "naver code") @RequestParam String code,
        HttpServletRequest request,
        HttpServletResponse response) {

        try
        {
            OAuth2AccessToken accessToken = snsService.getAccessToken("naver", null, code);

            //로그인 사용자 정보를 읽어온다.
            String apiResult = snsService.getNaverUserProfile(accessToken);
            System.out.println(apiResult);

            Gson gson = new Gson();
            ProfileApiResponse profileApiResponse = gson.fromJson(apiResult, ProfileApiResponse.class);
            String email = profileApiResponse.getResponse().getEmail();
            String id = profileApiResponse.getResponse().getId();

            log.info("/signin/naver email=" + email + ", id=" + id);

            HashMap<String, String> retrunData = new HashMap<String, String>();
            retrunData.put("EMAIL", email);
            retrunData.put("ID", id);
            retrunData.put("TOKEN", accessToken.getAccessToken());

            return responseService.getSingleResult(retrunData);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            log.error("Fail signinNaver :", e);
            return responseService.getFailSingleResult(ErrorCode.FAILURE.getStatus(), e.getMessage());
        }
    }

    // 카카오 인증 결과로 계정 정보 가져오기
    @ApiOperation(value = "kakao", notes = "kakao")
    @GetMapping(value = "/signin/kakao")
    public CommonResult signinKakao(
        @ApiParam(value = "kakao code") @RequestParam String code,
        HttpServletRequest request,
        HttpServletResponse response) {

        try
        {
            OAuth2AccessToken accessToken = snsService.getAccessToken("kakao", null, code);

            //로그인 사용자 정보를 읽어온다.
            String apiResult = snsService.getKakaoUserProfile(accessToken);
            System.out.println(apiResult);

            Gson gson = new Gson();
            ProfileApiResponse profileApiResponse = gson.fromJson(apiResult, ProfileApiResponse.class);
            String email = profileApiResponse.getKakao_account().getEmail();
            String id = profileApiResponse.getId();

            log.info("/signin/kakao email=" + email + ", id=" + id);

            HashMap<String, String> retrunData = new HashMap<String, String>();
            retrunData.put("EMAIL", email);
            retrunData.put("ID", id);
            retrunData.put("TOKEN", accessToken.getAccessToken());

            return responseService.getSingleResult(retrunData);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            log.error("Fail signinKakao :", e);
            return responseService.getFailSingleResult(ErrorCode.FAILURE.getStatus(), e.getMessage());
        }
    }
}
