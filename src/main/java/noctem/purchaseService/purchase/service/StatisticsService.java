package noctem.purchaseService.purchase.service;


import noctem.purchaseService.purchase.dto.response.*;

import java.util.List;

public interface StatisticsService {
    List<PopularMenuResDto> getPopularMenuTop3ByStore(Long storeId);

    List<PopularMenuResDto> getPopularMenuTop5();

    List<RegularCustomerResDto> getRegularCustomerTop3ByStore();

    List<MonthGraphResDto> getMonthGraph();

    List<WeekGraphResDto> getWeekGraph();

    List<DayGraphResDto> getDayGraph();

    HourGraphResDto getHourGraph();
}
