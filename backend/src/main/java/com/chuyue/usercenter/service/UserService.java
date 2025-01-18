package com.chuyue.usercenter.service;

import com.chuyue.usercenter.model.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

/**
* @author chuyue
* @description 针对表【user】的数据库操作Service
* @createDate 2024-01-01 10:50:36
*/
public interface UserService extends IService<User> {
    /**
     * user register func
     * @param userAccount
     * @param userPassword
     * @param userCheckPassword
     * @return
     */
    long userRegister(String userAccount, String userPassword, String userCheckPassword);

    User userLogin(String userAccount, String userPassword, HttpServletRequest request);

    /**
     * handle returned user data (用户脱敏)
     * @param originalUser
     * @return processed safe user
     */
    User getProcessedSafeUser(User originalUser);

    /**
     * user log out
     * @param request
     * @return no-meaning number
     */
    int userLogout(HttpServletRequest request);

    /**
     * search users based on given tag list directly from db
     *
     * @param tagNameList List of tags
     * @return list of matched users
     */
    List<User> searchUsersByTagsBySQL(List<String> tagNameList);


    /**
     * load users into memory to process
     * @param tagNameList
     * @return
     */
    List<User> searchUsersByTags(List<String> tagNameList);

    /**
     * display recommend users based on tags
     * @param num number of users shown
     * @param loginUser the current log-in user
     * @return
     */
    List<User> recommendUserByTags(long num, User loginUser);

    /**
     * update user
     * @param user
     * @param loginUser
     * @return
     */
    Integer updateUser(User user, User loginUser);

    /**
     * retrive current login user
     * @return
     */
    User getLoginUser(HttpServletRequest request);


    /**
     * check to see if admin user
     * @param request
     */
    boolean isAdmin(HttpServletRequest request);

    /**
     * check to see if admin user
     * @param loginUser
     * @return
     */
    boolean isAdmin(User loginUser);
}
