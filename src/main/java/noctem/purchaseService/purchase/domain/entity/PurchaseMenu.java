package noctem.purchaseService.purchase.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import noctem.purchaseService.global.common.BaseEntity;
import noctem.purchaseService.global.enumeration.CategorySmall;
import noctem.purchaseService.global.enumeration.CupType;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/***
 * menuTotalPrice: 옵션을 포함한 메뉴가격
 */
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PurchaseMenu extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "purchase_menu_id")
    private Long id;
    private Long sizeId;
    @Enumerated(EnumType.STRING)
    private CategorySmall categorySmall;
    private String menuFullName;
    private String menuShortName;
    private String temperature;
    private String size;
    @Enumerated(EnumType.STRING)
    private CupType cupType;
    private Integer qty;
    private Integer menuTotalPrice;
    @JsonIgnore
    @OneToMany(mappedBy = "purchaseMenu", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PurchasePersonalOption> purchasePersonalOptionList = new ArrayList<>();

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "purchase_id")
    private Purchase purchase;

    @Builder
    public PurchaseMenu(Long sizeId, CategorySmall categorySmall, String menuFullName, String menuShortName, CupType cupType, Integer qty, Integer menuTotalPrice) {
        this.sizeId = sizeId;
        this.categorySmall = categorySmall;
        this.menuFullName = menuFullName;
        this.menuShortName = menuShortName;
        this.cupType = cupType;
        this.qty = qty;
        this.menuTotalPrice = menuTotalPrice;
        this.temperature = extractTemperature(menuShortName);
        this.size = extractSize(menuShortName);
    }

    public PurchaseMenu linkToPurchase(Purchase purchase) {
        this.purchase = purchase;
        return this;
    }

    public PurchaseMenu linkToPersonalOptionList(List<PurchasePersonalOption> purchasePersonalOptionList) {
        this.purchasePersonalOptionList.addAll(purchasePersonalOptionList);
        purchasePersonalOptionList.forEach(e -> e.linkToPurchaseMenu(this));
        return this;
    }

    private String extractTemperature(String menuShortName) {
        char temperatureChar = menuShortName.charAt(0);
        switch (temperatureChar) {
            case 'I':
                return "ICED";
            default:
                return "HOT";
        }
    }

    private String extractSize(String menuShortName) {
        char sizeChar = menuShortName.charAt(2);
        switch (sizeChar) {
            case 'T':
                return "Tall";
            case 'G':
                return "Grande";
            default:
                return "Venti";
        }
    }
}
