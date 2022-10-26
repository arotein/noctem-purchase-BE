package noctem.purchaseService.purchase.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import noctem.purchaseService.global.common.CommonResponse;
import noctem.purchaseService.purchase.dto.response.*;
import noctem.purchaseService.purchase.service.StatisticsService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("${global.api.base-path}/statistics")
@RequiredArgsConstructor
public class StatisticsController {
    private final StatisticsService statisticsService;

    // 해당 매장의 인기메뉴
    @GetMapping("/{storeId}/popularMenu")
    public CommonResponse getPopularMenuTop3ByStore(@PathVariable Long storeId) {
        List<PopularMenuResDto> dtoList = statisticsService.getPopularMenuTop3ByStore(storeId);
        dtoList.forEach(e -> e.setIndex(dtoList.indexOf(e)));
        return CommonResponse.builder()
                .data(dtoList)
                .build();
    }

    // 인기메뉴 전체 조회
    @GetMapping("/popularMenu")
    public CommonResponse getPopularMenuTop5() {
        List<PopularMenuResDto> dtoList = statisticsService.getPopularMenuTop5();
        dtoList.forEach(e -> e.setIndex(dtoList.indexOf(e)));
        return CommonResponse.builder()
                .data(dtoList)
                .build();
    }

    // 해당 매장의 성별+연령별 방문자
    @PreAuthorize("hasRole('STORE')")
    @GetMapping("/customer")
    public CommonResponse getPopularAgeTop3ByStore() {
        List<RegularCustomerResDto> dtoList = statisticsService.getRegularCustomerTop3ByStore();
        dtoList.forEach(e -> e.setIndex(dtoList.indexOf(e)));
        return CommonResponse.builder()
                .data(dtoList)
                .build();
    }

    // x축 = 월
    @PreAuthorize("hasRole('STORE')")
    @GetMapping("/sales/month")
    public CommonResponse getMonthGraph() {
        return CommonResponse.builder()
                .data(statisticsService.getMonthGraph())
                .build();
    }

    // x축 = 일
    @PreAuthorize("hasRole('STORE')")
    @GetMapping("/sales/day")
    public CommonResponse getDayGraph() {
        return CommonResponse.builder()
                .data(statisticsService.getDayGraph())
                .build();
    }

    // x축 = 시간
    @PreAuthorize("hasRole('STORE')")
    @GetMapping("/sales/hour")
    public CommonResponse getHourGraph() {
        return CommonResponse.builder()
                .data(statisticsService.getHourGraph())
                .build();
    }
}
