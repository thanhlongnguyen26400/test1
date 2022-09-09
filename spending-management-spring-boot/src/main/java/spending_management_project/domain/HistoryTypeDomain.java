package spending_management_project.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spending_management_project.component.Transformer;
import spending_management_project.constant.CommonsConstant;
import spending_management_project.enums.ErrorType;
import spending_management_project.exception.CommonsException;
import spending_management_project.param.HistoryTypeParam;
import spending_management_project.po.HistoryType;
import spending_management_project.po.User;
import spending_management_project.repo.CustomRepository;
import spending_management_project.repo.HistoryTypeRepository;
import spending_management_project.tools.Assert;
import spending_management_project.tools.ErrorMsgHelper;
import spending_management_project.vo.HistoryTypeVO;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class HistoryTypeDomain extends BaseDomain<HistoryType, Long> {

    @Transactional
    public HistoryTypeVO create(HistoryTypeParam param, User currentUser) throws Exception {
        nameExists(param.getName(),param.getDescription(),currentUser.getId());
        return po2Vo(super.createByPO(param2Po(param, new HistoryType(), currentUser)));
    }

    public HistoryTypeVO po2Vo(HistoryType historyType) throws Exception {
        if (historyTypeRepository == null) {
            return null;
        }
        HistoryTypeVO vo = transformer.po2VO(HistoryTypeVO.class, historyType);
        vo.setDefault(historyType.getCreatedBy() == 0);
        return vo;
    }

    public List<HistoryTypeVO> all(long id) {
        return historyTypeRepository.findAll().stream()
                .filter(historyType -> historyType.getCreatedBy() == id || historyType.getCreatedBy() == 0)
                .map(role -> {
                    try {
                        return po2Vo(role);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }).collect(Collectors.toList());
    }



    @Transactional
    public HistoryTypeVO update(HistoryTypeParam param, User currentUser) throws Exception {
        HistoryType role = findById(param.getId());
        if (role == null) {
            throw new CommonsException(ErrorType.SYS0122, ErrorMsgHelper
                    .getReturnMsg(ErrorType.SYS0122, getClassT().getSimpleName(), CommonsConstant.ID));
        }
        return po2Vo(super.updateByPO(param2Po(param, role, currentUser)));
    }

    public HistoryTypeVO getById(Long id) throws Exception {
        return po2Vo(historyTypeRepository.findById(id).orElse(null));
    }

    //for update and delete
    public HistoryType findById(Long id) {
        return historyTypeRepository.findById(id).filter(x -> x.getCreatedBy() != 0).orElse(null);
    }

    @Transactional
    @Override
    public void deepDelete(Long id) throws Exception {
        HistoryType type = findById(id);
        if (type == null) {
            throw new CommonsException(ErrorType.SYS0122, ErrorMsgHelper
                    .getReturnMsg(ErrorType.SYS0122, getClassT().getSimpleName().toLowerCase(),
                            CommonsConstant.ID));
        }
        historyTypeRepository.delete(type);
    }
    public HistoryTypeVO delete(User user, long id) throws Exception {
        HistoryType type = historyTypeRepository.findAll()
                .stream().filter(historyType -> historyType.getCreatedBy() == user.getId() && historyType.getId() == id).findFirst().orElse(null);
        if(type == null){
            throw new CommonsException(ErrorType.SYS0140,ErrorMsgHelper.getReturnMsg(ErrorType.SYS0140,id+""));
        }
        HistoryTypeVO typeVO = po2Vo(type);
        historyTypeRepository.delete(type);
        return typeVO;
    }

    private final HistoryTypeRepository historyTypeRepository;

    @Autowired

    public HistoryTypeDomain(CustomRepository<HistoryType, Long> repository, Transformer transformer, HistoryTypeRepository historyTypeRepository) {
        super(repository, transformer);
        Assert.defaultNotNull(historyTypeRepository);
        this.historyTypeRepository = historyTypeRepository;
    }

    private HistoryType param2Po(HistoryTypeParam param, HistoryType historyType, User currentUser) throws Exception {
        transformer.param2PO(getClassT(), param, historyType, currentUser);
        historyType.setCreatedBy(currentUser.getId());
        return historyType;
    }

    private void nameExists(String name, String description, long id) throws Exception {
        HistoryType obj = historyTypeRepository.findAll().stream().filter(historyType -> historyType.getCreatedBy() == id &&
                historyType.getName().equalsIgnoreCase(name) && historyType.getDescription().equalsIgnoreCase(description)).findFirst().orElse(null);
        if (obj != null) {
            // Throw role already existing exception, name taken.
            throw new CommonsException(ErrorType.SYS0111, ErrorMsgHelper
                    .getReturnMsg(ErrorType.SYS0111, getClassT().getSimpleName().toLowerCase(),
                            CommonsConstant.NAME));
        }
    }
}
