package com.chuyue.usercenter.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.chuyue.usercenter.common.BaseResponse;
import com.chuyue.usercenter.common.ErrorCode;
import com.chuyue.usercenter.common.ResultUtils;
import com.chuyue.usercenter.constant.RecommendConstant;
import com.chuyue.usercenter.constant.UserConstant;
import com.chuyue.usercenter.exception.BusinessException;
import com.chuyue.usercenter.model.domain.User;
import com.chuyue.usercenter.model.domain.request.UserLoginRequest;
import com.chuyue.usercenter.model.domain.request.UserRegisterRequest;
import com.chuyue.usercenter.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * encapsulate the user api for front end
 *
 * @RestController annotation includes @ResponseBody
 * so the response would be in json format(all functions)
 *
 * 'widely used for restful style api'
 */
@RestController
@RequestMapping("/user")
@Tag(name = "User Parameters")
@CrossOrigin(origins = {"http://localhost:8000", "http://localhost:5173"}, allowCredentials = "true")
public class UserController {

    @Resource
    private UserService userService;

    /**
     * user register request handler
     * @param userRegisterRequest
     * @return user id if register successfully
     */
    @Operation(summary = "new user register request")
    @PostMapping("/register")
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest){
        if(userRegisterRequest == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "register request missing");
        }
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        if(StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "input field empty");
        }

        long result = userService.userRegister(userAccount, userPassword, checkPassword);
        return ResultUtils.success(result);
    }

    @Operation(summary = "retrieve current user request")
    @GetMapping("/current")
    public BaseResponse<User> getCurrentUser(HttpServletRequest request){
        Object userObj = request.getSession().getAttribute(UserConstant.USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        if(currentUser == null){
            throw new BusinessException(ErrorCode.NOT_LOGIN, "need to log in");
        }
        long userId = currentUser.getId();
        // todo verify if valid user
        User user = userService.getById(userId);
        User processedSafeUser = userService.getProcessedSafeUser(user);
        return ResultUtils.success(processedSafeUser);
    }


    /**
     * user login request handler
     * @param userLoginRequest
     * @param request
     * @return the user if login succesful
     */
    @Operation(summary = "user attempt login request")
    @PostMapping("/login")
    public BaseResponse<User> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request){
        if(userLoginRequest == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "failed retrieving login request");
        }
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        if(StringUtils.isAnyBlank(userAccount, userPassword)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "input field empty");
        }

        User user = userService.userLogin(userAccount, userPassword, request);
        return ResultUtils.success(user);
    }

    @Operation(summary = "user logout request")
    @PostMapping("/logout")
    public BaseResponse<Integer> userLogout(HttpServletRequest request){
        if(request == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "input field empty");
        }
        int result = userService.userLogout(request);
        return ResultUtils.success(result);
    }

    /**
     * admin user search for users
     * @param userName
     * @param request
     * @return the result list of users
     */
    @Operation(summary = "admin user search users request")
    @GetMapping("/search")
    public BaseResponse<List<User>> searchUsers(String userName, HttpServletRequest request){
        if(!userService.isAdmin(request)){
            throw new BusinessException(ErrorCode.NO_AUTH);
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if(StringUtils.isNotBlank(userName)){
            queryWrapper.like("username", userName);
        }

        List<User> userList =  userService.list(queryWrapper);
        //return processed safe user info
        List<User> resultUserList = userList.stream().map(user -> userService.getProcessedSafeUser(user)).
                collect(Collectors.toList());
        return ResultUtils.success(resultUserList);
    }

    @Operation(summary = "display recommend users")
    @GetMapping("/recommend")
    public BaseResponse<List<User>> recommendUsers(HttpServletRequest request){
        if(request == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        List<User> userList =  userService.recommendUserByTags(RecommendConstant.DEFAULT_RECOMMEND_NUMBER, loginUser);
        // Return processed safe user info
        List<User> resultUserList = userList.stream().map(user -> userService.getProcessedSafeUser(user)).
                collect(Collectors.toList());
        return ResultUtils.success(resultUserList);
    }

    @Operation(summary = "display all users(no usage so far)")
    @GetMapping("/all")
    public BaseResponse<List<User>> getAllUsers(String userName, HttpServletRequest request){
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        List<User> userList =  userService.list(queryWrapper);
        //return processed safe user info
        List<User> resultUserList = userList.stream().map(user -> userService.getProcessedSafeUser(user)).
                collect(Collectors.toList());
        return ResultUtils.success(resultUserList);
    }

    /**
     * Search users based on given tags
     * @param tagNameList
     * @return
     */
    @Operation(summary = "search users based on given tags")
    @GetMapping("/search/tags")
    public BaseResponse<List<User>> searchUserByTags(@RequestParam(required = false) List<String> tagNameList){
        if(CollectionUtils.isEmpty(tagNameList)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        List<User> userList = userService.searchUsersByTags(tagNameList);
        return ResultUtils.success(userList);
    }

    /**
     * admin user delete users
     * @param id
     * @param request
     * @return true if deleted, false otherwise
     */
    @Operation(summary = "admin users delete regular users request")
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteUsers(@RequestBody long id, HttpServletRequest request){
        if(!userService.isAdmin(request)){
            throw new BusinessException(ErrorCode.NO_AUTH, "request from non admin user");
        }
        if(id <= 0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "id invalid");
        }
        boolean result = userService.removeById(id);
        return ResultUtils.success(result);
    }

    @PostMapping("/update")
    public BaseResponse<Integer> updateUser(@RequestBody User user, HttpServletRequest request) {
        if(user == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        Integer result = userService.updateUser(user, loginUser);
        return ResultUtils.success(result);

    }
}
