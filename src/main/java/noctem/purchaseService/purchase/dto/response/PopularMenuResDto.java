package noctem.purchaseService.purchase.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PopularMenuResDto {
    private Integer index;
    private Integer rank;
    private Long sizeId;
    private Integer totalCount;

    public PopularMenuResDto(Long sizeId, Integer totalCount) {
        this.sizeId = sizeId;
        this.totalCount = totalCount;
    }
}
