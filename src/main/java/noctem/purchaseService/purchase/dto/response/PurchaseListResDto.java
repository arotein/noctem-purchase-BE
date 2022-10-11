package noctem.purchaseService.purchase.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import noctem.purchaseService.purchase.dto.InnerDto;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Data
@NoArgsConstructor
public class PurchaseListResDto {
    private Integer count; // 검색된 결제 건수
    private Integer allPurchaseTotalPrice; // 총 금액
    private List<InnerDto.UserPurchaseResDto> purchaseList;

    public PurchaseListResDto(List<InnerDto.UserPurchaseResDto> purchaseList) {
        this.count = purchaseList.size();
        this.allPurchaseTotalPrice = 0;
        this.purchaseList = new ArrayList<>();
        this.purchaseList.addAll(purchaseList);
        log.info("purchaseList={}", purchaseList);
        log.info("this.purchaseList={}", this.purchaseList);
        purchaseList.forEach(e -> this.allPurchaseTotalPrice += e.getTotalMenuPrice());
        this.purchaseList.forEach(e -> e.setIndex(purchaseList.indexOf(e)));
    }
}
