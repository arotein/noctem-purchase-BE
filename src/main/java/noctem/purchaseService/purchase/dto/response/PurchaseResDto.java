package noctem.purchaseService.purchase.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PurchaseResDto {
    private Long storeId;
    private Long purchaseId;
}
