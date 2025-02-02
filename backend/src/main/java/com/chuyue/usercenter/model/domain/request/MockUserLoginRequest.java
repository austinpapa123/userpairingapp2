package com.chuyue.usercenter.model.domain.request;

import lombok.Data;

import java.io.Serializable;

/**
 * Mock User Login Request Body
 *
 * @author chuyue
 */
@Data
public class MockUserLoginRequest implements Serializable {

    private String mockUserAccount;

    private String mockUserPassword;
}
