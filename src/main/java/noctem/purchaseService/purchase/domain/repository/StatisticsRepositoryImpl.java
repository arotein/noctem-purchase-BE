package noctem.purchaseService.purchase.domain.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import noctem.purchaseService.purchase.domain.entity.PurchaseMenu;
import noctem.purchaseService.purchase.domain.entity.QPurchase;
import noctem.purchaseService.purchase.dto.response.PopularMenuResDto;
import noctem.purchaseService.purchase.dto.response.RegularCustomerResDto;
import noctem.purchaseService.purchase.dto.vo.PopularMenuVo;
import noctem.purchaseService.purchase.dto.vo.PurchaseStatisticsDayBaseVo;
import noctem.purchaseService.purchase.dto.vo.PurchaseStatisticsHourBaseVo;
import noctem.purchaseService.purchase.dto.vo.PurchaseStatisticsMonthBaseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
    private QPurchase subPurchase = new QPurchase("subPurchase");

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
    public PurchaseStatisticsMonthBaseVo findPurchaseStatisticsForMonth(Long storeId, LocalDateTime baseDttm) {
        return queryFactory.select(Projections.constructor(
                                PurchaseStatisticsMonthBaseVo.class,
                                priceSumForMonth(storeId, baseDttm.minusMonths(11)),
                                priceSumForMonth(storeId, baseDttm.minusMonths(10)),
                                priceSumForMonth(storeId, baseDttm.minusMonths(9)),
                                priceSumForMonth(storeId, baseDttm.minusMonths(8)),
                                priceSumForMonth(storeId, baseDttm.minusMonths(7)),
                                priceSumForMonth(storeId, baseDttm.minusMonths(6)),
                                priceSumForMonth(storeId, baseDttm.minusMonths(5)),
                                priceSumForMonth(storeId, baseDttm.minusMonths(4)),
                                priceSumForMonth(storeId, baseDttm.minusMonths(3)),
                                priceSumForMonth(storeId, baseDttm.minusMonths(2)),
                                priceSumForMonth(storeId, baseDttm.minusMonths(1)),
                                priceSumForMonth(storeId, baseDttm),
                                JPAExpressions.select(subPurchase.countDistinct().longValue())
                                        .from(subPurchase)
                                        .where(subPurchase.createdAt.between(
                                                        LocalDateTime.of(baseDttm.getYear(), baseDttm.getMonth(), 1, 0, 0).minusMonths(11),
                                                        LocalDateTime.of(baseDttm.getYear(), baseDttm.getMonth(), 1, 0, 0).plusMonths(1).minusNanos(1)),
                                                subPurchase.storeId.eq(storeId))
                                        .groupBy(subPurchase.storeId)
                        )
                )
                .from(purchase)
                .fetchFirst();
    }

    @Override
    public PurchaseStatisticsDayBaseVo findPurchaseStatisticsForDay(Long storeId, LocalDateTime baseDttm) {
        return queryFactory.select(Projections.constructor(
                                PurchaseStatisticsDayBaseVo.class,
                                priceSumForDay(storeId, baseDttm.minusDays(6)),
                                priceSumForDay(storeId, baseDttm.minusDays(5)),
                                priceSumForDay(storeId, baseDttm.minusDays(4)),
                                priceSumForDay(storeId, baseDttm.minusDays(3)),
                                priceSumForDay(storeId, baseDttm.minusDays(2)),
                                priceSumForDay(storeId, baseDttm.minusDays(1)),
                                priceSumForDay(storeId, baseDttm),
                                JPAExpressions.select(subPurchase.countDistinct().longValue())
                                        .from(subPurchase)
                                        .where(subPurchase.createdAt.between(
                                                        LocalDateTime.of(baseDttm.toLocalDate(), LocalTime.of(0, 0)).minusDays(6),
                                                        LocalDateTime.of(baseDttm.toLocalDate(), LocalTime.of(0, 0)).plusDays(1).minusNanos(1)),
                                                subPurchase.storeId.eq(storeId))
                                        .groupBy(subPurchase.storeId)
                        )
                )
                .from(purchase)
                .fetchFirst();
    }

    @Override
    public PurchaseStatisticsHourBaseVo findPurchaseStatisticsForHour(Long storeId, LocalDateTime baseDttm) {
        return queryFactory.select(Projections.constructor(
                                PurchaseStatisticsHourBaseVo.class,
                                priceSumForHour(storeId, baseDttm.minusHours(11)),
                                priceSumForHour(storeId, baseDttm.minusHours(10)),
                                priceSumForHour(storeId, baseDttm.minusHours(9)),
                                priceSumForHour(storeId, baseDttm.minusHours(8)),
                                priceSumForHour(storeId, baseDttm.minusHours(7)),
                                priceSumForHour(storeId, baseDttm.minusHours(6)),
                                priceSumForHour(storeId, baseDttm.minusHours(5)),
                                priceSumForHour(storeId, baseDttm.minusHours(4)),
                                priceSumForHour(storeId, baseDttm.minusHours(3)),
                                priceSumForHour(storeId, baseDttm.minusHours(2)),
                                priceSumForHour(storeId, baseDttm.minusHours(1)),
                                priceSumForHour(storeId, baseDttm),
                                JPAExpressions.select(subPurchase.countDistinct().longValue())
                                        .from(subPurchase)
                                        .where(subPurchase.createdAt.between(
                                                        LocalDateTime.of(baseDttm.getYear(), baseDttm.getMonth(), baseDttm.getDayOfMonth(), baseDttm.getHour(), 0).minusHours(11),
                                                        LocalDateTime.of(baseDttm.getYear(), baseDttm.getMonth(), baseDttm.getDayOfMonth(), baseDttm.getHour(), 0).plusHours(1).minusNanos(1)),
                                                subPurchase.storeId.eq(storeId))
                                        .groupBy(subPurchase.storeId)
                        )
                )
                .from(purchase)
                .fetchFirst();
    }

    private JPQLQuery<Long> priceSumForMonth(Long storeId, LocalDateTime baseDttm) {
        LocalDateTime startDttm = LocalDateTime.of(baseDttm.getYear(), baseDttm.getMonth(), 1, 0, 0);
        LocalDateTime endDttm = startDttm.plusMonths(1).minusNanos(1);
        return JPAExpressions.select(subPurchase.purchaseTotalPrice.sum().longValue())
                .from(subPurchase)
                .where(subPurchase.createdAt.between(startDttm, endDttm),
                        subPurchase.storeId.eq(storeId))
                .groupBy(subPurchase.storeId);
    }


    private JPQLQuery<Long> priceSumForDay(Long storeId, LocalDateTime baseDttm) {
        LocalDateTime startDttm = LocalDateTime.of(baseDttm.toLocalDate(), LocalTime.of(0, 0));
        LocalDateTime endDttm = startDttm.plusDays(1).minusNanos(1);
        return JPAExpressions.select(subPurchase.purchaseTotalPrice.sum().longValue())
                .from(subPurchase)
                .where(subPurchase.createdAt.between(startDttm, endDttm),
                        subPurchase.storeId.eq(storeId))
                .groupBy(subPurchase.storeId);
    }

    private JPQLQuery<Long> priceSumForHour(Long storeId, LocalDateTime baseDttm) {
        LocalDateTime startDttm = LocalDateTime.of(baseDttm.getYear(), baseDttm.getMonth(), baseDttm.getDayOfMonth(), baseDttm.getHour(), 0);
        LocalDateTime endDttm = startDttm.plusHours(1).minusNanos(1);
        return JPAExpressions.select(subPurchase.purchaseTotalPrice.sum().longValue())
                .from(subPurchase)
                .where(subPurchase.createdAt.between(startDttm, endDttm),
                        subPurchase.storeId.eq(storeId))
                .groupBy(subPurchase.storeId);
    }
}
