package noctem.purchaseService.purchase.dto.response;

import lombok.Data;

@Data
public class PopularMenuResDto {
    private Integer index;
    private Integer rank;
    private Long temperatureId;
    private Integer totalCount;

    public PopularMenuResDto(Long temperatureId, Integer totalCount) {
        this.temperatureId = temperatureId;
        this.totalCount = totalCount;
    }
}
