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

/***
 * totalSales: 매출금액
 * totalCount: 주문 건수
 * performanceSales: (이번 매출 - 저번 매출) 매출 증가량 (음수도 가능)
 * performanceCount: (이번 주문건수 - 저번 주문건수) 주문건수 증가량 (음수도 가능)
 */
@Data
@Slf4j
public class HourGraphResDto {
    private Long totalSales;
    private Integer totalCount;
    private Long performanceSales;
    private Integer performanceCount;
    private List<InnerDto.HourInnerDto> recentStatistics;
    private List<InnerDto.HourInnerDto> beforeStatistics;

    public HourGraphResDto() {
        this.totalSales = 0L;
        this.totalCount = 0;
        this.performanceSales = 0L;
        this.performanceCount = 0;
        this.recentStatistics = new ArrayList<>();
        this.beforeStatistics = new ArrayList<>();
    }

    public HourGraphResDto inputData(List<SalesDataVo> beforeHour, List<SalesDataVo> recentHour) {
        // now를 기준으로 -12~0시간까지 분할
        // <hour, dto>
        Map<Integer, InnerDto.HourInnerDto> recentMap = new HashMap<>();
        Map<Integer, InnerDto.HourInnerDto> beforeMap = new HashMap<>();
        for (int k = 0; k <= 12; k++) {
            int time = LocalDateTime.now().getHour() - k;
            InnerDto.HourInnerDto recentHourDto = new InnerDto.HourInnerDto(time);
            recentStatistics.add(recentHourDto);
            recentMap.put(time, recentHourDto);

            InnerDto.HourInnerDto beforeHourDto = new InnerDto.HourInnerDto(time);
            beforeStatistics.add(beforeHourDto);
            beforeMap.put(time, beforeHourDto);
        }
        int recentHourSize = recentHour.size();
        int beforeHourSize = beforeHour.size();
        this.totalCount = recentHourSize;
        this.performanceCount = recentHourSize - beforeHourSize;

        recentHour.forEach(e -> {
            totalSales += e.getPurchaseTotalPrice().longValue();
            performanceSales += e.getPurchaseTotalPrice().longValue();

            recentMap.get(e.getCreatedAt().getHour())
                    .addSales(e.getPurchaseTotalPrice().longValue());
        });

        beforeHour.forEach(e -> {
            performanceSales -= e.getPurchaseTotalPrice().longValue();

            beforeMap.get(e.getCreatedAt().getHour())
                    .addSales(e.getPurchaseTotalPrice().longValue());
        });

        recentStatistics.forEach(e -> {
            e.delIntHour();
            e.setIndex(recentStatistics.indexOf(e));
        });
        beforeStatistics.forEach(e -> {
            e.delIntHour();
            e.setIndex(beforeStatistics.indexOf(e));
        });
        return this;
    }
}