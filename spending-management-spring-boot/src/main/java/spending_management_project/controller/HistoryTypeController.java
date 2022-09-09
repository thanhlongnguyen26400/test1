package spending_management_project.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import spending_management_project.annotation.CurrentUser;
import spending_management_project.component.ResultHelper;
import spending_management_project.domain.HistoryTypeDomain;
import spending_management_project.enums.ErrorType;
import spending_management_project.exception.CommonsException;
import spending_management_project.param.HistoryTypeParam;
import spending_management_project.po.User;
import spending_management_project.tools.Assert;


@RestController
@RequestMapping(path = "/history-type")
public class HistoryTypeController {

    @PostMapping(path = "/add")
    private ResponseEntity<?> create(@CurrentUser User user, @RequestBody HistoryTypeParam param){

        try {
            return resultHelper.successResp(historyTypeDomain.create(param,user),HttpStatus.CREATED);
        }catch (Exception e){
            return resultHelper.errorResp(logger, e, ErrorType.UNKNOWN, e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(path = "/update")
    private ResponseEntity<?> update(@CurrentUser User user, @RequestBody HistoryTypeParam param){
        try {
            return resultHelper.successResp(historyTypeDomain.update(param,user),HttpStatus.CREATED);
        }catch (CommonsException e) {
            // Return error information and log the exception.
            return resultHelper.infoResp(logger, e.getErrorType(), e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        } catch (Exception e) {
            return resultHelper.errorResp(logger, e, ErrorType.UNKNOWN, e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping(path = "/{id}")
    private ResponseEntity<?> getHistoryType(@PathVariable Long id){
        try {
            return resultHelper.successResp(historyTypeDomain.getById(id),HttpStatus.OK);
        }catch (CommonsException e) {
            // Return error information and log the exception.
            return resultHelper.infoResp(logger, e.getErrorType(), e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        } catch (Exception e) {
            return resultHelper.errorResp(logger, e, ErrorType.UNKNOWN, e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping(path = "/all")
    private ResponseEntity<?> getAll(@CurrentUser User user){
        try {
            return resultHelper.successResp(historyTypeDomain.all(user.getId()),HttpStatus.OK);
        }catch (Exception e) {
            return resultHelper.errorResp(logger, e, ErrorType.UNKNOWN, e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping(path = "/delete/{id}")
    private  ResponseEntity<?> delete(@CurrentUser User user, @PathVariable Long id){
        try{
            return resultHelper.successResp(historyTypeDomain.delete(user,id),HttpStatus.OK);
        }catch (Exception e){
            return resultHelper.errorResp(logger, e, ErrorType.UNKNOWN, e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final ResultHelper resultHelper;
    private final HistoryTypeDomain historyTypeDomain;

    @Autowired

    public HistoryTypeController(ResultHelper resultHelper, HistoryTypeDomain historyTypeDomain) {
        Assert.defaultNotNull(resultHelper);
        Assert.defaultNotNull(historyTypeDomain);
        this.resultHelper = resultHelper;
        this.historyTypeDomain = historyTypeDomain;
    }
}
