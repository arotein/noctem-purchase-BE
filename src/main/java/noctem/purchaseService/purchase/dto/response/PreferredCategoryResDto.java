package noctem.purchaseService.purchase.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PreferredCategoryResDto {
    private Integer index;
    private String menuType;
    private Long count;

    public PreferredCategoryResDto(String menuType) {
        this.menuType = menuType;
        this.count = 0L;
    }

    public void plusCount(Long count) {
        this.count += count;
    }
}
