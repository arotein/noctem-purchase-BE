package noctem.purchaseService.purchase.domain.repository;

public interface RedisRepository {
    Integer getStorePurchaseNumber(Long storeId);
}
