package noctem.purchaseService.purchase.service;


import noctem.purchaseService.purchase.dto.response.PopularMenuResDto;
import noctem.purchaseService.purchase.dto.response.RegularCustomerResDto;

import java.util.List;

public interface StatisticsService {
    List<PopularMenuResDto> getPopularMenuTop3ByStore(Long storeId);

    List<PopularMenuResDto> getPopularMenuTop5();

    List<RegularCustomerResDto> getRegularCustomerTop3ByStore();
}
