package noctem.purchaseService.dev;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import noctem.purchaseService.global.enumeration.CategorySmall;
import noctem.purchaseService.global.enumeration.Sex;
import noctem.purchaseService.purchase.domain.entity.PaymentInfo;
import noctem.purchaseService.purchase.domain.entity.Purchase;
import noctem.purchaseService.purchase.domain.entity.PurchaseMenu;
import noctem.purchaseService.purchase.domain.repository.PurchaseRepository;
import noctem.purchaseService.purchase.domain.repository.RedisRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class DevService {
    private final PurchaseRepository purchaseRepository;
    private final RedisRepository redisRepository;

    public Boolean addDummy() {
        for (int k = 0; k <= 10000; k++) {
            Integer random = Integer.valueOf(RandomStringUtils.randomNumeric(1));
            Sex sex;
            if (random % 2 == 0) {
                sex = Sex.MALE;
            } else {
                sex = Sex.FEMALE;
            }
            AddDummyVo vo = new AddDummyVo();
            List<PurchaseMenu> menuList = new ArrayList<>();

            PurchaseMenu menu = PurchaseMenu.builder()
                    .sizeId(vo.getSizeId())
                    .categorySmall(CategorySmall.findByValue(vo.getCategorySmall()))
                    .menuFullName(vo.getMenuFullName())
                    .menuShortName(vo.getMenuShortName())
                    .menuTotalPrice(vo.getMenuTotalPrice())
                    .cupType(vo.getCupType())
                    .qty(vo.getQty())
                    .build();

            menuList.add(menu);

            PaymentInfo paymentInfo = PaymentInfo.builder()
                    .cardCorp("์ ํ์นด๋")
                    .cardPaymentPrice(vo.getMenuTotalPrice())
                    .build();

            Purchase purchase = Purchase.builder()
                    .storeId(1L)
                    .storeOrderNumber(redisRepository.getStorePurchaseNumber(1L))
                    .storeName("๋ณธ์ ")
                    .storeAddress("๋ถ์ฐ ํด์ด๋๊ตฌ APEC๋ก 17, 4์ธต")
                    .storeContactNumber("051-0000-0000")
                    .userAccountId(1L)
                    .userNickname("์ฐจ๋๋ง")
                    .age(20 + Integer.valueOf(RandomStringUtils.randomNumeric(2)) % 20)
                    .sex(sex)
                    .purchaseTotalPrice(vo.getMenuTotalPrice())
                    .build()
                    .setRandomCreatedAt()
                    .linkToPaymentInfo(paymentInfo)
                    .linkToPurchaseMenuList(menuList);

            purchaseRepository.save(purchase);
        }
        return true;
    }
}
