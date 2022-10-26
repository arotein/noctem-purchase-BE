package noctem.purchaseService.purchase.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import noctem.purchaseService.global.enumeration.Amount;
import noctem.purchaseService.purchase.domain.entity.Purchase;
import noctem.purchaseService.purchase.domain.entity.PurchaseMenu;
import noctem.purchaseService.purchase.domain.entity.PurchasePersonalOption;

import java.time.LocalDateTime;
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
    public static class HourInnerDto {
        private String x; // 시간
        private Long y = 0L; // 매출량

        public HourInnerDto addHour(LocalDateTime dateTime) {
            int hour = dateTime.getHour();
            if (hour >= 10) {
                this.x = String.format("%d:00", hour);
            } else {
                this.x = String.format("0%d:00", hour);
            }
            return this;
        }

        public HourInnerDto addSales(long amount) {
            this.y += amount;
            return this;
        }
    }

    @Data
    @NoArgsConstructor
    public static class DayInnerDto {
        private String x; // 요일
        private Long y = 0L; // 판매량

        public DayInnerDto addDay(LocalDateTime dateTime) {
            switch (dateTime.getDayOfWeek().getValue()) {
                case 1:
                    x = "월";
                    break;
                case 2:
                    x = "화";
                    break;
                case 3:
                    x = "수";
                    break;
                case 4:
                    x = "목";
                    break;
                case 5:
                    x = "금";
                    break;
                case 6:
                    x = "토";
                    break;
                default:
                    x = "일";
            }
            return this;
        }

        public DayInnerDto addSales(long amount) {
            this.y += amount;
            return this;
        }
    }

    @Data
    @NoArgsConstructor
    public static class MonthInnerDto {
        private String x; // 월
        private Long y = 0L; // 판매량

        public MonthInnerDto addMonth(LocalDateTime dateTime) {
            x = String.format("%d월", dateTime.getMonth().getValue());
            return this;
        }

        public MonthInnerDto addSales(long amount) {
            this.y += amount;
            return this;
        }
    }
}