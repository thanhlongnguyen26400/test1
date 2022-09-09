package spending_management_project.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spending_management_project.component.Transformer;
import spending_management_project.constant.CommonsConstant;
import spending_management_project.enums.ErrorType;
import spending_management_project.enums.IncomeType;
import spending_management_project.exception.CommonsException;
import spending_management_project.param.HistoryParam;
import spending_management_project.po.History;
import spending_management_project.po.HistoryType;
import spending_management_project.po.User;
import spending_management_project.repo.CustomRepository;
import spending_management_project.repo.HistoryRepository;
import spending_management_project.repo.HistoryTypeRepository;
import spending_management_project.tools.Assert;
import spending_management_project.tools.DateUtil;
import spending_management_project.tools.ErrorMsgHelper;
import spending_management_project.vo.HistoryVO;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Transactional
public class HistoryDomain extends BaseDomain<History, Long> {
    // ------------------------
    // PUBLIC METHODS
    // ------------------------

    @Transactional
    public HistoryVO create(HistoryParam param, User currentUser) throws Exception {
        return po2Vo(super.createByPO(param2Po(param, new History(), currentUser)));
    }

    public List<HistoryVO> all(long id) {
        return historyRepository.findAll()
                .stream()
                .filter(history -> history.getUser().getId() == id)
                .map(history -> {
                    try {
                        return po2Vo(history);
                    } catch (Exception e) {
                        throw new RuntimeException();
                    }
                }).collect(Collectors.toList());
    }

    public Stream<History> getAllStream(long id){
        return historyRepository.findAll().stream().filter(history -> history.getUser().getId() == id);
    }

    @Transactional
    public HistoryVO update(HistoryParam param, User currentUser) throws Exception {
        History history = findById(param.getId());
        if (history == null) {
            throw new CommonsException(ErrorType.SYS0122, ErrorMsgHelper
                    .getReturnMsg(ErrorType.SYS0122, getClassT().getSimpleName(), CommonsConstant.ID));
        }
        return po2Vo(super.updateByPO(param2Po(param, history, currentUser)));
    }

    public HistoryVO getById(Long id) throws Exception {
        return po2Vo(historyRepository.findById(id).orElse(null));
    }

    @Transactional
    public HistoryVO delete(Long id) throws Exception {
        History history = historyRepository.findById(id).orElse(null);
        if (history == null) {
            throw new CommonsException(ErrorType.SYS0122, ErrorMsgHelper
                    .getReturnMsg(ErrorType.SYS0122, getClassT().getSimpleName().toLowerCase(),
                            CommonsConstant.ID));
        }
        historyRepository.delete(history);
        return po2Vo(history);
    }

    public Stream<History> getHistoryOfCurrentMonth(long id){
        return historyRepository.findAll().stream().filter(history ->
                history.getUser().getId() == id &&
                history.getDate().after(DateUtil.asDate(DateUtil.getFirstDayMonth())) &&
                history.getDate().before(DateUtil.asDate(DateUtil.getLastDayOfMonth())));
    }

    public Page<?> getPage(Specification<History> specification, Pageable pageable) {
        Page<History> poPage = historyRepository.findAll(specification, pageable);
        if (poPage.getSize() == 0) {
            return null;
        }
        List<HistoryVO> voList = poPage.getContent().stream().map(history -> {
            try {
                return po2Vo(history);
            } catch (Exception e) {
                throw new RuntimeException();
            }
        }).collect(Collectors.toList());
        return super.transformer.poPage2VO(voList, pageable, poPage.getTotalElements());
    }

    //for update and delete
    public History findById(Long id) {
        return historyRepository.findById(id).filter(x -> x.getCreatedBy() != 0).orElse(null);
    }

    @Transactional
    @Override
    public void deepDelete(Long id) throws Exception {
        History history = findById(id);
        if (history == null) {
            throw new CommonsException(ErrorType.SYS0122, ErrorMsgHelper
                    .getReturnMsg(ErrorType.SYS0122, getClassT().getSimpleName().toLowerCase(),
                            CommonsConstant.ID));
        }
        historyRepository.delete(history);
    }

    public HistoryVO po2Vo(History history) throws Exception {
        if (history == null) {
            throw new CommonsException(ErrorType.SYS0122, ErrorMsgHelper
                    .getReturnMsg(ErrorType.SYS0122, getClassT().getSimpleName().toLowerCase(),
                            CommonsConstant.ID));
        }
        HistoryVO vo = transformer.po2VO(HistoryVO.class, history);
        vo.setHistory_type(history.getHistoryType().getId());
        vo.setDate(DateUtil.dateToString(history.getDate(), "yyyy-MM-dd"));
        vo.setType(history.getType().code());
//        System.out.println(vo);
        return vo;
    }

    // --------------------------
    // PRIVATE FIELDS AND METHODS
    // --------------------------

    private final HistoryRepository historyRepository;
    private final HistoryTypeRepository historyTypeRepository;
    private final NotificationDomain notificationDomain;

    @Autowired
    public HistoryDomain(CustomRepository<History, Long> repository,
                         Transformer transformer, HistoryRepository historyRepository,
                         HistoryTypeRepository historyTypeRepository,
                         NotificationDomain notificationDomain) {
        super(repository, transformer);
        Assert.defaultNotNull(historyRepository);
        Assert.defaultNotNull(historyTypeRepository);
        Assert.defaultNotNull(notificationDomain);
        this.historyRepository = historyRepository;
        this.historyTypeRepository = historyTypeRepository;
        this.notificationDomain = notificationDomain;
    }

    private History param2Po(HistoryParam param, History history, User currentUser) throws Exception {
        history.setDate(DateUtil.stringToDate(param.getDate(), "yyyy-MM-dd"));
        history = transformer.param2PO(getClassT(), param, history, currentUser);
        HistoryType type = historyTypeRepository.findById(param.getHistory_type()).orElse(null);
        if (type == null) {
            throw new CommonsException(ErrorType.SYS0122, ErrorMsgHelper
                    .getReturnMsg(ErrorType.SYS0122, getClassT().getSimpleName().toLowerCase(),
                            CommonsConstant.ID));
        }
        history.setHistoryType(type);
        history.setUser(currentUser);
        history.setType(IncomeType.parse(param.getType()));
        history.setCreatedBy(currentUser.getId());
//        System.out.println(history);
        notificationDomain.create(currentUser,IncomeType.parse(param.getType()),param.getMoney());
        return history;
    }

}
