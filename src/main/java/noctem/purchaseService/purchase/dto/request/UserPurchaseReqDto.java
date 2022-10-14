package noctem.purchaseService.purchase.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import noctem.purchaseService.purchase.dto.InnerDto;

import java.util.List;

@Getter
@NoArgsConstructor
public class UserPurchaseReqDto {
    // == Purchase ==
    private Long storeId;
    private String storeName;
    private String storeAddress;
    private String storeContactNumber;
    private Integer userAge;
    private String userSex;
    private Integer purchaseTotalPrice;
    // private List<Long> usedGifticonList = new ArrayList<>();
    // private List<UsedGiftCard> usedGiftCardList = new ArrayList<>();

    // == PaymentInfo ==
    private String cardCorp;
    private Integer cardPaymentPrice;

    // == PurchaseMenu ==
    private List<InnerDto.MenuReqDto> menuList;
}
