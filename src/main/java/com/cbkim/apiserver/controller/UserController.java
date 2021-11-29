package com.cbkim.apiserver.controller;

import com.cbkim.apiserver.dto.UserDto;
import com.cbkim.apiserver.model.code.ErrorCode;
import com.cbkim.apiserver.model.response.CommonResult;
import com.cbkim.apiserver.model.response.SingleResult;
import com.cbkim.apiserver.service.ResponseService;
import com.cbkim.apiserver.service.UserService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Api(tags = { "002. User" })
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping(value = "/v1")
public class UserController {

    private final ResponseService responseService; // 결과를 처리할 Service
    private final UserService userService;
    
    @ApiOperation(value = "회원정보 검색", notes = "회원을 검색한다.")
    @GetMapping("/user")
    public SingleResult<UserDto> getUser(@ApiParam(value = "User Idx") @RequestParam long idx) {        
        try {
            log.info("getUser");
            return responseService.getSingleResult(userService.findByIdx(idx));
                
        } catch (Exception e) {
            e.printStackTrace();
            return responseService.getFailSingleResult(ErrorCode.FAILURE.getStatus(), e.getMessage());
        }
    }

    @ApiImplicitParams({
        @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
    })
    @ApiOperation(value = "회원정보 저장", notes = "회원 정보를 저장한다.")
    @PostMapping("/user")
    public CommonResult insertUser(@ApiParam(value = "User 정보") @RequestBody UserDto userDto) {       
        try {
            log.info("insertUser");
            return responseService.getSingleResult(userService.insert(userDto));
                
        } catch (Exception e) {
            e.printStackTrace();
            return responseService.getFailSingleResult(ErrorCode.FAILURE.getStatus(), e.getMessage());
        }
    }

}
