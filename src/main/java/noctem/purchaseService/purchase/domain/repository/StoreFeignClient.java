package noctem.purchaseService.purchase.domain.repository;

import noctem.purchaseService.global.common.CommonResponse;
import noctem.purchaseService.purchase.dto.response.StoreReceiptInfoResFromServDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "store-service")
public interface StoreFeignClient {
    @GetMapping(value = "/api/store-service/store/{storeId}/receipt")
    CommonResponse<StoreReceiptInfoResFromServDto> getStoreReceiptInfoToFeignClient(@PathVariable Long storeId);
}
