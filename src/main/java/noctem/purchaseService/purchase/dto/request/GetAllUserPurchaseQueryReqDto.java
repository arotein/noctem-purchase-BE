package noctem.purchaseService.purchase.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@NoArgsConstructor
public class GetAllUserPurchaseQueryReqDto {
    private String startDate;
    private String endDate;
}
