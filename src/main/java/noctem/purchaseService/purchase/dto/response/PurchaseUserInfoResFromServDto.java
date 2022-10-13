package noctem.purchaseService.purchase.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import noctem.purchaseService.global.enumeration.Sex;

@Data
@AllArgsConstructor
public class PurchaseUserInfoResFromServDto {
    private Sex userSex;
    private Integer userAge;
}
