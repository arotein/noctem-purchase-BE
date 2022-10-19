package noctem.purchaseService.purchase.dto.response;

import lombok.Data;

@Data
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
