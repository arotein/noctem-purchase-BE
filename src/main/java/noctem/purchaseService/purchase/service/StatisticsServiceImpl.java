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
    public List<MonthGraphResDto> getMonthGraph() {
        return null;
    }

    @Override
    public List<WeekGraphResDto> getWeekGraph() {
        return null;
    }

    @Override
    public List<DayGraphResDto> getDayGraph() {
        return null;
    }

    @Override
    public HourGraphResDto getHourGraph() {
        List<SalesDataVo> beforeHour = statisticsRepository.findHourGraph(
                clientInfoLoader.getStoreId(),
                LocalDateTime.now().minusHours(36),
                LocalDateTime.now().minusHours(24));

        List<SalesDataVo> recentHour = statisticsRepository.findHourGraph(
                clientInfoLoader.getStoreId(),
                LocalDateTime.now().minusHours(12),
                LocalDateTime.now());

        return new HourGraphResDto().inputData(beforeHour, recentHour);
    }
}
