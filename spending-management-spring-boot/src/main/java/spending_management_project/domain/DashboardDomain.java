package spending_management_project.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spending_management_project.enums.IncomeType;
import spending_management_project.po.History;
import spending_management_project.po.User;
import spending_management_project.repo.HistoryRepository;
import spending_management_project.tools.Assert;
import spending_management_project.tools.DateUtil;
import spending_management_project.tools.SpecificationUtil;
import spending_management_project.vo.DashBoardVO;
import spending_management_project.vo.HistoryTypeVO;
import spending_management_project.vo.HistoryVO;


import java.text.ParseException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Transactional(readOnly = true)
public class DashboardDomain {

    public DashBoardVO getSummary(User user) {

        //query origin
        Specification<History> condition = Specification.where(specificationUtil.getSpecificationHistoryUser(user.getId()));
        Collection<History> histories = historyRepository.findAll(condition);

        //get history type
        List<HistoryTypeVO> vos = historyTypeDomain.all(user.getId());
        Set<HistoryTypeVO> typeVOS = new HashSet<>(vos);

        //get history per month
        Map<Object, Map<IncomeType, Long>> listPerMonths = validateFillLineChart(histories);

        //get month name
        LocalDateTime now = LocalDateTime.now();
        String month_name = now.getMonth().getDisplayName(TextStyle.SHORT, Locale.getDefault());

        //get history per day in current month
        Map<String, Map<IncomeType, Long>> thirtyDay = validateDataSinceThirtyDay(histories);

        //get pie
        Map<IncomeType, Map<Long, Long>> pie = validatePie(histories, month_name, vos);

        //last update
        History history = filterDataCurrentYear(histories).max(Comparator.comparingLong(History::getLastModifiedAt)).orElse(null);
        String lastUpdate = null;
        if (history != null) {
            lastUpdate = DateUtil.dateToString(new Date(history.getLastModifiedAt()), "YYYY-MM-dd hh:mm a");
        }
        //month summary
        Map<IncomeType, Long> current_month = listPerMonths.get(month_name);
        long income = current_month.get(IncomeType.INCOME);
        long expend = current_month.get(IncomeType.EXPENDITURE);
        long balance = income - expend;
        long total = income + expend;
        total = total == 0 ? 1 : total;
        double saving = (income * 1.0 / total);
        return new DashBoardVO(income, expend, balance, saving, lastUpdate, listPerMonths, pie, typeVOS, thirtyDay);
    }

    private Map<Object, Map<IncomeType, Long>> validateFillLineChart(Collection<History> histories) {
        Map<Object, Map<IncomeType, Long>> listMap = getHistoryIncomeTypeEachMonth(histories);
        //put missing month to map
        months.forEach(x -> {
            Map<IncomeType, Long> temp = listMap.get(x);
            if (temp == null) {
                temp = new TreeMap<>();
                temp.put(IncomeType.INCOME, (long) 0);
                temp.put(IncomeType.EXPENDITURE, (long) 0);
            }
            temp.computeIfAbsent(IncomeType.INCOME, k -> (long) 0);
            temp.computeIfAbsent(IncomeType.EXPENDITURE, k -> (long) 0);
            listMap.put(x, temp);
        });
        Comparator<Object> comparator = Comparator.comparingInt(months::indexOf);
        Map<Object, Map<IncomeType, Long>> sortMap = new TreeMap<>(comparator);
        sortMap.putAll(listMap);
        return sortMap;
    }

    private Map<String, Map<IncomeType, Long>> validateDataSinceThirtyDay(Collection<History> histories) {
        Map<String, Map<IncomeType, Long>> lineChart = getDataSinceThirtyDay(histories);
        int day_ago = 30;
        List<String> listDate = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM");
        for (int i = day_ago; i >= 0; i--) {
            LocalDate dateTemp = DateUtil.getDayAgo(i);
            String key = dateTemp.format(formatter);
            listDate.add(key);
            Map<IncomeType, Long> temp = lineChart.get(key);
            if (temp == null) {
                temp = new TreeMap<>();
                temp.put(IncomeType.EXPENDITURE, (long) 0);
                temp.put(IncomeType.INCOME, (long) 0);
            }
            temp.computeIfAbsent(IncomeType.INCOME, k -> (long) 0);
            temp.computeIfAbsent(IncomeType.EXPENDITURE, k -> (long) 0);
            lineChart.put(key, temp);
        }
        Map<String, Map<IncomeType, Long>> sortedMap = new TreeMap<>(Comparator.comparingInt(listDate::indexOf));
        sortedMap.putAll(lineChart);
        return sortedMap;
    }

    private Map<IncomeType, Map<Long, Long>> validatePie(Collection<History> histories, String currentMonth, List<HistoryTypeVO> vos) {
        Map<IncomeType, Map<Long, Long>> pie = getPie(histories, currentMonth);
//        System.out.println("pie "+pie);
        if (pie == null) {
            return null;
        }
        Map<Long, Long> income = pie.get(IncomeType.INCOME);
        Map<Long, Long> expend = pie.get(IncomeType.EXPENDITURE);
        if (income == null) income = new TreeMap<>();
        if (expend == null) expend = new TreeMap<>();

        //init missing data
        Map<Long, Long> finalIncome = income;
        Map<Long, Long> finalExpend = expend;
        vos.stream().mapToLong(HistoryTypeVO::getId).forEach(value -> {
            finalIncome.computeIfAbsent(value, _k -> (long) 0);
            finalExpend.computeIfAbsent(value, _k -> (long) 0);
        });
        pie.put(IncomeType.INCOME, income);
        pie.put(IncomeType.EXPENDITURE, expend);
        return pie;
    }

    public Map<Object, Map<IncomeType, Long>> getHistoryIncomeTypeEachMonth(Collection<History> histories)
            throws RuntimeException {
        return filterDataCurrentYear(histories)
                .map(history -> {
                    try {
                        return historyDomain.po2Vo(history);
                    } catch (Exception e) {
                        throw new RuntimeException();
                    }
                })
                .collect(Collectors.groupingBy(history -> {
                            try {
                                return DateUtil.formatStringDate(history.getDate(), "yyyy-MM-dd", "MMM");
                            } catch (ParseException e) {
                                e.printStackTrace();
                                return null;
                            }
                        },
                        TreeMap::new,
                        Collectors.groupingBy(history -> IncomeType.parse(history.getType()),
                                TreeMap::new,
                                Collectors.summingLong(HistoryVO::getMoney))));
    }

    public Map<IncomeType, Map<Long, Long>> getPie(Collection<History> histories, String currentMonth) throws RuntimeException {
        return filterDataCurrentYear(histories)
                .filter((history) -> DateUtil.dateToString(history.getDate(), "MMM").equalsIgnoreCase(currentMonth))
                .collect(Collectors.groupingBy(History::getType,
                        TreeMap::new,
                        Collectors.groupingBy(history -> history.getHistoryType().getId(),
                                TreeMap::new,
                                Collectors.summingLong(History::getMoney)
                        )));

    }

    public Map<String, Map<IncomeType, Long>> getDataSinceThirtyDay(Collection<History> histories) {
        Date ago_30 = DateUtil.asDate(LocalDate.now().minus(Period.ofDays(30)));
        return filterDataCurrentYear(histories)
                .filter(history -> history.getDate().after(ago_30))
                .sorted(Comparator.comparing(History::getDate))
                .collect(Collectors.groupingBy(history -> DateUtil.dateToString(history.getDate(), "dd/MM"),
                        TreeMap::new,
                        Collectors.groupingBy(History::getType,
                                TreeMap::new,
                                Collectors.summingLong(History::getMoney)
                        )));
    }

    public Stream<History> filterDataCurrentYear(Collection<History> histories) {
        Date firstDate = DateUtil.asDate(DateUtil.getFirstDayOfYear());
        Date lastDate = DateUtil.asDate(DateUtil.getLastDayOfYear());
        return histories.stream().filter(history -> history.getDate().after(firstDate) && history.getDate().before(lastDate));
    }

    private final HistoryRepository historyRepository;
    private final HistoryTypeDomain historyTypeDomain;
    private final SpecificationUtil specificationUtil;
    private final HistoryDomain historyDomain;
    private final List<String> months = Arrays.asList("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec");

    @Autowired

    public DashboardDomain(HistoryRepository historyRepository,
                           SpecificationUtil specificationUtil,
                           HistoryDomain historyDomain, HistoryTypeDomain domain) {
        Assert.defaultNotNull(historyRepository);
        Assert.defaultNotNull(specificationUtil);
        Assert.defaultNotNull(historyDomain);
        Assert.defaultNotNull(domain);
        this.historyRepository = historyRepository;
        this.specificationUtil = specificationUtil;
        this.historyDomain = historyDomain;
        this.historyTypeDomain = domain;
    }
}
