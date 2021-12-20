package com.cbkim.apiserver.controller;

import com.cbkim.apiserver.dto.UserDto;
import com.cbkim.apiserver.model.code.ErrorCode;
import com.cbkim.apiserver.model.response.SingleResult;
import com.cbkim.apiserver.service.ResponseService;
import com.cbkim.apiserver.service.UserService;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Api(tags = { "002. User" })
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/v1")
public class UserController {

    private final ResponseService responseService; // 결과를 처리할 Service
    private final UserService userService;

    @ApiOperation(value = "회원정보 검색", notes = "회원을 검색한다.")
    @GetMapping("/user")
    public SingleResult<UserDto> getUser(@ApiParam(value = "User Idx") @RequestParam long idx) {
        try {
            return responseService.getSingleResult(userService.findByIdx(idx));

        } catch (Exception e) {
            e.printStackTrace();
            return responseService.getFailSingleResult(ErrorCode.FAILURE.getStatus(), e.getMessage());
        }
    }

    @ApiImplicitParams({
        @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
    })
    @ApiOperation(value = "내 정보 조회", notes = "내 정보 조회")
    @GetMapping(value = "/user/profile")
    public SingleResult<UserDto> findUserProfile() {

        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            return responseService.getSingleResult(userService.findUserProfile(authentication.getName()));

        } catch (Exception e) {
            e.printStackTrace();
            return responseService.getFailSingleResult(ErrorCode.FAILURE.getStatus(), e.getMessage());
        }
    }
}
