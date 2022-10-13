package noctem.purchaseService.purchase.dto.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import noctem.purchaseService.global.enumeration.Sex;
import noctem.purchaseService.purchase.domain.entity.PaymentInfo;
import noctem.purchaseService.purchase.domain.entity.Purchase;
import noctem.purchaseService.purchase.dto.InnerDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/***
 * storeOrderNumber: 해당 매장의 주문 순서
 * purchaseTotalPrice: 결제 총 금액
 * vat: 부가세
 * cardCorp: 카드사
 * cardPaymentPrice: 카드 결제금액
 * purchaseSerialNumber: 결제 고유번호
 * approvedAt: 결제일
 */
@Getter
@AllArgsConstructor
public class PurchaseEventDto {
    // 매장 정보
    private String noctemCEO;
    private Long storeId;
    private String storeName;
    private String storeAddress;
    private String storeContactNumber;
    // 회원 정보
    private Long userAccountId;
    private String userNickname;
    private Sex userSex;
    private Integer userAge;
    // 비회원 정보
    private String anonymousName;
    private String anonymousPhoneNumber;
    private Sex anonymousSex;
    private Integer anonymousAge;
    // 결제 정보
    private Integer storeOrderNumber;
    private List<InnerDto.PurchaseEventMenuInnerDto> menuList;
    private Integer purchaseTotalPrice;
    private Integer vat;
    private String cardCorp;
    private Integer cardPaymentPrice;
    private String purchaseSerialNumber;
    private LocalDateTime approvedAt;

    public PurchaseEventDto(Purchase purchase) {
        PaymentInfo paymentInfo = purchase.getPaymentInfo();
        this.storeId = purchase.getStoreId();
        this.storeOrderNumber = purchase.getStoreOrderNumber();
        this.noctemCEO = purchase.getNoctemCEO();
        this.userAccountId = purchase.getUserAccountId();
        this.userNickname = purchase.getUserNickname();
        this.anonymousName = purchase.getAnonymousName();
        this.anonymousPhoneNumber = purchase.getAnonymousPhoneNumber();
        this.menuList = purchase.getPurchaseMenuList().stream()
                .map(InnerDto.PurchaseEventMenuInnerDto::new).collect(Collectors.toList());
        this.purchaseTotalPrice = purchase.getPurchaseTotalPrice();
        this.vat = paymentInfo.getVat();
        this.cardCorp = paymentInfo.getCardCorp();
        this.cardPaymentPrice = paymentInfo.getCardPaymentPrice();
        this.purchaseSerialNumber = purchase.getPurchaseSerialNumber();
        this.approvedAt = paymentInfo.getApprovedAt();
    }

    public PurchaseEventDto addAdditionalAnonymousInfo(Sex anonymousSex, Integer anonymousAge) {
        this.anonymousSex = anonymousSex;
        this.anonymousAge = anonymousAge;
        return this;
    }

    public PurchaseEventDto addUserInfo(Sex userSex, Integer userAge) {
        this.userSex = userSex;
        this.userAge = userAge;
        return this;
    }

    public PurchaseEventDto addStoreInfo(String storeName, String storeAddress, String storeContactNumber) {
        this.storeName = storeName;
        this.storeAddress = storeAddress;
        this.storeContactNumber = storeContactNumber;
        return this;
    }
}
