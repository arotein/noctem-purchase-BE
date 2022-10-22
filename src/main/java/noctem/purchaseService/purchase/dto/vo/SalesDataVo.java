package noctem.purchaseService.purchase.dto.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class SalesDataVo {
    private LocalDateTime createdAt;
    private Integer purchaseTotalPrice;
}
