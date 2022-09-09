package spending_management_project.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spending_management_project.MetaModel.Notification_;
import spending_management_project.component.Transformer;
import spending_management_project.enums.IncomeType;
import spending_management_project.param.NotificationParam;
import spending_management_project.po.Notification;
import spending_management_project.po.User;
import spending_management_project.repo.CustomRepository;
import spending_management_project.repo.NotificationRepository;
import spending_management_project.tools.Assert;
import spending_management_project.tools.DateUtil;
import spending_management_project.vo.NotificationVO;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = false)
public class NotificationDomain extends BaseDomain<Notification, Long> {

    @Transactional
    public Notification create(User user, IncomeType type, long money) {
        Notification notification = new Notification();
        notification.setType(type);
        notification.setUser_id(user.getId());
        String content = "";
        if (type.isIncome()) {
            content = "Your money increased " + money;
        }
        if (type.isExpend()) {
            content = "Your money decreased " + money;
        }
        notification.setContent(content);
        notification.setCreatedBy(user.getId());
        notification.setLastModifiedBy(user.getId());
        return notificationRepository.save(notification);
    }

    public Map<String, Object> getNotification(User user, NotificationParam param) {
        Map<String, Object> map = new TreeMap<>();
        List<NotificationVO> vos = notificationRepository.findAll(getSpec(user), getPage(param)).getContent().stream().sorted(Comparator.comparingLong(Notification::getLastModifiedAt))
                .map(notification -> {
                    try {
                        return po2VO(notification);
                    } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                        e.printStackTrace();
                        throw new RuntimeException();
                    }
                }).collect(Collectors.toList());
        long totals = notificationRepository.findAll(getSpec(user)).size();
        map.put("notification", vos);
        map.put("totals", totals);
        map.put("unread", getTotalsUnread(user));
        return map;
    }
    private NotificationVO po2VO(Notification notification) throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        NotificationVO vo = super.transformer.po2VO(NotificationVO.class,notification);
        vo.setLastUpdate(DateUtil.dateToString(new Date(notification.getLastModifiedAt()),"yyyy-MM-dd'T'HH:mm:ss"));
        return vo;
    }
    public Long getTotalsUnread(User user) {
        return notificationRepository.findAll(getSpec(user)).stream().filter(Notification::isUnRead).count();
    }

    @Transactional
    public Map<String, Integer> checkRead(User user) {
        //mark all notification read
        List<Notification> collect = notificationRepository.findAll(getSpec(user))
                .stream().filter(Notification::isUnRead)
                .peek(Notification::markRead)
                .collect(Collectors.toList());
        notificationRepository.saveAllAndFlush(collect);
        Map<String, Integer> map = new HashMap<>();
        map.put("unread", 0);
        return map;
    }

    private Pageable getPage(NotificationParam params) {
        return PageRequest.of(params.getPageNo() - 1, params.getPageSize()).withSort(Sort.by(Sort.Direction.DESC, "lastModifiedAt"));
    }

    private Specification<Notification> getSpec(User user) {
        return Specification.where((root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.in(root.get(Notification_.USER_ID)).value(user.getId()));
    }

    private final NotificationRepository notificationRepository;

    @Autowired
    public NotificationDomain(CustomRepository<Notification, Long> repository, Transformer transformer,
                              NotificationRepository notificationRepository) {
        super(repository, transformer);
        Assert.defaultNotNull(notificationRepository);
        this.notificationRepository = notificationRepository;
    }
}
