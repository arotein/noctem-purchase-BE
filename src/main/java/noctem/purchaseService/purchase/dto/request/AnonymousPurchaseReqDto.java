package noctem.purchaseService.purchase.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import noctem.purchaseService.purchase.dto.InnerDto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Getter
@NoArgsConstructor
public class AnonymousPurchaseReqDto {
    // == Purchase ==
    @Min(1)
    private Long storeId;
    @NotBlank
    private String anonymousName;
    @NotBlank
    private String anonymousPhoneNumber;
    @Min(0)
    private Integer anonymousAge;
    @NotBlank
    private String anonymousSex;
    @Min(0)
    private Integer purchaseTotalPrice;
    // private List<Long> usedGifticonList = new ArrayList<>();
    // private List<UsedGiftCard> usedGiftCardList = new ArrayList<>();

    // == PaymentInfo ==
    @NotBlank
    private String cardCorp;
    @Min(0)
    private Integer cardPaymentPrice;

    // == PurchaseMenu ==
    private List<InnerDto.MenuReqDto> menuList;
}
