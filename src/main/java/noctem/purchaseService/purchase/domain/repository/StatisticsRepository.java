package noctem.purchaseService.purchase.domain.repository;

import noctem.purchaseService.purchase.dto.response.*;
import noctem.purchaseService.purchase.dto.vo.SalesDataVo;

import java.time.LocalDateTime;
import java.util.List;

public interface StatisticsRepository {
    List<PopularMenuResDto> findPopularMenuTop3ByStore(Long storeId);

    List<PopularMenuResDto> findPopularMenuTop5();

    List<RegularCustomerResDto> findRegularCustomerTop3ByStore(Long storeId);

    List<SalesDataVo> findMonthGraph(Long storeId, LocalDateTime startTime, LocalDateTime endTime);

    List<SalesDataVo> findWeekGraph(Long storeId, LocalDateTime startTime, LocalDateTime endTime);

    List<SalesDataVo> findDayGraph(Long storeId, LocalDateTime startTime, LocalDateTime endTime);

    List<SalesDataVo> findHourGraph(Long storeId, LocalDateTime startTime, LocalDateTime endTime);
}
