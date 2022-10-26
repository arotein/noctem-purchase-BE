package noctem.purchaseService.purchase.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import noctem.purchaseService.purchase.dto.InnerDto;
import noctem.purchaseService.purchase.dto.vo.PurchaseStatisticsDayBaseVo;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DayGraphResDto {
    private Long totalSales;
    private Long totalCount;
    private Long performanceSales;
    private Long performanceCount;
    private List<InnerDto.DayInnerDto> recentStatistics;
    private List<InnerDto.DayInnerDto> beforeStatistics;
    private LocalDateTime recentDttm;
    private LocalDateTime oldDttm;

    public DayGraphResDto() {
        this.totalSales = 0L;
        this.totalCount = 0L;
        this.performanceSales = 0L;
        this.performanceCount = 0L;
        this.recentStatistics = new ArrayList<>();
        this.beforeStatistics = new ArrayList<>();
    }

    public DayGraphResDto inputDate(LocalDateTime recentDttm, LocalDateTime oldDttm) {
        this.recentDttm = recentDttm;
        this.oldDttm = oldDttm;
        return this;
    }

    public DayGraphResDto inputData(PurchaseStatisticsDayBaseVo recentData, PurchaseStatisticsDayBaseVo oldData) {
        List<Long> recentSalesDataList = recentData.outputSalesList();
        List<Long> oldSalesDataList = oldData.outputSalesList();

        recentSalesDataList.forEach(e -> totalSales += e);
        totalCount = recentData.getTotalCountNullSafe();

        performanceSales = totalSales;
        performanceCount = totalCount;

        oldSalesDataList.forEach(e -> performanceSales -= e);
        performanceCount -= oldData.getTotalCountNullSafe();

        for (int k = 0; k <= 6; k++) {
            recentStatistics.add(new InnerDto.DayInnerDto()
                    .addSales(recentSalesDataList.get(k))
                    .addDay(recentDttm.minusDays(6 - k)));

            beforeStatistics.add(new InnerDto.DayInnerDto()
                    .addSales(oldSalesDataList.get(k))
                    .addDay(oldDttm.minusDays(6 - k)));
        }
        recentDttm = null;
        oldDttm = null;
        return this;
    }
}
