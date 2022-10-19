package noctem.purchaseService.purchase.domain.repository;

import noctem.purchaseService.purchase.dto.response.PopularMenuResDto;

import java.util.List;

public interface StatisticsRepository {
    List<PopularMenuResDto> findPopularMenuTop3ByStore(Long storeId);

    List<PopularMenuResDto> findPopularMenuTop5();
}
