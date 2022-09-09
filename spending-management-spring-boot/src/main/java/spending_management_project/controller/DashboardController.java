package spending_management_project.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import spending_management_project.annotation.CurrentUser;
import spending_management_project.component.ResultHelper;
import spending_management_project.domain.DashboardDomain;
import spending_management_project.enums.ErrorType;
import spending_management_project.po.User;
import spending_management_project.tools.Assert;


@RestController
@RequestMapping(path = "/dashboard")
public class DashboardController {

    @GetMapping(path = "/summary")
    public ResponseEntity<?> getIncome(@CurrentUser User user){
        try {
            return resultHelper.successResp(dashboardDomain.getSummary(user),HttpStatus.OK);
        }catch (Exception e) {
            return resultHelper.infoResp(ErrorType.UNKNOWN, e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private final ResultHelper resultHelper;
    private final DashboardDomain dashboardDomain;

    @Autowired

    public DashboardController(ResultHelper resultHelper, DashboardDomain dashboardDomain) {
        Assert.defaultNotNull(resultHelper);
        Assert.defaultNotNull(dashboardDomain);
        this.resultHelper = resultHelper;
        this.dashboardDomain = dashboardDomain;
    }
}
