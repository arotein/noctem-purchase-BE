package noctem.purchaseService.purchase.domain.repository;

import noctem.purchaseService.purchase.dto.response.PopularMenuResDto;
import noctem.purchaseService.purchase.dto.response.RegularCustomerResDto;
import noctem.purchaseService.purchase.dto.vo.PurchaseStatisticsDayBaseVo;
import noctem.purchaseService.purchase.dto.vo.PurchaseStatisticsHourBaseVo;
import noctem.purchaseService.purchase.dto.vo.PurchaseStatisticsMonthBaseVo;

import java.time.LocalDateTime;
import java.util.List;

public interface StatisticsRepository {
    List<PopularMenuResDto> findPopularMenuTop3ByStore(Long storeId);

    List<PopularMenuResDto> findPopularMenuTop5();

    List<RegularCustomerResDto> findRegularCustomerTop3ByStore(Long storeId);

    PurchaseStatisticsMonthBaseVo findPurchaseStatisticsForMonth(Long storeId, LocalDateTime baseDttm);

    PurchaseStatisticsDayBaseVo findPurchaseStatisticsForDay(Long storeId, LocalDateTime baseDttm);

    PurchaseStatisticsHourBaseVo findPurchaseStatisticsForHour(Long storeId, LocalDateTime baseDttm);
}
