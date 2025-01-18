package com.chuyue.usercenter.model.domain.request;

import lombok.Data;

import java.io.Serializable;

/**
 * User Login Request Body
 *
 * @author chuyue
 */
@Data
public class UserLoginRequest implements Serializable {

    private String userAccount;

    private String userPassword;
}
