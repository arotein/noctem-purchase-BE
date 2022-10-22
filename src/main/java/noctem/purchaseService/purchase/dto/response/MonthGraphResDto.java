package noctem.purchaseService.purchase.dto.response;

import lombok.Data;
import noctem.purchaseService.purchase.dto.InnerDto;
import noctem.purchaseService.purchase.dto.vo.SalesDataVo;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class MonthGraphResDto {
    private Long totalSales;
    private Integer totalCount;
    private Long performanceSales;
    private Integer performanceCount;
    private List<InnerDto.MonthInnerDto> recentStatistics;
    private List<InnerDto.MonthInnerDto> beforeStatistics;

    public MonthGraphResDto() {
        this.totalSales = 0L;
        this.totalCount = 0;
        this.performanceSales = 0L;
        this.performanceCount = 0;
        this.recentStatistics = new ArrayList<>();
        this.beforeStatistics = new ArrayList<>();
    }

    public MonthGraphResDto inputData(List<SalesDataVo> beforeVoList, List<SalesDataVo> recentVoList) {
        // <hour, dto>
        Map<Integer, InnerDto.MonthInnerDto> recentMap = new HashMap<>();
        Map<Integer, InnerDto.MonthInnerDto> beforeMap = new HashMap<>();
        for (int k = 0; k <= 11; k++) {
            LocalDateTime recentDateTime = LocalDateTime.now().minusMonths(k);
            int recentMonth = recentDateTime.getMonthValue();
            InnerDto.MonthInnerDto recentMonthDto = new InnerDto.MonthInnerDto().addMonth(recentDateTime);
            recentStatistics.add(recentMonthDto);
            recentMap.put(recentMonth, recentMonthDto);

            LocalDateTime beforeDateTime = recentDateTime.minusYears(1);
            int beforeDayOfMonth = recentDateTime.getDayOfMonth();
            InnerDto.MonthInnerDto beforeMonthDto = new InnerDto.MonthInnerDto().addMonth(beforeDateTime);
            beforeStatistics.add(beforeMonthDto);
            beforeMap.put(beforeDayOfMonth, beforeMonthDto);
        }
        int recentDaySize = recentVoList.size();
        int beforeDaySize = beforeVoList.size();
        this.totalCount = recentDaySize;
        this.performanceCount = recentDaySize - beforeDaySize;

        recentVoList.forEach(e -> {
            totalSales += e.getPurchaseTotalPrice().longValue();
            performanceSales += e.getPurchaseTotalPrice().longValue();
            recentMap.get(e.getCreatedAt().getMonthValue())
                    .addSales(e.getPurchaseTotalPrice().longValue());
        });

        beforeVoList.forEach(e -> {
            performanceSales -= e.getPurchaseTotalPrice().longValue();

            beforeMap.get(e.getCreatedAt().getMonthValue())
                    .addSales(e.getPurchaseTotalPrice().longValue());
        });

        recentStatistics.forEach(e -> {
            e.delIntMonth();
            e.setIndex(recentStatistics.indexOf(e));
        });
        beforeStatistics.forEach(e -> {
            e.delIntMonth();
            e.setIndex(beforeStatistics.indexOf(e));
        });
        return this;
    }
}
