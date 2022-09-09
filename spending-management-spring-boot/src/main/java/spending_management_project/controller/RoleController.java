package spending_management_project.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import spending_management_project.annotation.CurrentUser;
import spending_management_project.component.ResultHelper;
import spending_management_project.domain.RoleDomain;
import spending_management_project.enums.ErrorType;
import spending_management_project.exception.CommonsException;
import spending_management_project.param.RoleParam;
import spending_management_project.po.User;
import spending_management_project.tools.ErrorMsgHelper;

@RestController
@RequestMapping(path = "/role")
public class RoleController {

    @Autowired
    private RoleDomain roleDomain;

    @Autowired
    private ResultHelper resultHelper;

    private static final Logger logger = LoggerFactory.getLogger(RoleController.class);
    private final static String ROLE = "Role";
    private final static String NAME = "name";

    @PostMapping(path = "/add")
    public ResponseEntity<?> createRole(@CurrentUser User currentUser, @RequestBody RoleParam roleParam) {
        try {
            // Return result and message.
            return resultHelper.successResp(roleDomain.create(roleParam, currentUser), HttpStatus.CREATED);
        } catch (DataIntegrityViolationException e) {
            return resultHelper.infoResp(logger, ErrorType.SYS0111, ErrorMsgHelper.getReturnMsg(ErrorType.SYS0111, ROLE, NAME), HttpStatus.UNPROCESSABLE_ENTITY);
        } catch (CommonsException e) {
            // Return error information and log the exception.
            return resultHelper.infoResp(logger, e.getErrorType(), e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        } catch (Exception e) {
            // Return unknown error and log the exception.
            return resultHelper.errorResp(logger, e, ErrorType.UNKNOWN, e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(path = "/delete/{id}")
    private ResponseEntity<?> deleteRole(@PathVariable Long id) {
        try {
            return resultHelper.successResp(roleDomain.delete(id), HttpStatus.OK);
        } catch (CommonsException e) {
            // Return error information and log the exception.
            return resultHelper.infoResp(logger, e.getErrorType(), e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        } catch (Exception e) {
            return resultHelper.errorResp(logger, e, ErrorType.UNKNOWN, e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(path = "/all")
    public ResponseEntity<?> all() {
        try {
            return resultHelper.successResp(roleDomain.all(), HttpStatus.OK);
        } catch (Exception e) {
            // Return unknown error and log the exception.
            return resultHelper.errorResp(logger, e, ErrorType.UNKNOWN, e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
