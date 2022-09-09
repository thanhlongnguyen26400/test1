package spending_management_project.service;

import org.springframework.http.ResponseEntity;
import spending_management_project.param.JwtParam;
import spending_management_project.param.RefreshParam;
import spending_management_project.vo.RegisterVO;

public interface ILoginService {
    RegisterVO login(JwtParam param) throws Exception;

    RegisterVO refresh(RefreshParam param) throws Exception;
}
