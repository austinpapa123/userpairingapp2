package com.chuyue.usercenter.model.domain;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName user
 */
@TableName(value ="user")
@Data
public class User implements Serializable {
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * username
     */
    private String username;

    /**
     * user account
     */
    private String userAccount;

    /**
     * profile photo url
     */
    private String avatarUrl;

    /**
     * profile, user short description
     */
    private String profile;

    /**
     * tagNames list json
     */
    private String tags;

    /**
     * gender
     */
    private Integer gender;

    /**
     * user password
     */
    private String userPassword;

    /**
     * phone number
     */
    private String phone;

    /**
     * email address
     */
    private String email;

    /**
     * user status, e.g. banned
     */
    private Integer userStatus;

    /**
     * create time
     */
    private Date createTime;

    /**
     * update time
     */
    private Date updateTime;

    /**
     * 0 - normal user  1 - admin user
     */
    private Integer userRole;

    /**
     * if deleted
     * @TableLogic is for letting mybatis-plus know which column
     * is the one bout logic delete, the config is in .yml file
     * this annotation is needed as suggested in the doc
     *
     * '(since 3.3.0,配置后可以忽略不配置此注解)'
     */
    @TableLogic
    private Integer deleted;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}