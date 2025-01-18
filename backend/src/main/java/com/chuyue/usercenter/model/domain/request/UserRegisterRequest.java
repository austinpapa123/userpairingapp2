package com.chuyue.usercenter.model.domain.request;

import lombok.Data;

import java.io.Serializable;

/**
 * User Register Request Body
 *
 * @author chuyue
 */
@Data
public class UserRegisterRequest implements Serializable {
    private String userAccount;
    private String userPassword;

    private String checkPassword;
}
