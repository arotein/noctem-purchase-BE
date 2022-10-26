package noctem.purchaseService.purchase.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import noctem.purchaseService.purchase.dto.InnerDto;
import noctem.purchaseService.purchase.dto.vo.PurchaseStatisticsHourBaseVo;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/***
 * totalSales: 현재기준 매출금액
 * totalCount: 현재기준 총 주문 건수
 * performanceSales: (이번 매출 - 저번 매출) 매출 증가량 (음수도 가능)
 * performanceCount: (이번 주문건수 - 저번 주문건수) 주문건수 증가량 (음수도 가능)
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class HourGraphResDto {
    private Long totalSales;
    private Long totalCount;
    private Long performanceSales;
    private Long performanceCount;
    private List<InnerDto.HourInnerDto> recentStatistics;
    private List<InnerDto.HourInnerDto> beforeStatistics;
    private LocalDateTime recentDttm;
    private LocalDateTime oldDttm;

    public HourGraphResDto() {
        this.totalSales = 0L;
        this.totalCount = 0L;
        this.performanceSales = 0L;
        this.performanceCount = 0L;
        this.recentStatistics = new ArrayList<>();
        this.beforeStatistics = new ArrayList<>();
    }

    public HourGraphResDto inputDate(LocalDateTime recentDttm, LocalDateTime oldDttm) {
        this.recentDttm = recentDttm;
        this.oldDttm = oldDttm;
        return this;
    }

    public HourGraphResDto inputData(PurchaseStatisticsHourBaseVo recentData, PurchaseStatisticsHourBaseVo oldData) {
        List<Long> recentSalesDataList = recentData.outputSalesList();
        List<Long> oldSalesDataList = oldData.outputSalesList();

        recentSalesDataList.forEach(e -> totalSales += e);
        totalCount = recentData.getTotalCountNullSafe();

        performanceSales = totalSales;
        performanceCount = totalCount;

        oldSalesDataList.forEach(e -> performanceSales -= e);
        performanceCount -= oldData.getTotalCountNullSafe();

        for (int k = 0; k <= 11; k++) {
            recentStatistics.add(new InnerDto.HourInnerDto()
                    .addSales(recentSalesDataList.get(k))
                    .addHour(recentDttm.minusHours(11 - k)));

            beforeStatistics.add(new InnerDto.HourInnerDto()
                    .addSales(oldSalesDataList.get(k))
                    .addHour(oldDttm.minusHours(11 - k)));
        }
        recentDttm = null;
        oldDttm = null;
        return this;
    }
}