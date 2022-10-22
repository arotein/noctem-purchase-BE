package noctem.purchaseService.purchase.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
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
        private String menuFullName;
        private String menuShortName;
        private Integer qty;
        private Integer menuTotalPrice;
//        private List<PersonalOptionReqDto> optionList; // 미구현
    }

    @Data
    @NoArgsConstructor
    public static class PersonalOptionReqDto {
        private Long personalOptionId;
        private String personalOptionName;
        private String amount;
        private Integer totalSurcharge; // 총 추가금(샷 추가만 amount의 영향을 받음)
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

    @Data
    @NoArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class HourInnerDto {
        private Integer index;
        private String stringHour;
        private Integer intHour;
        private Long sales; // 기준 시간에서의 매출

        public HourInnerDto(int intHour) {
            this.intHour = intHour;
            this.sales = 0L;
            if (intHour >= 10) {
                this.stringHour = String.format("%d:00", intHour);
            } else {
                this.stringHour = String.format("0%d:00", intHour);
            }
        }

        public HourInnerDto addSales(long amount) {
            this.sales += amount;
            return this;
        }

        public void delIntHour() {
            this.intHour = null;
        }
    }
}