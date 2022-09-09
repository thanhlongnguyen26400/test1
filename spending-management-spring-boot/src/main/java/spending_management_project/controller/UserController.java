package spending_management_project.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import spending_management_project.annotation.CurrentUser;
import spending_management_project.component.ResultHelper;
import spending_management_project.domain.NotificationDomain;
import spending_management_project.domain.UserDomain;
import spending_management_project.domain.UserProfileDomain;
import spending_management_project.enums.ErrorType;
import spending_management_project.exception.CommonsException;
import spending_management_project.param.JwtParam;
import spending_management_project.param.NotificationParam;
import spending_management_project.param.UserParam;
import spending_management_project.param.UserPorfileParam;
import spending_management_project.po.User;
import spending_management_project.service.LoginService;
import spending_management_project.tools.Assert;

@RestController
public class UserController {

    @PostMapping(path = "/login")
    private ResponseEntity<?> login(@RequestBody JwtParam jwtParam) {
        try {
            return resultHelper.successResp(loginService.login(jwtParam),HttpStatus.OK);
        } catch (CommonsException e) {
            // Return error information and log the exception.
            return resultHelper
                    .infoResp(logger, e.getErrorType(), e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        } catch (Exception e) {
            return resultHelper.errorResp(logger, e, ErrorType.UNKNOWN, e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(path = "/register")
    private ResponseEntity<?> create(@CurrentUser User currentUser, @RequestBody UserParam param) {
        if (currentUser == null) {
            currentUser = userDomain.findById((long) 0);
        }
        try {
            // Return result and message.
            return resultHelper.successResp(userDomain.create(param, currentUser), HttpStatus.CREATED);
            //      return new ResponseEntity<?<>(userDomain.create(param, currentUser), HttpStatus.CREATED);
        } catch (CommonsException e) {
            // Return error information and log the exception.
            return resultHelper
                    .infoResp(logger, e.getErrorType(), e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        } catch (Exception e) {
            // Return unknown error and log the exception.
            return resultHelper.errorResp(logger, e, ErrorType.UNKNOWN, e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(path = "user/profile/update")
    private ResponseEntity<?> updateProfile(@CurrentUser User user, @RequestBody UserPorfileParam param){
        try {
            // Return result and message.
            return resultHelper.successResp(userProfileDomain.update(param, user), HttpStatus.CREATED);
            //      return new ResponseEntity<?<>(userDomain.create(param, currentUser), HttpStatus.CREATED);
        } catch (Exception e) {
            // Return unknown error and log the exception.
            return resultHelper.errorResp(logger, e, ErrorType.UNKNOWN, e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping(path = "user/profile")
    private ResponseEntity<?> getCurrentUserProfile(@CurrentUser User user){
        try {
            // Return result and message.
            return resultHelper.successResp(userProfileDomain.getById(user.getId()), HttpStatus.OK);
            //      return new ResponseEntity<?<>(userDomain.create(param, currentUser), HttpStatus.CREATED);
        } catch (Exception e) {
            // Return unknown error and log the exception.
            return resultHelper.errorResp(logger, e, ErrorType.UNKNOWN, e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(path = "user/profile/{id}")
    private ResponseEntity<?> getProfile(@PathVariable Long id){
        try {
            // Return result and message.
            return resultHelper.successResp(userProfileDomain.getById(id), HttpStatus.OK);
            //      return new ResponseEntity<?<>(userDomain.create(param, currentUser), HttpStatus.CREATED);
        } catch (Exception e) {
            // Return unknown error and log the exception.
            return resultHelper.errorResp(logger, e, ErrorType.UNKNOWN, e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(path = "/notification")
    private ResponseEntity<?> getNotification(@CurrentUser User user, NotificationParam param){
        try {
            // Return result and message.
            return resultHelper.successResp(notificationDomain.getNotification(user,param), HttpStatus.OK);
            //      return new ResponseEntity<?<>(userDomain.create(param, currentUser), HttpStatus.CREATED);
        } catch (Exception e) {
            // Return unknown error and log the exception.
            return resultHelper.errorResp(logger, e, ErrorType.UNKNOWN, e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(path = "/notification/read")
    private ResponseEntity<?> checkReadNotification(@CurrentUser User user){
        try {
            return resultHelper.successResp(notificationDomain.checkRead(user),HttpStatus.OK);
        }catch (Exception e){
            return resultHelper.errorResp(logger, e, ErrorType.UNKNOWN, e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(path = "user/search")
    private ResponseEntity<?> getSearchUser(@RequestParam("name") String name, @CurrentUser User user){
        try {
            return resultHelper.successResp(userProfileDomain.searchName(name,user.getId()),HttpStatus.OK);
        }catch (Exception e){
            return resultHelper.errorResp(logger, e, ErrorType.UNKNOWN, e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final ResultHelper resultHelper;
    private final LoginService loginService;
    private final UserDomain userDomain;
    private final UserProfileDomain userProfileDomain;
    private final NotificationDomain notificationDomain;

    @Autowired
    public UserController(ResultHelper resultHelper, UserDomain userDomain,
                          LoginService loginService, UserProfileDomain userProfileDomain,
                          NotificationDomain notificationDomain) {
        Assert.defaultNotNull(resultHelper);
        Assert.defaultNotNull(userDomain);
        Assert.defaultNotNull(loginService);
        Assert.defaultNotNull(userProfileDomain);
        Assert.defaultNotNull(notificationDomain);
        this.resultHelper = resultHelper;
        this.userDomain = userDomain;
        this.loginService = loginService;
        this.userProfileDomain = userProfileDomain;
        this.notificationDomain = notificationDomain;
    }
}
