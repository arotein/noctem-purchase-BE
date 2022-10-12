package noctem.purchaseService.purchase.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MenuReceiptInfoResFromServDto {
    private Long sizeId;
    private Long menuId;
    private String menuKorName;
    private String menuShortenName;
    private Integer totalPrice;
}
