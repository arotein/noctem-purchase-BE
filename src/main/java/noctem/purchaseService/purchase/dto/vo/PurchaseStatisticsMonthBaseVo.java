package noctem.purchaseService.purchase.dto.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@ToString
public class PurchaseStatisticsMonthBaseVo {
    private Long sales0;
    private Long sales1;
    private Long sales2;
    private Long sales3;
    private Long sales4;
    private Long sales5;
    private Long sales6;
    private Long sales7;
    private Long sales8;
    private Long sales9;
    private Long sales10;
    private Long sales11; // 가장 최근
    private Long totalCount; // 총 주문 건수

    public List<Long> outputSalesList() {
        List<Long> recentDataList = new ArrayList<>();
        recentDataList.add(sales0);
        recentDataList.add(sales1);
        recentDataList.add(sales2);
        recentDataList.add(sales3);
        recentDataList.add(sales4);
        recentDataList.add(sales5);
        recentDataList.add(sales6);
        recentDataList.add(sales7);
        recentDataList.add(sales8);
        recentDataList.add(sales9);
        recentDataList.add(sales10);
        recentDataList.add(sales11);
        return recentDataList.stream().map(e -> {
            if (e == null) {
                return 0L;
            }
            return e;
        }).collect(Collectors.toList());
    }

    public Long getTotalCountNullSafe() {
        if (totalCount == null) {
            return 0L;
        }
        return totalCount;
    }
}
