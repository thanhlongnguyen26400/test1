package spending_management_project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import spending_management_project.enums.ValidFlag;
import spending_management_project.po.User;
import spending_management_project.repo.UserRepository;
import spending_management_project.tools.Assert;


@Service
public class UserService implements UserDetailsService {
    @Override
    public User loadUserByUsername(String usr) throws UsernameNotFoundException {
        return userRepository.findByUsrAndValidFlag(usr, ValidFlag.VALID).orElseThrow(
                // Throw cannot find any user by this usr param.
                () -> new UsernameNotFoundException(String.format("User %s does not exist!", usr)));
    }
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        Assert.defaultNotNull(userRepository);
        this.userRepository = userRepository;
    }
}
