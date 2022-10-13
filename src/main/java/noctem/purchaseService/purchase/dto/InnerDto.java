package noctem.purchaseService.purchase.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import noctem.purchaseService.global.enumeration.Amount;
import noctem.purchaseService.purchase.domain.entity.Purchase;
import noctem.purchaseService.purchase.domain.entity.PurchaseMenu;
import noctem.purchaseService.purchase.domain.entity.PurchasePersonalOption;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class InnerDto {
    @Data
    @NoArgsConstructor
    public static class MenuReqDto {
        private Long sizeId;
        private Integer qty;
        private List<PersonalOptionReqDto> optionList;
    }

    @Data
    @NoArgsConstructor
    public static class PersonalOptionReqDto {
        private Long personalOptionId;
        private String amount;
    }

    @Data
    @AllArgsConstructor
    public static class UserPurchaseResDto {
        private Integer index;
        private String purchaseSerialNumber;
        private String storeName;
        private String paymentDttm;
        private Integer totalPurchasePrice;

        public UserPurchaseResDto(Purchase purchase) {
            this.purchaseSerialNumber = purchase.getPurchaseSerialNumber();
            this.storeName = purchase.getStoreName();
            this.paymentDttm = purchase.getPaymentInfo().getApprovedAt()
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
            this.totalPurchasePrice = purchase.getPurchaseTotalPrice();
        }
    }

    @Data
    @AllArgsConstructor
    public static class ReceiptDetailMenuInnerDto {
        private String menuShortName;
        private Integer qty;
        private List<ReceiptDetailPersonalOptionReqDto> personalOptionList;

        public ReceiptDetailMenuInnerDto(PurchaseMenu menuList) {
            this.menuShortName = menuList.getMenuShortName();
            this.qty = menuList.getQty();
            this.personalOptionList = menuList.getPurchasePersonalOptionList().stream()
                    .map(ReceiptDetailPersonalOptionReqDto::new)
                    .collect(Collectors.toList());
        }
    }

    @Data
    @NoArgsConstructor
    public static class ReceiptDetailPersonalOptionReqDto {
        private String personalOptionName;
        private String amount;
        private Integer totalSurcharge; // 퍼스널 옵션에 대한 추가금

        public ReceiptDetailPersonalOptionReqDto(PurchasePersonalOption purchasePersonalOption) {
            this.personalOptionName = purchasePersonalOption.getPersonalOptionName();
            this.amount = purchasePersonalOption.getAmount().getValue();
            this.totalSurcharge = purchasePersonalOption.getTotalSurcharge();
        }
    }

    @Data
    @NoArgsConstructor
    public static class PurchaseEventMenuInnerDto {
        private Long sizeId;
        private String menuFullName;
        private Integer qty;
        private Integer menuTotalPrice;
        private List<PurchaseEventOptionInnerDto> personalOptionList;

        public PurchaseEventMenuInnerDto(PurchaseMenu purchaseMenu) {
            this.sizeId = purchaseMenu.getSizeId();
            this.menuFullName = purchaseMenu.getMenuFullName();
            this.qty = purchaseMenu.getQty();
            this.menuTotalPrice = purchaseMenu.getMenuTotalPrice();
            this.personalOptionList = purchaseMenu.getPurchasePersonalOptionList()
                    .stream().map(PurchaseEventOptionInnerDto::new)
                    .collect(Collectors.toList());
        }
    }

    @Data
    @NoArgsConstructor
    public static class PurchaseEventOptionInnerDto {
        private String personalOptionName;
        private Amount amount;
        private Integer totalSurcharge;

        public PurchaseEventOptionInnerDto(PurchasePersonalOption purchasePersonalOption) {
            this.personalOptionName = purchasePersonalOption.getPersonalOptionName();
            this.amount = purchasePersonalOption.getAmount();
            this.totalSurcharge = purchasePersonalOption.getTotalSurcharge();
        }
    }
}