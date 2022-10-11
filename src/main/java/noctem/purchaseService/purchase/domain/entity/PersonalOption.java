package noctem.purchaseService.purchase.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import noctem.purchaseService.global.common.BaseEntity;
import noctem.purchaseService.global.enumeration.Amount;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PersonalOption extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "personal_option_id")
    private Long id;
    private String personalOptionName;
    @Enumerated(EnumType.STRING)
    private Amount amount;
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "purchase_menu_id")
    private PurchaseMenu purchaseMenu;

    @Builder
    public PersonalOption(String personalOptionName, Amount amount) {
        this.personalOptionName = personalOptionName;
        this.amount = amount;
    }

    public PersonalOption linkToPurchaseMenu(PurchaseMenu purchaseMenu) {
        this.purchaseMenu = purchaseMenu;
        return this;
    }
}
