package noctem.purchaseService.purchase.domain.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import noctem.purchaseService.purchase.domain.entity.PurchaseMenu;
import noctem.purchaseService.purchase.dto.response.PopularMenuResDto;
import noctem.purchaseService.purchase.dto.response.RegularCustomerResDto;
import noctem.purchaseService.purchase.dto.vo.PopularMenuVo;
import noctem.purchaseService.purchase.dto.vo.SalesDataVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.querydsl.core.group.GroupBy.groupBy;
import static noctem.purchaseService.purchase.domain.entity.QPurchase.purchase;
import static noctem.purchaseService.purchase.domain.entity.QPurchaseMenu.purchaseMenu;


@Repository
@RequiredArgsConstructor
public class StatisticsRepositoryImpl implements StatisticsRepository {
    private final EntityManager entityManager;
    private final JPAQueryFactory queryFactory;

    @Autowired
    public StatisticsRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    public List<PopularMenuResDto> findPopularMenuTop3ByStore(Long storeId) {
        List<PopularMenuVo> voList = queryFactory.select(Projections.constructor(PopularMenuVo.class,
                        purchaseMenu.menuFullName, purchaseMenu.qty.sum()))
                .from(purchaseMenu)
                .join(purchaseMenu.purchase, purchase).on(purchase.storeId.eq(storeId))
                .groupBy(purchaseMenu.menuFullName)
                .orderBy(purchaseMenu.qty.sum().desc())
                .limit(3)
                .fetch();

        Map<String, PurchaseMenu> menuMap = queryFactory.from(purchaseMenu)
                .join(purchaseMenu.purchase, purchase).on(purchase.storeId.eq(storeId))
                .where(purchaseMenu.menuFullName.in(voList.stream().map(PopularMenuVo::getMenuFullName).collect(Collectors.toList())))
                .distinct()
                .transform(groupBy(purchaseMenu.menuFullName).as(purchaseMenu));

        return voList.stream().map(e -> new PopularMenuResDto(menuMap.get(e.getMenuFullName()).getSizeId(),
                e.getTotalCount())).collect(Collectors.toList());
    }

    public List<PopularMenuResDto> findPopularMenuTop5() {
        List<PopularMenuVo> voList = queryFactory.select(Projections.constructor(PopularMenuVo.class,
                        purchaseMenu.menuFullName, purchaseMenu.qty.sum()))
                .from(purchaseMenu)
                .groupBy(purchaseMenu.menuFullName)
                .orderBy(purchaseMenu.qty.sum().desc())
                .limit(5)
                .fetch();

        Map<String, PurchaseMenu> menuMap = queryFactory.from(purchaseMenu)
                .where(purchaseMenu.menuFullName.in(voList.stream().map(PopularMenuVo::getMenuFullName).collect(Collectors.toList())))
                .distinct()
                .transform(groupBy(purchaseMenu.menuFullName).as(purchaseMenu));

        return voList.stream().map(e -> new PopularMenuResDto(menuMap.get(e.getMenuFullName()).getSizeId(),
                e.getTotalCount())).collect(Collectors.toList());
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

    @Override
    public List<SalesDataVo> findMonthGraph(Long storeId, LocalDateTime startTime, LocalDateTime endTime) {
        return null;
    }

    @Override
    public List<SalesDataVo> findWeekGraph(Long storeId, LocalDateTime startTime, LocalDateTime endTime) {
        return null;
    }

    @Override
    public List<SalesDataVo> findDayGraph(Long storeId, LocalDateTime startTime, LocalDateTime endTime) {
        return null;
    }

    @Override
    public List<SalesDataVo> findHourGraph(Long storeId, LocalDateTime startTime, LocalDateTime endTime) {
        return queryFactory.select(Projections.constructor(SalesDataVo.class,
                        purchase.createdAt, purchase.purchaseTotalPrice))
                .from(purchase)
                .where(purchase.createdAt.between(startTime, endTime))
                .orderBy(purchase.createdAt.asc())
                .fetch();
    }
}
