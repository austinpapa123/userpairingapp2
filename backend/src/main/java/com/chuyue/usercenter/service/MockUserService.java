package com.chuyue.usercenter.service;

import com.chuyue.usercenter.model.domain.MockUser;
import jakarta.servlet.http.HttpServletRequest;

public interface MockUserService {

    MockUser getUserByUsername(String username);

    MockUser mockUserLogin(String userAccount, String userPassword, HttpServletRequest request);

}
