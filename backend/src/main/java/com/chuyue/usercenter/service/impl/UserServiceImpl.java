package com.chuyue.usercenter.service.impl;
import java.util.*;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chuyue.usercenter.common.ErrorCode;
import com.chuyue.usercenter.constant.UserConstant;
import com.chuyue.usercenter.exception.BusinessException;
import com.chuyue.usercenter.model.domain.User;
import com.chuyue.usercenter.service.UserService;
import com.chuyue.usercenter.mapper.UserMapper;
import com.chuyue.usercenter.utils.EditDistanceAlgorithm;
import com.chuyue.usercenter.utils.UsernameValidator;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.DigestUtils;

/**
* @author chuyue
* @description 针对表【user】的数据库操作Service实现
* @createDate 2024-01-01 10:50:36
*/
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

    @Resource
    private UserMapper userMapper;

    /**
     * Simple encryption with db name as salt
     */
    private static final String SALT = "ayue";

    @Override
    public long userRegister(String userAccount, String userPassword, String userCheckPassword) {
        if(StringUtils.isAnyBlank(userAccount, userPassword, userCheckPassword)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "input field empty");
        }
        if(userPassword.length() < 6){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "password too short, at least 6");
        }
        if(userCheckPassword.length() < 6){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "password too short, at least 6");
        }

        //Checking userAccount format
        if(!UsernameValidator.isValid(userAccount)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "user account name not valid");
        }

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_account", userAccount);
        long count = userMapper.selectCount(queryWrapper);

        //Already registered by some other users if count is not 0
        if(count > 0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "user already exist");
        }

        //Generate the encrypted password
        String encryptedPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
        //Insert newly registered user
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(encryptedPassword);

        boolean insertResult = this.save(user);
        if(!insertResult){
            return -1;
        }

        return user.getId();
    }

    @Override
    public User userLogin(String userAccount, String userPassword, HttpServletRequest request) {
        //1.1 Checking input field emptiness
        if(StringUtils.isAnyBlank(userAccount, userPassword)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "input field empty");
        }
        //1.2 Checking userPassword length
        if(userPassword.length() < 6){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "password length should be at least 6");
        }

        //1.3 Checking userAccount format
        if(!UsernameValidator.isValid(userAccount)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "user account name not valid, containing invalid characters");
        }

        //2. Generate the encrypted password
        String encryptedPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_account", userAccount);
        //todo match the encrypted password
        queryWrapper.eq("user_password", encryptedPassword);

        User user = userMapper.selectOne(queryWrapper);
        //User not exist
        if(user == null){
            log.info("user login failed, no match");
            throw new BusinessException(ErrorCode.NULL_ERROR, "user not found");
        }

        //3. handle returned user data (用户脱敏)
        User handledUser = getProcessedSafeUser(user);

        //4. Record the login status
        request.getSession().setAttribute(UserConstant.USER_LOGIN_STATE, handledUser);

        return handledUser;
    }


    /**
     * handle returned user data (用户脱敏)
     * @param originalUser
     * @return processed safe user
     */
    @Override
    public User getProcessedSafeUser(User originalUser){
        if(originalUser == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "failed during encryption");
        }
        User handledUser = new User();
        handledUser.setId(originalUser.getId());
        handledUser.setUsername(originalUser.getUsername());
        handledUser.setUserAccount(originalUser.getUserAccount());
        handledUser.setAvatarUrl(originalUser.getAvatarUrl());
        handledUser.setGender(originalUser.getGender());
        handledUser.setUserRole(originalUser.getUserRole());
        handledUser.setPhone(originalUser.getPhone());
        handledUser.setEmail(originalUser.getEmail());
        handledUser.setUserStatus(originalUser.getUserStatus());
        handledUser.setCreateTime(new Date());
        handledUser.setProfile(originalUser.getProfile());
        handledUser.setTags(originalUser.getTags());
        return handledUser;
    }

    /**
     * user log out
     * @param request
     * @return no-meaning number
     */
    @Override
    public int userLogout(HttpServletRequest request) {
        //remove user-login-state stored in session
        request.getSession().removeAttribute(UserConstant.USER_LOGIN_STATE);
        return 0;
    }


    /**
     * load all users into memory and then process
     * @param tagNameList
     * @return
     */
    public List<User> searchUsersByTags(List<String> tagNameList){
        if(CollectionUtils.isEmpty(tagNameList)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        //Load all users first
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        List<User> userList = userMapper.selectList(queryWrapper);
        Gson gson = new Gson();
        return userList.stream().filter( user -> {
            String tagsStr = user.getTags();
            if(StringUtils.isBlank(tagsStr)) {
                return false;
            }
            Set<String> tempTagNameSet = gson.fromJson(tagsStr, new TypeToken<Set<String>>(){}.getType());
            for(String tagName : tagNameList) {
                if(!tempTagNameSet.contains(tagName)) {
                    return false;
                }
            }
            return true;
        }).map(this::getProcessedSafeUser).collect(Collectors.toList());
    }

    /**
     * recommend
     * @param num number of users shown
     * @param loginUser the current log-in user
     * @return
     */
    @Override
    public List<User> recommendUserByTags(long num, User loginUser) {
        // Map to store each user and their distance to the logged-in user
        Map<User, Integer> userDistances = new HashMap<>();
        // Get all users, ignore null tags data and only do two cols id+tags
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.isNotNull("tags");
        queryWrapper.select("id","tags");
        List<User> allUsers = this.list(queryWrapper);

        String loginUserTags = loginUser.getTags();
        Gson gson = new Gson();
        List<String> loginUserTagList = gson.fromJson(loginUserTags, new TypeToken<List<String>>() {
        }.getType());

        //Iterate through all users except for logged-in one and fill the user-distance(similarity) map
        for (User user : allUsers) {
            if (!user.getId().equals(loginUser.getId())) { // Exclude the logged-in user itself
                String userTags = user.getTags();
                if(StringUtils.isBlank(userTags)) {
                    continue;
                }
                List<String> userTagList = gson.fromJson(userTags, new TypeToken<List<String>>() {
                }.getType());
                int distance = EditDistanceAlgorithm.minDistanceBetweenTags(loginUserTagList, userTagList);
                userDistances.put(user, distance);
            }
        }
        // Convert map entries to a list and sort by distance
        List<Map.Entry<User, Integer>> sortedEntries = new ArrayList<>(userDistances.entrySet());
        sortedEntries.sort(Map.Entry.comparingByValue());

        // Extract the sorted users
        List<User> sortedUsers = new ArrayList<>();
        // For tracking the number of added users so far
        int i = 0;
        User user = null;
        for (Map.Entry<User, Integer> entry : sortedEntries) {
            if(i > num) {
                break;
            }
            QueryWrapper<User> qw = new QueryWrapper<>();
            qw.eq("id", entry.getKey().getId());
            user = userMapper.selectOne(qw);
            //User not exist
            if(user == null){
                log.info("user search failed, no match");
                throw new BusinessException(ErrorCode.NULL_ERROR, "user not found");
            }
            sortedUsers.add(user);
            i++;
        }
        return sortedUsers;
    }


    @Override
    public Integer updateUser(User user, User loginUser) {
        Long userId = user.getId();
        if(userId == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        //Can only edit your own profile
        if(userId != loginUser.getId()) {
            throw new BusinessException(ErrorCode.NO_AUTH);
        }
        User oldUser = userMapper.selectById(userId);
        if(oldUser == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        return userMapper.updateById(user);
    }

    /**
     * retrieve the current login user
     * @param request
     * @return
     */
    @Override
    public User getLoginUser(HttpServletRequest request) {
        if(request == null) {
            return null;
        }
        Object userObj = request.getSession().getAttribute(UserConstant.USER_LOGIN_STATE);
        if(userObj == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        return (User) userObj;
    }

    /**
     * search users based on given tags from db directly
     * @param tagNameList List of tags
     * @return list of matched users
     */
    @Override
    @Deprecated
    public List<User> searchUsersByTagsBySQL(List<String> tagNameList){
        if(CollectionUtils.isEmpty(tagNameList)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        //Concatenate AND
        for(String tagName : tagNameList) {
            //'tags', the column in user table, as the tagNameList
            queryWrapper.like("tags", tagName);
        }
        //Retrieve matched users
        List<User> userList = userMapper.selectList(queryWrapper);
        return userList.stream().map(this::getProcessedSafeUser).collect(Collectors.toList());
    }

    /**
     * to see if is admin user
     * @param request
     * @return true if is Admin, false otherwise
     */
    @Override
    public boolean isAdmin(HttpServletRequest request){
        //only admin user could search
        Object userObj = request.getSession().getAttribute(UserConstant.USER_LOGIN_STATE);
        User user = (User) userObj;
        //Avoid userobj being null
        if(user == null || user.getUserRole() != UserConstant.ADMIN_ROLE){
            return false;
        }
        return true;
    }

    @Override
    public boolean isAdmin(User loginUser){
        if(loginUser == null || loginUser.getUserRole() != UserConstant.ADMIN_ROLE){
            return false;
        }
        return true;
    }
}





