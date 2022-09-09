package spending_management_project.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spending_management_project.component.Transformer;
import spending_management_project.constant.CommonsConstant;
import spending_management_project.enums.ErrorType;
import spending_management_project.enums.ValidFlag;
import spending_management_project.exception.CommonsException;
import spending_management_project.param.RoleParam;
import spending_management_project.po.Role;
import spending_management_project.po.User;
import spending_management_project.repo.CustomRepository;
import spending_management_project.repo.RoleRepository;
import spending_management_project.tools.Assert;
import spending_management_project.tools.ErrorMsgHelper;
import spending_management_project.vo.RoleVO;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class RoleDomain extends BaseDomain<Role, Long> {
    // ------------------------
    // PUBLIC METHODS
    // ------------------------

    @Transactional
    public RoleVO create(RoleParam param, User currentUser) throws Exception {
        return po2Vo(super.createByPO(param2Po(param, new Role(), currentUser)));
    }

    public List<RoleVO> all() {
        return roleRepository.findAll().stream()
                .map(role -> {
                    try {
                        return po2Vo(role);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }).collect(Collectors.toList());
    }

    @Transactional
    public RoleVO update(RoleParam param, User currentUser) throws Exception {
        Role role = findById(param.getId());
        if (role == null) {
            throw new CommonsException(ErrorType.SYS0122, ErrorMsgHelper
                    .getReturnMsg(ErrorType.SYS0122, getClassT().getSimpleName(), CommonsConstant.ID));
        }
        return po2Vo(super.updateByPO(param2Po(param, role, currentUser)));
    }

    public RoleVO getById(Long id) throws Exception {
        return po2Vo(roleRepository.findById(id).orElse(null));
    }

    public Role findById(Long id) {
        return roleRepository.findById(id).orElse(null);
    }

    @Transactional
    public RoleVO delete(Long id) throws Exception {
        Role role = findById(id);
        if (role == null) {
            throw new CommonsException(ErrorType.SYS0122, ErrorMsgHelper
                    .getReturnMsg(ErrorType.SYS0122, getClassT().getSimpleName().toLowerCase(),
                            CommonsConstant.ID));
        }
        roleRepository.delete(role);
        return po2Vo(role);
    }

    public RoleVO po2Vo(Role role) throws Exception {
        if (role == null) {
            return null;
        }
//                vo.setResources(role.getResources().stream()
//                .map(resource -> {
//                    try {
//                        return transformer.po2VO(ResourceVO.class, resource);
//                    } catch (InstantiationException | IllegalAccessException e) {
//                        throw new RuntimeException(e);
//                    }
//                }).collect(Collectors.toSet()));
        return transformer.po2VO(RoleVO.class, role);
    }

    // --------------------------
    // PRIVATE FIELDS AND METHODS
    // --------------------------

    private final RoleRepository roleRepository;

    @Autowired
    public RoleDomain(CustomRepository<Role, Long> repository, Transformer transformer,
                      RoleRepository roleRepository) {
        super(repository, transformer);
        Assert.defaultNotNull(roleRepository);
        this.roleRepository = roleRepository;
    }

    private Role param2Po(RoleParam param, Role role, User currentUser) throws Exception {
        nameExists(param.getName());
        transformer.param2PO(getClassT(), param, role, currentUser);
//        if (StringUtils.isNotBlank(param.getResourceIds())) {
//            Set<Resource> resources = Sets
//                    .newHashSet(resourceDomain.getAllByIds(transformer.idsStr2List(param.getResourceIds())));
//            role.setResources(resources);
//        } else {
//            role.setResources(new HashSet<>());
//        }
        return role;
    }

    private void nameExists(String name) throws Exception {
        if (roleRepository.findByNameAndValidFlag(name, ValidFlag.VALID).isPresent()) {
            // Throw role already existing exception, name taken.
            throw new CommonsException(ErrorType.SYS0111, ErrorMsgHelper
                    .getReturnMsg(ErrorType.SYS0111, getClassT().getSimpleName().toLowerCase(),
                            CommonsConstant.NAME));
        }
    }
}
