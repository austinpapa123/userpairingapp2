package com.chuyue.usercenter.controller;

import com.chuyue.usercenter.common.BaseResponse;
import com.chuyue.usercenter.common.ErrorCode;
import com.chuyue.usercenter.common.ResultUtils;
import com.chuyue.usercenter.exception.BusinessException;
import com.chuyue.usercenter.model.domain.MockUser;
import com.chuyue.usercenter.model.domain.request.MockUserLoginRequest;
import com.chuyue.usercenter.service.MockUserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/mockusers")
@CrossOrigin(origins = {"http://localhost:5173"}, allowCredentials = "true")
public class MockUserController {

    @Resource
    private final MockUserService mockUserService;

    public MockUserController(MockUserService userService) {
        this.mockUserService = userService;
    }

    @Operation(summary = "retrieve current mock user request")
    @GetMapping("/current/{username}")
    public BaseResponse<MockUser> getCurrentMockUser(HttpServletRequest request, @PathVariable String username) {
        System.out.println("current username: " + username);
        MockUser mockUser = mockUserService.getUserByUsername(username);
        if (mockUser == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "username not exist");
        }
        return ResultUtils.success(mockUser);
    }

    /**
     * mock user login request handler
     * @param mockUserLoginRequest
     * @param request
     * @return the mock user if login succesful
     */
    @Operation(summary = "user attempt mock login request")
    @PostMapping("/login")
    public BaseResponse<MockUser> userLogin(@RequestBody MockUserLoginRequest mockUserLoginRequest, HttpServletRequest request){
        if(mockUserLoginRequest == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "failed retrieving mock login request");
        }
        String mockUserAccount = mockUserLoginRequest.getMockUserAccount();
        String mockUserPassword = mockUserLoginRequest.getMockUserPassword();
        if(StringUtils.isAnyBlank(mockUserAccount, mockUserPassword)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "input field empty");
        }
        MockUser mockUser = mockUserService.mockUserLogin(mockUserAccount, mockUserPassword, request);
        return ResultUtils.success(mockUser);
    }
}
