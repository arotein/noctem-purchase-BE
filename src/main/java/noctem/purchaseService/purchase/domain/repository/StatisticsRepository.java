package noctem.purchaseService.purchase.domain.repository;

import noctem.purchaseService.purchase.dto.response.PopularMenuResDto;
import noctem.purchaseService.purchase.dto.response.RegularCustomerResDto;

import java.util.List;

public interface StatisticsRepository {
    List<PopularMenuResDto> findPopularMenuTop3ByStore(Long storeId);

    List<PopularMenuResDto> findPopularMenuTop5();

    List<RegularCustomerResDto> findRegularCustomerTop3ByStore(Long storeId);
}
