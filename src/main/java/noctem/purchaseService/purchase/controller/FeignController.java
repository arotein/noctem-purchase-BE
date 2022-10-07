package noctem.purchaseService.purchase.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import noctem.purchaseService.purchase.service.FeignService;
import noctem.purchaseService.global.common.CommonResponse;
import noctem.purchaseService.global.security.token.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("${global.api.base-path}")
@RequiredArgsConstructor
public class FeignController {
    private final FeignService feignService;

    @GetMapping("/search")
    public CommonResponse getAllQuery(@RequestHeader(JwtAuthenticationToken.JWT_HEADER) String jwt) {
        return CommonResponse.builder()
                .data(feignService.getAllQuery(jwt))
                .build();
    }
}
