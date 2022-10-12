package noctem.purchaseService.purchase.domain.repository;

import noctem.purchaseService.global.common.CommonResponse;
import noctem.purchaseService.purchase.dto.response.MenuReceiptInfoResFromServDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "menu-service")
public interface MenuFeignClient {
    @GetMapping(value = "/api/menu-service/size/menu/{sizeId}")
    CommonResponse<MenuReceiptInfoResFromServDto> getMenuReceiptInfoToFeignClient(@PathVariable Long sizeId);
}
