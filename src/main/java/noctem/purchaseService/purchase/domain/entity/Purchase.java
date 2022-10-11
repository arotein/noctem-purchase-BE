package noctem.purchaseService.purchase.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import noctem.purchaseService.global.common.BaseEntity;
import noctem.purchaseService.global.enumeration.Sex;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/***
 * storePurchaseNumber: 해당 매장의 주문 번호
 * userId: 구매한 유저 id. 비회원 구매일 때 null
 * anonymousName: 비회원 구매시 입력한 이름
 * anonymousPhoneNumber: 비회원 구매시 입력한 휴대폰 번호
 * anonymousSex: 성별(선택사항)
 * anonymousAge: 연령(선택사항)
 * giftId: 선물id. null이면 선물 아님
 * totalPrice: 총 금액
 * usedGifticonList: 사용된 기프티콘들 id
 * usedGiftCardList: 사용된 기프트카드들
 * paymentInfo: 카드 결제 정보
 */
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class Purchase extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "purchase_id")
    private Long id;
    private String noctemCEO = "박찬우";
    private Long storeId;
    private Integer storePurchaseNumber;
    private String storeName;
    private String storeAddress;
    private String storeContactNumber;
    private Long userAccountId;
    private String userNickname;
    private String anonymousName;
    private String anonymousPhoneNumber;
    private Sex anonymousSex;
    private Integer anonymousAge;
    private Integer purchaseTotalPrice;
    private Long giftId;
    @ElementCollection
    private List<Long> usedGifticonList = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "purchase", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private List<PurchaseMenu> purchaseMenuList = new ArrayList<>();
    @JsonIgnore
    @OneToMany(mappedBy = "purchase", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private List<UsedGiftCard> usedGiftCardList = new ArrayList<>();
    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "payment_info_id")
    private PaymentInfo paymentInfo;

    @Builder
    public Purchase(Long storeId, Integer storePurchaseNumber, String storeName, String storeAddress, String storeContactNumber, Long userAccountId, String userNickname, String anonymousName, String anonymousPhoneNumber, Sex anonymousSex, Integer anonymousAge, Integer purchaseTotalPrice, Long giftId) {
        this.storeId = storeId;
        this.storePurchaseNumber = storePurchaseNumber;
        this.storeName = storeName;
        this.storeAddress = storeAddress;
        this.storeContactNumber = storeContactNumber;
        this.userAccountId = userAccountId;
        this.userNickname = userNickname;
        this.anonymousName = anonymousName;
        this.anonymousPhoneNumber = anonymousPhoneNumber;
        this.anonymousSex = anonymousSex;
        this.anonymousAge = anonymousAge;
        this.purchaseTotalPrice = purchaseTotalPrice;
        this.giftId = giftId;
    }

    public Purchase linkToPurchaseMenuList(List<PurchaseMenu> purchaseMenuList) {
        this.purchaseMenuList.addAll(purchaseMenuList);
        purchaseMenuList.forEach(e -> e.linkToPurchase(this));
        return this;
    }

    public Purchase linkToUsedGiftCard(UsedGiftCard usedGiftCard) {
        this.usedGiftCardList.add(usedGiftCard);
        return this;
    }

    public Purchase linkToPaymentInfo(PaymentInfo paymentInfo) {
        this.paymentInfo = paymentInfo;
        paymentInfo.linkToPurchase(this);
        return this;
    }
}
