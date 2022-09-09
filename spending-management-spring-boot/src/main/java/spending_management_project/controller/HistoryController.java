package spending_management_project.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import spending_management_project.annotation.CurrentUser;
import spending_management_project.component.ResultHelper;
import spending_management_project.domain.HistoryDomain;
import spending_management_project.enums.ErrorType;
import spending_management_project.exception.CommonsException;
import spending_management_project.param.HistoryPaginationParam;
import spending_management_project.param.HistoryParam;
import spending_management_project.po.User;
import spending_management_project.tools.Assert;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/history")
public class HistoryController {

    @GetMapping(path = "/{id}")
    private ResponseEntity<?> getHistory(@PathVariable Long id) {
        try {
            return resultHelper.successResp(historyDomain.getById(id), HttpStatus.OK);
        } catch (CommonsException e) {
            return resultHelper.infoResp(logger, e.getErrorType(), e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        } catch (Exception e) {
            return resultHelper.errorResp(logger, e, ErrorType.UNKNOWN, e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(path = "/all")
    private ResponseEntity<?> getAll(@CurrentUser User user) {
        try {
            return resultHelper.successResp(historyDomain.all(user.getId()), HttpStatus.OK);
        } catch (Exception e) {
            return resultHelper.errorResp(logger, e, ErrorType.UNKNOWN, e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(path = "/add")
    private ResponseEntity<?> create(@CurrentUser User user, @RequestBody HistoryParam param) {
        try {
            return resultHelper.successResp(historyDomain.create(param, user), HttpStatus.CREATED);
        } catch (Exception e) {
            return resultHelper.errorResp(logger, e, ErrorType.UNKNOWN, e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(path = "/update")
    private ResponseEntity<?> update(@CurrentUser User user, @RequestBody HistoryParam param) {
        try {
            return resultHelper.successResp(historyDomain.update(param, user), HttpStatus.CREATED);
        } catch (CommonsException e) {
            // Return error information and log the exception.
            return resultHelper.infoResp(logger, e.getErrorType(), e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        } catch (Exception e) {
            return resultHelper.errorResp(logger, e, ErrorType.UNKNOWN, e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(path = "/delete/{id}")
    private ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            return resultHelper.successResp(historyDomain.delete(id), HttpStatus.OK);
        } catch (CommonsException e) {
            // Return error information and log the exception.
            return resultHelper.infoResp(logger, e.getErrorType(), e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        } catch (Exception e) {
            return resultHelper.errorResp(logger, e, ErrorType.UNKNOWN, e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    private ResponseEntity<?> page(@CurrentUser User user, @Valid HistoryPaginationParam param) {
        try {
            return resultHelper.successResp(
                    historyDomain.getPage(param.getSpecification(user.getId()), param.getPageable()),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return resultHelper.errorResp(logger, e, ErrorType.UNKNOWN, e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final ResultHelper resultHelper;
    private final HistoryDomain historyDomain;

    @Autowired
    public HistoryController(ResultHelper resultHelper, HistoryDomain historyDomain) {
        Assert.defaultNotNull(resultHelper);
        Assert.defaultNotNull(historyDomain);
        this.resultHelper = resultHelper;
        this.historyDomain = historyDomain;
    }
}
