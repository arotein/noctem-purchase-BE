package noctem.purchaseService.purchase.dto.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import noctem.purchaseService.global.enumeration.CategorySmall;

@Getter
@AllArgsConstructor
public class PreferredCategoryVo {
    private CategorySmall categorySmall;
    private Long count;
}
