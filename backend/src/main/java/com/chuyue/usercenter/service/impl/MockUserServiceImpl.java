package com.chuyue.usercenter.service.impl;

import com.chuyue.usercenter.common.ErrorCode;
import com.chuyue.usercenter.exception.BusinessException;
import com.chuyue.usercenter.model.domain.MockUser;
import com.chuyue.usercenter.service.MockUserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class MockUserServiceImpl implements MockUserService {

    private final List<MockUser> mockUsers = List.of(
            MockUser.builder().id(1L).username("alice").avatar("src/assets/images/cover7.png").role("regular").build(),
            MockUser.builder().id(2L).username("bob").avatar("src/assets/images/cover8.png").role("regular").build()
    );

    @Override
    public MockUser getUserByUsername(String username) {
        return mockUsers.stream()
                .filter(user -> user.getUsername().equals(username))
                .findFirst()
                .orElse(null);
    }

    @Override
    public MockUser mockUserLogin(String mockUserAccount, String mockUserPassword, HttpServletRequest request) {
        // Checking input field emptiness
        if (StringUtils.isAnyBlank(mockUserAccount, mockUserPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "input field empty");
        }
        // Find the matching user by username using stream API
        Optional<MockUser> userOptional = mockUsers.stream()
                .filter(user -> user.getUsername().equalsIgnoreCase(mockUserAccount))
                .findFirst();
        // Check if user was found
        if (userOptional.isEmpty()) {
            log.info("mock user login failed, no match for account: {}", mockUserAccount);
            throw new BusinessException(ErrorCode.NULL_ERROR, "mock user not found");
        }
        // Retrieve the user from the Optional
        MockUser mockUser = userOptional.get();

        return mockUser;
    }

    @Override
    public List<MockUser> getAllMockUsers() {
        return mockUsers;
    }
}
