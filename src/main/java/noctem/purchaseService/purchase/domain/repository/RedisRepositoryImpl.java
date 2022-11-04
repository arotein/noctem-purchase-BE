package noctem.purchaseService.purchase.domain.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.support.atomic.RedisAtomicInteger;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Repository
@RequiredArgsConstructor
public class RedisRepositoryImpl implements RedisRepository {
    private final RedisTemplate<String, Integer> redisIntegerTemplate;
    private final RedisTemplate<String, String> redisStringTemplate;
    private final String STATISTICS_STORE_KEY_PREFIX = "statistics:store";

    @Override
    public Integer getStorePurchaseNumber(Long storeId) {
        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String key = String.format("purchase:%d:%s", storeId, date);
        RedisAtomicInteger redisAtomicInteger = new RedisAtomicInteger(key, redisIntegerTemplate.getConnectionFactory());
        return redisAtomicInteger.incrementAndGet();
    }

    @Override
    public void rollbackStorePurchaseNumber(Long storeId) {
        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String key = String.format("purchase:%d:%s", storeId, date);
        RedisAtomicInteger redisAtomicInteger = new RedisAtomicInteger(key, redisIntegerTemplate.getConnectionFactory());
        redisAtomicInteger.decrementAndGet();
    }

    @Override
    public String getMonthGraphData(Long storeId) {
        String key = String.format("%s:%d:month", STATISTICS_STORE_KEY_PREFIX, storeId);
        return redisStringTemplate.opsForValue().get(key);
    }

    @Override
    public String getDayGraphData(Long storeId) {
        String key = String.format("%s:%d:day", STATISTICS_STORE_KEY_PREFIX, storeId);
        return redisStringTemplate.opsForValue().get(key);
    }

    @Override
    public String getHourGraphData(Long storeId) {
        String key = String.format("%s:%d:hour", STATISTICS_STORE_KEY_PREFIX, storeId);
        return redisStringTemplate.opsForValue().get(key);
    }

    @Override
    public String getPopularMenuTop3ByStore(Long storeId) {
        String key = String.format("%s:%d:popularMenuTop3", STATISTICS_STORE_KEY_PREFIX, storeId);
        return redisStringTemplate.opsForValue().get(key);
    }

    @Override
    public String getPopularMenuTop5() {
        String key = String.format("%s:popularMenuTop5", STATISTICS_STORE_KEY_PREFIX);
        return redisStringTemplate.opsForValue().get(key);
    }

    @Override
    public String getRegularCustomerTop3ByStore(Long storeId) {
        String key = String.format("%s:%d:regularCustomerTop3", STATISTICS_STORE_KEY_PREFIX, storeId);
        return redisStringTemplate.opsForValue().get(key);
    }
}
