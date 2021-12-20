package com.cbkim.apiserver.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.cbkim.apiserver.model.code.ErrorCode;
import com.cbkim.apiserver.model.response.CommonResult;
import com.cbkim.apiserver.service.EmailService;
import com.cbkim.apiserver.service.ResponseService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Api(tags = { "003. Certification" })
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping(value = "/v1")
public class CertificationController {

    private final ResponseService responseService; 
    private final EmailService emailService;
 
    @ApiImplicitParams({
        @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
    })
    @ApiOperation(value = "이메일 인증코드 전송", notes = "이메일 인증코드를 전송한다.")
    @PostMapping(value = "/email")
    public CommonResult sendEmail(HttpServletRequest request, @ApiParam(value = "이메일") @RequestParam String email) throws Exception {

        try {
            HttpSession session = request.getSession();
            return responseService.getSingleResult(emailService.sendEmail(session, email));
        }
        catch (Exception e) {
            e.printStackTrace();
            log.error("Fail send email :", e);
            return responseService.getFailResult(ErrorCode.FAILURE.getStatus(), e.getMessage());
        }
    }

    @ApiImplicitParams({
        @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
    })
    @ApiOperation(value = "이메일 인증코드 확인", notes = "이메일 인증코드을 확인한다.")
    @PostMapping(value = "/email/certification")
    public CommonResult emailCertification(
        HttpServletRequest request, @ApiParam(value = "이메일") 
        @RequestParam String email, @ApiParam(value = "인증코드")
        @RequestParam String inputCode) throws Exception {
        
        try {
            HttpSession session = request.getSession();            
            return responseService.getSingleResult(emailService.emailCertification(session, email, Integer.parseInt(inputCode)));
        }
        catch (Exception e) {
            e.printStackTrace();
            log.error("Fail certify email :", e);
            return responseService.getFailResult(ErrorCode.FAILURE.getStatus(), e.getMessage());
        }
    }
}
