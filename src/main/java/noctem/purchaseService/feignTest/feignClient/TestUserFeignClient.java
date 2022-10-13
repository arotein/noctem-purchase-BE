package noctem.purchaseService.feignTest.feignClient;

import noctem.purchaseService.global.common.CommonResponse;
import noctem.purchaseService.global.security.token.JwtAuthenticationToken;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.Set;

@FeignClient(name = "user-service")
public interface TestUserFeignClient {
    @GetMapping(value = "/api/user-service/search")
    CommonResponse<Set<String>> getAllQuery(@RequestHeader(JwtAuthenticationToken.JWT_HEADER) String jwt);
}
