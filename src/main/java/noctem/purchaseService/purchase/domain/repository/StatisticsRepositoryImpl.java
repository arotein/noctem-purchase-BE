package noctem.purchaseService.purchase.domain.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import noctem.purchaseService.purchase.domain.entity.QPurchaseMenu;
import noctem.purchaseService.purchase.dto.response.PopularMenuResDto;
import noctem.purchaseService.purchase.dto.response.RegularCustomerResDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

import static noctem.purchaseService.purchase.domain.entity.QPurchase.purchase;


@Repository
@RequiredArgsConstructor
public class StatisticsRepositoryImpl implements StatisticsRepository {
    private final EntityManager entityManager;
    private final JPAQueryFactory queryFactory;
    private final QPurchaseMenu purchaseMenu = new QPurchaseMenu("purchase_menu");

    @Autowired
    public StatisticsRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    public List<PopularMenuResDto> findPopularMenuTop3ByStore(Long storeId) {
        return queryFactory.select(Projections.constructor(PopularMenuResDto.class,
                        purchaseMenu.sizeId, purchaseMenu.qty.sum()))
                .from(purchaseMenu)
                .join(purchaseMenu.purchase, purchase)
                .where(purchase.storeId.eq(storeId))
                .groupBy(purchaseMenu.sizeId)
                .orderBy(purchaseMenu.qty.sum().desc())
                .limit(3)
                .fetch();
    }

    public List<PopularMenuResDto> findPopularMenuTop5() {
        return queryFactory.select(Projections.constructor(PopularMenuResDto.class,
                        purchaseMenu.sizeId, purchaseMenu.qty.sum()))
                .from(purchaseMenu)
                .groupBy(purchaseMenu.sizeId)
                .orderBy(purchaseMenu.qty.sum().desc())
                .limit(5)
                .fetch();
    }

    public List<RegularCustomerResDto> findRegularCustomerTop3ByStore(Long storeId) {
        return queryFactory.select(Projections.constructor(RegularCustomerResDto.class,
                        purchase.age.divide(10).floor().multiply(10), purchase.sex, purchase.countDistinct()))
                .from(purchase)
                .join(purchase.purchaseMenuList, purchaseMenu)
                .where(purchase.storeId.eq(storeId))
                .groupBy(purchase.sex, purchase.age.divide(10).floor().multiply(10))
                .orderBy(purchase.countDistinct().desc())
                .limit(3)
                .fetch();
    }
}
