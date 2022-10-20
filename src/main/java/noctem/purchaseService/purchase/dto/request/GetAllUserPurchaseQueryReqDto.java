package noctem.purchaseService.purchase.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.NotBlank;

@Slf4j
@Data
@NoArgsConstructor
public class GetAllUserPurchaseQueryReqDto {
    @NotBlank
    private String startDate;
    @NotBlank
    private String endDate;
}
