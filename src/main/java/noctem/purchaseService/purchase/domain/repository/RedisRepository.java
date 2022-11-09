package noctem.purchaseService.purchase.domain.repository;

public interface RedisRepository {
    Integer getStorePurchaseNumber(Long storeId);

    void rollbackStorePurchaseNumber(Long storeId);

    String getMonthGraphData(Long storeId);

    String getDayGraphData(Long storeId);

    String getHourGraphData(Long storeId);

    String getPopularMenuTop3ByStore(Long storeId);

    String getPopularMenuTop5();

    String getRegularCustomerTop3ByStore(Long storeId);

    void setOrderInProgress(Long userAccountId, Long purchaseId);
}
