package noctem.purchaseService.purchase.eventHandler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import noctem.purchaseService.purchase.domain.repository.StoreFeignClient;
import noctem.purchaseService.purchase.domain.repository.UserFeignClient;
import noctem.purchaseService.purchase.dto.event.PurchaseEventDto;
import noctem.purchaseService.purchase.dto.response.PurchaseUserInfoResFromServDto;
import noctem.purchaseService.purchase.dto.response.StoreReceiptInfoResFromServDto;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/***
 * 호출 메서드의 트랜잭션 성공여부와 상관없이 결제는 이미 완료된 것이므로 store-service로 데이터를 전달해야됨.
 * 따라서 @TransactionalEventListener말고 @EventListener를 사용. (아마도)
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PurchaseEventHandler {
    private final String PURCHASE_TO_STORE_TOPIC = "purchase-to-store";
    private final StoreFeignClient storeFeignClient;
    private final UserFeignClient userFeignClient;
    private final KafkaTemplate<String, PurchaseEventDto> stringKafkaTemplate;

    @Async
    @EventListener
    public void purchaseProcess(PurchaseEventDto eventDto) {
        // Store 정보 조회
        StoreReceiptInfoResFromServDto storeFeignData = storeFeignClient.getStoreReceiptInfoToFeignClient(eventDto.getStoreId()).getData();
        eventDto.addStoreInfo(storeFeignData.getStoreName(), storeFeignData.getStoreAddress(), storeFeignData.getStoreContactNumber());
        // User 정보 조회
        if (eventDto.getUserAccountId() != null) {
            PurchaseUserInfoResFromServDto userFeignData = userFeignClient.getPurchaseUserInfoToFeignClient(eventDto.getUserAccountId()).getData();
            eventDto.addUserInfo(userFeignData.getUserSex(), userFeignData.getUserAge());
        }
        // Topic 전송
        stringKafkaTemplate.send(PURCHASE_TO_STORE_TOPIC, eventDto);
        log.info("Purchase process event occurred");
    }
}
