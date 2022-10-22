package noctem.purchaseService.purchase.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import noctem.purchaseService.global.security.bean.ClientInfoLoader;
import noctem.purchaseService.purchase.domain.repository.StatisticsRepository;
import noctem.purchaseService.purchase.dto.response.*;
import noctem.purchaseService.purchase.dto.vo.SalesDataVo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StatisticsServiceImpl implements StatisticsService {
    private final StatisticsRepository statisticsRepository;
    private final ClientInfoLoader clientInfoLoader;

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
        List<SalesDataVo> beforeVoList = statisticsRepository.findPurchaseStatistics(
                clientInfoLoader.getStoreId(),
                LocalDateTime.now().minusYears(2),
                LocalDateTime.now().minusYears(1));

        List<SalesDataVo> recentVoList = statisticsRepository.findPurchaseStatistics(
                clientInfoLoader.getStoreId(),
                LocalDateTime.now().minusYears(1),
                LocalDateTime.now());
        return new MonthGraphResDto().inputData(beforeVoList, recentVoList);
    }

    @Override
    public List<WeekGraphResDto> getWeekGraph() {
        return null;
    }

    @Override
    public DayGraphResDto getDayGraph() {
        List<SalesDataVo> beforeDay = statisticsRepository.findPurchaseStatistics(
                clientInfoLoader.getStoreId(),
                LocalDateTime.now().minusDays(14),
                LocalDateTime.now().minusDays(7));

        List<SalesDataVo> recentDay = statisticsRepository.findPurchaseStatistics(
                clientInfoLoader.getStoreId(),
                LocalDateTime.now().minusDays(7),
                LocalDateTime.now());
        return new DayGraphResDto().inputData(beforeDay, recentDay);
    }

    @Override
    public HourGraphResDto getHourGraph() {
        List<SalesDataVo> beforeHour = statisticsRepository.findPurchaseStatistics(
                clientInfoLoader.getStoreId(),
                LocalDateTime.now().minusHours(36),
                LocalDateTime.now().minusHours(24));

        List<SalesDataVo> recentHour = statisticsRepository.findPurchaseStatistics(
                clientInfoLoader.getStoreId(),
                LocalDateTime.now().minusHours(12),
                LocalDateTime.now());

        return new HourGraphResDto().inputData(beforeHour, recentHour);
    }
}
