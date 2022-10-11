package noctem.purchaseService.purchase.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

public class InnerDto {
    @Data
    @NoArgsConstructor
    public static class MenuReqDto {
        private String menuFullName;
        private String menuShortName;
        private Integer qty;
        private Integer menuTotalPrice; // 옵션 포함 가격
        private List<PersonalOptionReqDto> optionList;
    }

    @Data
    @NoArgsConstructor
    public static class PersonalOptionReqDto {
        private String personalOptionName;
        private String amount;
    }

    @Data
    @AllArgsConstructor
    @ToString
    public static class UserPurchaseResDto {
        private Integer index;
        private Long purchaseId;
        private String storeName;
        private String paymentDttm;
        private Integer totalMenuPrice;
    }
}