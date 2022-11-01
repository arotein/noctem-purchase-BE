package noctem.purchaseService.dev;

import lombok.Data;
import noctem.purchaseService.global.enumeration.CupType;
import org.apache.commons.lang3.RandomStringUtils;

@Data
public class AddDummyVo {
    private Long sizeId;
    private String categorySmall;
    private String menuFullName;
    private String menuShortName;
    private CupType cupType;
    private Integer menuTotalPrice;
    private Integer qty;

    public AddDummyVo() {
        this.qty = 1 + Integer.valueOf(RandomStringUtils.randomNumeric(1)) % 2;
        Integer randInt = Integer.valueOf(RandomStringUtils.randomNumeric(2));
        switch (randInt % 3) {
            case 0:
                this.cupType = CupType.DISPOSABLE_CUP;
                break;
            case 1:
                this.cupType = CupType.STORE_CUP;
                break;
            default:
                this.cupType = CupType.PERSONAL_CUP;
                break;
        }
        switch (randInt % 8) {
            case 0:
                this.sizeId = 1L;
                this.categorySmall = "리프레셔";
                this.menuFullName = "딸기 아사이 레모네이드 녹템 리프레셔";
                this.menuShortName = "I-T)딸기아사이REF";
                this.menuTotalPrice = qty * 5900;
                break;
            case 1:
                this.sizeId = 2L;
                this.categorySmall = "리프레셔";
                this.menuFullName = "딸기 아사이 레모네이드 녹템 리프레셔";
                this.menuShortName = "I-G)딸기아사이REF";
                this.menuTotalPrice = qty * 6400;
                break;
            case 2:
                this.sizeId = 10L;
                this.categorySmall = "콜드 브루";
                this.menuFullName = "돌체 콜드 브루";
                this.menuShortName = "I-T)돌체콜드브루";
                this.menuTotalPrice = qty * 6000;
                break;
            case 3:
                this.sizeId = 12L;
                this.categorySmall = "콜드 브루";
                this.menuFullName = "돌체 콜드 브루";
                this.menuShortName = "I-V)돌체콜드브루";
                this.menuTotalPrice = qty * 7000;
                break;
            case 4:
                this.sizeId = 24L;
                this.categorySmall = "블론드";
                this.menuFullName = "아이스 블론드 바닐라 더블 샷 마키아또";
                this.menuShortName = "I-G)BLO바닐라2S";
                this.menuTotalPrice = qty * 6400;
                break;
            case 5:
                this.sizeId = 101L;
                this.categorySmall = "디카페인 커피";
                this.menuFullName = "아이스 디카페인 카라멜 마키아또";
                this.menuShortName = "I-T)DEC카라멜마키아또";
                this.menuTotalPrice = qty * 6200;
                break;
            case 6:
                this.sizeId = 177L;
                this.categorySmall = "티바나";
                this.menuFullName = "아이스 민트 블렌드 티";
                this.menuShortName = "I-T)민트BLE-T";
                this.menuTotalPrice = qty * 4500;
                break;
            default:
                this.sizeId = 32L;
                this.categorySmall = "블론드";
                this.menuFullName = "블론드 녹템 돌체 라떼";
                this.menuShortName = "H-T)BLO돌체L";
                this.menuTotalPrice = qty * 5900;
                break;

        }
    }
}
