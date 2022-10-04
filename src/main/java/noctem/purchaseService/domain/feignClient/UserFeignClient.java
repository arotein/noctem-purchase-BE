package noctem.purchaseService.domain.feignClient;

import noctem.purchaseService.global.common.CommonRequest;
import noctem.purchaseService.global.security.token.JwtAuthenticationToken;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.Set;

@FeignClient(name = "user-service")
public interface UserFeignClient {
    @GetMapping(value = "/api/user-service/search")
    CommonRequest<Set<String>> getAllQuery(@RequestHeader(JwtAuthenticationToken.JWT_HEADER) String jwt);
}
