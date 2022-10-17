package noctem.purchaseService.purchase.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import noctem.purchaseService.purchase.domain.entity.PaymentInfo;
import noctem.purchaseService.purchase.domain.entity.Purchase;
import noctem.purchaseService.purchase.domain.entity.PurchaseMenu;
import noctem.purchaseService.purchase.dto.InnerDto;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
public class ReceiptDetailResDto {
    // 매장 및 결제일 정보
    private String storeName;
    private String storeAddress;
    private String noctemCeo;
    private Long storeId;
    private String storeContactNumber;
    private String approvedAt;
    // 유저 및 결제메뉴 정보
    private String userNickname; // 비회원이면 null
    private Integer storeOrderNumber; // 주문 순서
    private List<InnerDto.ReceiptDetailMenuInnerDto> menuList; // 메뉴 정보
    private Integer purchaseTotalPrice; // 결제 총 금액
    private Integer vat; // 부가세
    private Integer cardPaymentPrice; // 카드 결제금액
    private String purchaseSerialNumber; // 결제 고유번호

    public ReceiptDetailResDto(Purchase purchase) {
        PaymentInfo paymentInfo = purchase.getPaymentInfo();
        List<PurchaseMenu> purchaseMenuList = purchase.getPurchaseMenuList();
        this.storeName = purchase.getStoreName();
        this.storeAddress = purchase.getStoreAddress();
        this.noctemCeo = purchase.getNoctemCeo();
        this.storeId = purchase.getStoreId();
        this.storeContactNumber = purchase.getStoreContactNumber();
        this.approvedAt = paymentInfo.getApprovedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        this.userNickname = purchase.getUserNickname();
        this.storeOrderNumber = purchase.getStoreOrderNumber();
        this.menuList = purchaseMenuList.stream().map(InnerDto.ReceiptDetailMenuInnerDto::new).collect(Collectors.toList());
        this.purchaseTotalPrice = purchase.getPurchaseTotalPrice();
        this.vat = paymentInfo.getVat();
        this.cardPaymentPrice = paymentInfo.getCardPaymentPrice();
        this.purchaseSerialNumber = purchase.getPurchaseSerialNumber();
    }
}
