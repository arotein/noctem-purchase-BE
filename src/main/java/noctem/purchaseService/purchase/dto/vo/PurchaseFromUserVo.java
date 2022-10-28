package noctem.purchaseService.purchase.dto.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PurchaseFromUserVo {
    private Long storeId;
    private Integer totalMenuQty;
}
