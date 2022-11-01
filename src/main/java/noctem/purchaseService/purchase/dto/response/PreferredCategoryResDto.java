package noctem.purchaseService.purchase.dto.response;

import lombok.Data;

@Data
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
