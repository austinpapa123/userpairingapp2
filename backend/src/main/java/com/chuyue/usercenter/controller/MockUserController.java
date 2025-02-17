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


    @Operation(summary = "look up mock user request (not login user)")
    @GetMapping("/lookup/{username}")
    public BaseResponse<MockUser> lookUpMockUser(HttpServletRequest request, @PathVariable String username) {
        System.out.println("current username: " + username);
        MockUser mockUser = mockUserService.getUserByUsername(username);
        if (mockUser == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "username not exist");
        }
        return ResultUtils.success(mockUser);
    }
}
