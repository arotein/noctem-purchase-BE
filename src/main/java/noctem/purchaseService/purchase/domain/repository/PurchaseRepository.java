package noctem.purchaseService.purchase.domain.repository;

import noctem.purchaseService.purchase.domain.entity.Purchase;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PurchaseRepository extends JpaRepository<Purchase, Long>, RedisRepository {

    @Override
    @EntityGraph(attributePaths = {"paymentInfo"})
    Optional<Purchase> findById(Long id);

    @EntityGraph(attributePaths = {"paymentInfo"})
    Purchase findByPurchaseSerialNumber(String purchaseSerialNumber);

    @EntityGraph(attributePaths = {"paymentInfo"})
    List<Purchase> findAllByUserAccountIdAndPaymentInfoApprovedAtBetween(Long userAccountId, LocalDateTime start, LocalDateTime end);

    @Query("select p from Purchase p join fetch p.paymentInfo i " +
            "where p.anonymousName=:anonymousName " +
            "and p.anonymousPhoneNumber=:anonymousPhoneNumber " +
            "and i.approvedAt between :startDate and :endDate")
    List<Purchase> findAllAnonymousPurchase(String anonymousName, String anonymousPhoneNumber, LocalDateTime startDate, LocalDateTime endDate);
}
