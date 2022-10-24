package noctem.purchaseService.dev;

import lombok.Data;
import org.apache.commons.lang3.RandomStringUtils;

@Data
public class AddDummyVo {
    private Long sizeId;
    private String menuFullName;
    private String menuShortName;
    private Integer menuTotalPrice;
    private Integer qty;

    public AddDummyVo() {
        this.qty = 1 + Integer.valueOf(RandomStringUtils.randomNumeric(1)) % 4;
        Integer randInt = Integer.valueOf(RandomStringUtils.randomNumeric(2));
        switch (randInt % 6) {
            case 0:
                this.sizeId = 1L;
                this.menuFullName = "딸기 아사이 레모네이드 녹템 리프레셔";
                this.menuShortName = "I-T)딸기아사이REF";
                this.menuTotalPrice = qty * 5900;
                break;
            case 1:
                this.sizeId = 2L;
                this.menuFullName = "딸기 아사이 레모네이드 녹템 리프레셔";
                this.menuShortName = "I-G)딸기아사이REF";
                this.menuTotalPrice = qty * 6400;
                break;
            case 2:
                this.sizeId = 10L;
                this.menuFullName = "돌체 콜드 브루";
                this.menuShortName = "I-T)돌체콜드브루";
                this.menuTotalPrice = qty * 6000;
                break;
            case 3:
                this.sizeId = 12L;
                this.menuFullName = "돌체 콜드 브루";
                this.menuShortName = "I-V)돌체콜드브루";
                this.menuTotalPrice = qty * 7000;
                break;
            case 4:
                this.sizeId = 24L;
                this.menuFullName = "아이스 블론드 바닐라 더블 샷 마키아또";
                this.menuShortName = "I-G)BLO바닐라2S";
                this.menuTotalPrice = qty * 6400;
                break;
            default:
                this.sizeId = 32L;
                this.menuFullName = "블론드 녹템 돌체 라떼";
                this.menuShortName = "H-T)BLO돌체L";
                this.menuTotalPrice = qty * 5900;
                break;

        }
        this.sizeId = sizeId;
        this.menuFullName = menuFullName;
        this.menuShortName = menuShortName;
        this.menuTotalPrice = menuTotalPrice;
    }
}
