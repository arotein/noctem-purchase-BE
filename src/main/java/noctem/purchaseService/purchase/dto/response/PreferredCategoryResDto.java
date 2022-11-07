package noctem.purchaseService.purchase.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PreferredCategoryResDto {
    private String id;
    private String label;
    private Long value;
    private String color;

    public PreferredCategoryResDto(String id, String color) {
        this.id = id;
        this.label = id;
        this.value = 0L;
        this.color = color;
    }

    public void plusCount(Long count) {
        this.value += count;
    }
}
