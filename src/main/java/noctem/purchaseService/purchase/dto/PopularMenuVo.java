package noctem.purchaseService.purchase.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PopularMenuVo {
    private String menuFullName;
    private Integer totalCount;
}
