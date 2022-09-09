package spending_management_project.domain;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spending_management_project.component.Transformer;
import spending_management_project.constant.AuthorityConstant;
import spending_management_project.param.UserPorfileParam;
import spending_management_project.po.History;
import spending_management_project.po.User;
import spending_management_project.po.UserProfile;
import spending_management_project.repo.CustomRepository;
import spending_management_project.repo.HistoryRepository;
import spending_management_project.repo.UserProfileRepository;
import spending_management_project.repo.UserRepository;
import spending_management_project.tools.Assert;
import spending_management_project.tools.DateUtil;
import spending_management_project.vo.SearchVO;
import spending_management_project.vo.UserProfileVO;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Transactional(readOnly = true)
public class UserProfileDomain extends BaseDomain<UserProfile, Long> {

    @Transactional
    public UserProfileVO update(UserPorfileParam param, User currentUser) throws Exception {
        if (StringUtils.isNotBlank(param.getFirstName())) {
            User user = userRepository.getById(currentUser.getId());
            user.setName(param.getFirstName());
            userRepository.save(user);
        }
        UserProfile profile = userProfileRepository.getById(currentUser.getId());
        if (profile == null) profile = new UserProfile();
        profile = userProfileRepository.save(super.transformer.param2PO(UserProfile.class, param, profile, currentUser));
        return po2VO(profile);
    }

    public UserProfileVO getById(long id) throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        return po2VO(userProfileRepository.getById(id));
    }

    public UserProfileVO po2VO(UserProfile userProfile) throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        UserProfileVO profileVO = super.transformer.po2VO(UserProfileVO.class, userProfile);
        Supplier<Stream<History>> stream = () -> historyDomain.getHistoryOfCurrentMonth(userProfile.getId());
        History history = stream.get().max(Comparator.comparingLong(History::getLastModifiedAt)).orElse(null);
        String lastUpdate = LocalDate.now().format(DateTimeFormatter.ISO_DATE);
        if (history != null) {
            lastUpdate = DateUtil.dateToString(new Date(history.getLastModifiedAt()), "YYYY-MM-dd");
        }
        long income = stream.get().filter(h -> h.getType().isIncome()).mapToLong(History::getMoney).sum();
        long expend = stream.get().filter(h -> h.getType().isExpend()).mapToLong(History::getMoney).sum();
        profileVO.setLastUpdate(lastUpdate);
        profileVO.setIncome(income);
        profileVO.setExpend(expend);
        return profileVO;
    }

    public Map<String, List<SearchVO>> searchName(String name, long id) {
        System.out.println("search string "+name);
        Comparator<UserProfile> comparator = Comparator.comparing(s -> s.getUsr().startsWith(name));
        comparator = comparator.thenComparing(UserProfile::getUsr);
        List<SearchVO> vos = userProfileRepository.findAll()
                .stream().filter(userProfile -> userProfile.getId() != id &&
                        userProfile.getId() != AuthorityConstant.ROOT_ID &&
                        userProfile.getUser().getRole().isUer())
                .filter(userProfile -> userProfile.getUsr().toLowerCase().contains(name))
//                .sorted(comparator)
                .peek(System.out::println)
                .map(SearchVO::new)
                .collect(Collectors.toList());
        Map<String, List<SearchVO>> map = new HashMap<>();
        map.put("data", vos);
        return map;
    }
    @Transactional
    public void saveAvatarUrl(String url, long id){
       UserProfile profile = userProfileRepository.findById(id).orElse(null);
       if(profile == null){
           throw new UsernameNotFoundException(String.format("User %s does not exist!", id));
       }
       profile.setAvatar(url);
       userProfileRepository.save(profile);
    }

    private final UserProfileRepository userProfileRepository;
    private final UserRepository userRepository;
    private final HistoryDomain historyDomain;

    @Autowired
    public UserProfileDomain(CustomRepository<UserProfile, Long> repository, Transformer transformer,
                             UserProfileRepository userProfileRepository, UserRepository userRepository,
                             HistoryDomain historyDomain) {
        super(repository, transformer);
        Assert.defaultNotNull(userProfileRepository);
        Assert.defaultNotNull(userRepository);
        Assert.defaultNotNull(historyDomain);
        this.userProfileRepository = userProfileRepository;
        this.userRepository = userRepository;
        this.historyDomain = historyDomain;
    }
}
