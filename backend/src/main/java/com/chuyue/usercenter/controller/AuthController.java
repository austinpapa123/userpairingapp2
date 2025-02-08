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
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@RestController
@RequestMapping("/auth")
//@CrossOrigin(origins = "http://localhost:5173", exposedHeaders = "Authorization") // Adjust origin as needed
@Slf4j
public class AuthController {

    @Resource
    private MockUserService mockUserService;

    @Operation(summary = "user attempt mock login request")
    @PostMapping("/login")
    public BaseResponse<MockUser> userLogin(@RequestBody MockUserLoginRequest mockUserLoginRequest,
                                            HttpServletRequest request,
                                            HttpServletResponse response) {
        if (mockUserLoginRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "failed retrieving mock login request");
        }
        String mockUserAccount = mockUserLoginRequest.getMockUserAccount();
        String mockUserPassword = mockUserLoginRequest.getMockUserPassword();
        if (StringUtils.isAnyBlank(mockUserAccount, mockUserPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "input field empty");
        }
        // Delegate the login logic to the service class
        MockUser mockUser = mockUserService.mockUserLogin(mockUserAccount, mockUserPassword, request);

        // Generate a token from the user details
        String token = generateToken(mockUser);
        // Add the token to the response header (with a "Bearer " prefix)
        response.setHeader("Authorization", "Bearer " + token);

        // Return the user info inside your custom BaseResponse type
        return ResultUtils.success(mockUser);
    }

    // Simple token generator: converts a JSON-like payload to Base64.
    // In production, use a JWT library (like jjwt) to generate signed tokens.
    private String generateToken(MockUser user) {
        String tokenPayload = "{\"username\":\"" + user.getUsername() + "\",\"role\":\"" + user.getRole() + "\",\"avatar\":\"" + user.getAvatar() + "\"}";
        return Base64.getEncoder().encodeToString(tokenPayload.getBytes(StandardCharsets.UTF_8));
    }
}
