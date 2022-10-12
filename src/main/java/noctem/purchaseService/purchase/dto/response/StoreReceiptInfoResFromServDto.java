package noctem.purchaseService.purchase.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StoreReceiptInfoResFromServDto {
    private String storeName;
    private String storeAddress;
    private String storeContactNumber;
}
