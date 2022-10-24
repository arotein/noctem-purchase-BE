package noctem.purchaseService.purchase.dto.response;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import noctem.purchaseService.purchase.dto.InnerDto;
import noctem.purchaseService.purchase.dto.vo.SalesDataVo;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@Slf4j
public class DayGraphResDto {
    private Long totalSales;
    private Integer totalCount;
    private Long performanceSales;
    private Integer performanceCount;
    private List<InnerDto.DayInnerDto> recentStatistics;
    private List<InnerDto.DayInnerDto> beforeStatistics;

    public DayGraphResDto() {
        this.totalSales = 0L;
        this.totalCount = 0;
        this.performanceSales = 0L;
        this.performanceCount = 0;
        this.recentStatistics = new ArrayList<>();
        this.beforeStatistics = new ArrayList<>();
    }

    public DayGraphResDto inputData(List<SalesDataVo> beforeDay, List<SalesDataVo> recentDay) {
        // <hour, dto>
        Map<Integer, InnerDto.DayInnerDto> recentMap = new HashMap<>();
        Map<Integer, InnerDto.DayInnerDto> beforeMap = new HashMap<>();
        for (int k = 0; k <= 7; k++) {
            LocalDateTime recentDateTime = LocalDateTime.now().minusDays(k);
            int recentDayOfMonth = recentDateTime.getDayOfMonth();
            InnerDto.DayInnerDto recentDayDto = new InnerDto.DayInnerDto().addDay(recentDateTime);
            recentStatistics.add(recentDayDto);
            recentMap.put(recentDayOfMonth, recentDayDto);

            LocalDateTime beforeDateTime = recentDateTime.minusDays(7);
            int beforeDayOfMonth = beforeDateTime.getDayOfMonth();
            InnerDto.DayInnerDto beforeDayDto = new InnerDto.DayInnerDto().addDay(beforeDateTime);
            beforeStatistics.add(beforeDayDto);
            beforeMap.put(beforeDayOfMonth, beforeDayDto);
        }
        int recentDaySize = recentDay.size();
        int beforeDaySize = beforeDay.size();
        this.totalCount = recentDaySize;
        this.performanceCount = recentDaySize - beforeDaySize;

        recentDay.forEach(e -> {
            totalSales += e.getPurchaseTotalPrice().longValue();
            performanceSales += e.getPurchaseTotalPrice().longValue();

            InnerDto.DayInnerDto dayInnerDto = recentMap.get(e.getCreatedAt().getDayOfMonth());
            if (dayInnerDto != null) {
                dayInnerDto.addSales(e.getPurchaseTotalPrice().longValue());
            } else {
                log.warn("non-existent date. recentDay.getDayOfMonth={}", e.getCreatedAt().getDayOfMonth());
            }
        });

        beforeDay.forEach(e -> {
            performanceSales -= e.getPurchaseTotalPrice().longValue();

            InnerDto.DayInnerDto dayInnerDto = beforeMap.get(e.getCreatedAt().getDayOfMonth());
            if (dayInnerDto != null) {
                dayInnerDto.addSales(e.getPurchaseTotalPrice().longValue());
            } else {
                log.warn("non-existent date. beforeDay.getDayOfMonth={}", e.getCreatedAt().getDayOfMonth());
            }
        });

        recentStatistics.forEach(e -> {
            e.delIntDay();
            e.setIndex(recentStatistics.indexOf(e));
        });
        beforeStatistics.forEach(e -> {
            e.delIntDay();
            e.setIndex(beforeStatistics.indexOf(e));
        });
        return this;
    }
}
