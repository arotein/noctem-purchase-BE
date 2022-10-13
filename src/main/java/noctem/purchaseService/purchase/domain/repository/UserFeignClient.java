package noctem.purchaseService.purchase.domain.repository;

import noctem.purchaseService.global.common.CommonResponse;
import noctem.purchaseService.purchase.dto.response.PurchaseUserInfoResFromServDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service")
public interface UserFeignClient {
    @GetMapping(value = "/api/user-service/userAccount/info/{userAccountId}")
    CommonResponse<PurchaseUserInfoResFromServDto> getPurchaseUserInfoToFeignClient(@PathVariable Long userAccountId);
}
