package noctem.purchaseService.domain.service;

import noctem.purchaseService.global.security.token.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.Set;

public interface FeignService {
    Set<String> getAllQuery(@RequestHeader(JwtAuthenticationToken.JWT_HEADER) String jwt);
}
