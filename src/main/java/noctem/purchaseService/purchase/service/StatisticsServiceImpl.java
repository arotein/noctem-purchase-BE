package noctem.purchaseService.purchase.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import noctem.purchaseService.global.security.bean.ClientInfoLoader;
import noctem.purchaseService.purchase.domain.repository.StatisticsRepository;
import noctem.purchaseService.purchase.dto.response.*;
import noctem.purchaseService.purchase.dto.vo.PreferredCategoryVo;
import noctem.purchaseService.purchase.dto.vo.PurchaseStatisticsDayBaseVo;
import noctem.purchaseService.purchase.dto.vo.PurchaseStatisticsHourBaseVo;
import noctem.purchaseService.purchase.dto.vo.PurchaseStatisticsMonthBaseVo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StatisticsServiceImpl implements StatisticsService {
    private final StatisticsRepository statisticsRepository;
    private final ClientInfoLoader clientInfoLoader;

    @Override
    public List<PreferredCategoryResDto> getPreferredCategoryByUser() {
        Map<String, PreferredCategoryResDto> dtoMap = new HashMap<>();
        dtoMap.put("caffeine", new PreferredCategoryResDto("카페인"));
        dtoMap.put("decaffeine", new PreferredCategoryResDto("디카페인"));
        dtoMap.put("smoothie", new PreferredCategoryResDto("스무디"));
        dtoMap.put("ade", new PreferredCategoryResDto("에이드"));
        dtoMap.put("tea", new PreferredCategoryResDto("티"));

        List<PreferredCategoryVo> voList = statisticsRepository.getPreferredCategoryByUser(clientInfoLoader.getUserAccountId());
        voList.forEach(e -> {
            switch (e.getCategorySmall()) {
                case REFRESHER:
                case FIZZIO:
                    dtoMap.get("ade").plusCount(e.getCount());
                    break;
                case COLD_BREW:
                case BLONDE:
                case ESPRESSO:
                    dtoMap.get("caffeine").plusCount(e.getCount());
                    break;
                case DECAFFEINE:
                    dtoMap.get("decaffeine").plusCount(e.getCount());
                    break;
                case PRAPPUCCINO:
                case BLENDED:
                    dtoMap.get("smoothie").plusCount(e.getCount());
                    break;
                case TEAVANA:
                    dtoMap.get("tea").plusCount(e.getCount());
                    break;
                default:
                    break;
            }
        });
        return dtoMap.values().stream().collect(Collectors.toList());
    }

    @Override
    public List<PopularMenuResDto> getPopularMenuTop3ByStore(Long storeId) {
        List<PopularMenuResDto> dtoList = statisticsRepository.findPopularMenuTop3ByStore(storeId);
        dtoList.forEach(e -> e.setRank(dtoList.indexOf(e) + 1));
        return dtoList;
    }

    @Override
    public List<PopularMenuResDto> getPopularMenuTop5() {
        List<PopularMenuResDto> dtoList = statisticsRepository.findPopularMenuTop5();
        dtoList.forEach(e -> e.setRank(dtoList.indexOf(e) + 1));
        return dtoList;
    }

    @Override
    public List<RegularCustomerResDto> getRegularCustomerTop3ByStore() {
        List<RegularCustomerResDto> dtoList = statisticsRepository.findRegularCustomerTop3ByStore(clientInfoLoader.getStoreId());
        dtoList.forEach(e -> e.setRank(dtoList.indexOf(e) + 1));
        return dtoList;
    }

    @Override
    public MonthGraphResDto getMonthGraph() {
        LocalDateTime now = LocalDateTime.now();
        PurchaseStatisticsMonthBaseVo recent = statisticsRepository.findPurchaseStatisticsForMonth(
                clientInfoLoader.getStoreId(), now);

        PurchaseStatisticsMonthBaseVo old = statisticsRepository.findPurchaseStatisticsForMonth(
                clientInfoLoader.getStoreId(), now.minusYears(1));
        return new MonthGraphResDto()
                .inputDate(now, now.minusYears(1))
                .inputData(recent, old);
    }


    @Override
    public DayGraphResDto getDayGraph() {
        LocalDateTime now = LocalDateTime.now();
        PurchaseStatisticsDayBaseVo recent = statisticsRepository.findPurchaseStatisticsForDay(
                clientInfoLoader.getStoreId(), now);

        PurchaseStatisticsDayBaseVo old = statisticsRepository.findPurchaseStatisticsForDay(
                clientInfoLoader.getStoreId(), now.minusWeeks(1));
        return new DayGraphResDto()
                .inputDate(now, now.minusWeeks(1))
                .inputData(recent, old);
    }

    @Override
    public HourGraphResDto getHourGraph() {
        LocalDateTime now = LocalDateTime.now();
        PurchaseStatisticsHourBaseVo recent = statisticsRepository.findPurchaseStatisticsForHour(
                clientInfoLoader.getStoreId(), now);

        PurchaseStatisticsHourBaseVo old = statisticsRepository.findPurchaseStatisticsForHour(
                clientInfoLoader.getStoreId(), now.minusDays(1));
        return new HourGraphResDto()
                .inputDate(now, now.minusDays(1))
                .inputData(recent, old);
    }
}
