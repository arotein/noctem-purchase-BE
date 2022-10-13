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
    private final RedisTemplate<String, Integer> redisTemplate;

    public Integer getStorePurchaseNumber(Long storeId) {
        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String key = String.format("purchase:%d:%s", storeId, date);
        RedisAtomicInteger redisAtomicInteger = new RedisAtomicInteger(key, redisTemplate.getConnectionFactory());
        return redisAtomicInteger.incrementAndGet();
    }
}
