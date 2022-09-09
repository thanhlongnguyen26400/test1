package spending_management_project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import spending_management_project.domain.UserDomain;
import spending_management_project.enums.ErrorType;
import spending_management_project.exception.CommonsException;
import spending_management_project.param.JwtParam;
import spending_management_project.param.RefreshParam;
import spending_management_project.po.User;
import spending_management_project.tools.Assert;
import spending_management_project.tools.JwtUtil;
import spending_management_project.vo.RegisterVO;

@Service
public class LoginService implements ILoginService{
    @Override
    public RegisterVO login(JwtParam param) throws Exception {
        return execute(param.getUsername(), param.getPassword());
    }

    @Override
    public RegisterVO refresh(RefreshParam param) throws Exception {
        User usr = userService.loadUserByUsername(
                jwtUtil.getUsernameFromToken(param.getRefreshToken())
        );
        return execute(usr.getUsername(), usr.getPassword());
    }

    private RegisterVO execute(String usr, String pwd) throws Exception {
        final User userDetails
                = userService.loadUserByUsername(usr);
        if(!userDetails.getPassword().equals(pwd)){
            throw new CommonsException(ErrorType.LOG0002,ErrorType.LOG0002.description());
        }
        final String token =
                jwtUtil.generateToken(userDetails);
        return new RegisterVO(userDomain.po2Vo(userDetails),token);
    }

    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final UserDomain userDomain;

    @Autowired

    public LoginService(UserService userService, JwtUtil jwtUtil, UserDomain userDomain) {
        Assert.defaultNotNull(userService);
        Assert.defaultNotNull(jwtUtil);
        Assert.defaultNotNull(userDomain);
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.userDomain = userDomain;
    }
}
