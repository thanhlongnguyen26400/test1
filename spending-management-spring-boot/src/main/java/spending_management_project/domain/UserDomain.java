package spending_management_project.domain;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spending_management_project.component.Transformer;
import spending_management_project.constant.CommonsConstant;
import spending_management_project.enums.ErrorType;
import spending_management_project.enums.ValidFlag;
import spending_management_project.exception.CommonsException;
import spending_management_project.param.UserParam;
import spending_management_project.po.*;
import spending_management_project.repo.AccountRepository;
import spending_management_project.repo.CustomRepository;
import spending_management_project.repo.UserProfileRepository;
import spending_management_project.repo.UserRepository;
import spending_management_project.tools.Assert;
import spending_management_project.tools.ErrorMsgHelper;
import spending_management_project.tools.JwtUtil;
import spending_management_project.vo.RegisterVO;
import spending_management_project.vo.UserVO;

import java.util.*;
import java.util.stream.Collectors;


@Service
@Transactional(readOnly = true)
public class UserDomain extends BaseDomain<User, Long> {

    // ------------------------
    // PUBLIC METHODS
    // ------------------------

    @Transactional
    public RegisterVO create(UserParam param, User currentUser) throws Exception {
        UserVO userVO = po2Vo(createReturnPo(param, currentUser));
        return new RegisterVO(userVO, jwtUtil.generateToken(userRepository.getById(userVO.getId())));
    }

    @Transactional
    public User createReturnPo(UserParam param, User currentUser) throws Exception {
        if (usrUsed(param.getUsr())) {
            // Throw user already exists error, usr taken.
            final String ACCOUNT = "account";
            throw new CommonsException(ErrorType.SYS0111,
                    ErrorMsgHelper.getReturnMsg(ErrorType.SYS0111, ACCOUNT, ACCOUNT));
        }
        User user = param2Po(param, new User(), currentUser);
        Account account = param2Account(param, new Account(), user, currentUser);
        accountRepository.save(account);
        UserProfile userProfile = user2UserProfile(user);
        userProfileRepository.save(userProfile);
        return super.createByPO(user);
    }

    public List<UserVO> getAll(Specification<User> specification, Sort sort) {
        return userRepository.findAll(specification, sort).stream().map(
                po -> {
                    try {
                        return po2Vo(po);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }).collect(Collectors.toList());
    }

    public Page<?> getPage(Specification<User> specification, Pageable pageable) throws Exception {
        return getPage(specification, pageable, UserVO.class);
    }

    public User findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public User findByAccount(String usr) {
        Account account = accountRepository.findByAccount(usr).orElse(null);
        return account == null ? null
                : userRepository.findByIdAndValidFlag(account.getUser().getId(), ValidFlag.VALID)
                .orElse(null);
    }


    public boolean usrUsed(String usr) {
        return accountRepository.findByAccount(usr).isPresent();
    }

    @Transactional
    public UserVO update(UserParam param, User currentUser) throws Exception {
        return po2Vo(updateReturnPo(param, currentUser));
    }

    @Transactional
    public User updateReturnPo(UserParam param, User currentUser) throws Exception {
        User user = findById(param.getId());
        if (user == null) {
            throw new CommonsException(ErrorType.SYS0122,
                    ErrorMsgHelper.getReturnMsg(ErrorType.SYS0122, getClassT().getSimpleName().toLowerCase(),
                            CommonsConstant.ID));
        }
        if (StringUtils.isNotBlank(param.getUsr())) {
            param.setUsr(null);
        }
        return super.updateByPO(param2Po(param, user, currentUser));
    }

    public Account param2Account(UserParam param, Account account, User user, User currentUser)
            throws Exception {
        account = transformer.param2PO(Account.class, param, account, currentUser);
        account.setAccount(param.getUsr());
        account.setUser(user);
        return account;
    }

    public UserProfile user2UserProfile(User user){
        UserProfile profile = new UserProfile(user);
        profile.setValidFlag(ValidFlag.VALID);
        profile.setCreatedAt(System.currentTimeMillis());
        profile.setCreatedBy(0);
        profile.setLastModifiedAt(System.currentTimeMillis());
        profile.setLastModifiedBy(0);
        return profile;
    }

    // --------------------------
    // PRIVATE FIELDS AND METHODS
    // --------------------------

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final RoleDomain roleDomain;
    private final UserProfileRepository userProfileRepository;
    private final JwtUtil jwtUtil;

    public UserDomain(CustomRepository<User, Long> repository, Transformer transformer,
                      AccountRepository accountRepository, UserRepository userRepository,
                      RoleDomain roleDomain, JwtUtil jwtUtil, UserProfileRepository userProfileRepository) {
        super(repository, transformer);
        Assert.defaultNotNull(accountRepository);
        Assert.defaultNotNull(userRepository);
        Assert.defaultNotNull(roleDomain);
        Assert.defaultNotNull(jwtUtil);
        Assert.defaultNotNull(userProfileRepository);
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
        this.roleDomain = roleDomain;
        this.jwtUtil = jwtUtil;
        this.userProfileRepository = userProfileRepository;
    }

    private User param2Po(UserParam param, User user, User currentUser) throws Exception {
        nameExists(param.getUsr());
        user = transformer.param2PO(User.class, param, user, currentUser);
        user.setRole(roleDomain.findById(Long.valueOf(param.getRoleId())));
        user.setHistories(new HashSet<>());
        System.out.println("param2Po " + user);
        return user;
    }

    public UserVO po2Vo(User user) throws Exception {
        UserVO userVO = transformer.po2VO(UserVO.class, user);
        userVO.setRole_id(user.getRole().getId() + "");
        return userVO;
    }

    private void nameExists(String usr) throws Exception {
        if (userRepository.findByUsrAndValidFlag(usr, ValidFlag.VALID).isPresent()) {
            // Throw role already existing exception, name taken.
            throw new CommonsException(ErrorType.SYS0111, ErrorMsgHelper
                    .getReturnMsg(ErrorType.SYS0111, getClassT().getSimpleName().toLowerCase(),
                            CommonsConstant.NAME));
        }
    }
}
