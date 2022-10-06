package noctem.purchaseService.domain.service;

import lombok.RequiredArgsConstructor;
import noctem.purchaseService.domain.feignClient.UserFeignClient;
import noctem.purchaseService.global.common.CommonException;
import noctem.purchaseService.global.common.CommonResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@Transactional
@RequiredArgsConstructor
public class FeignServiceImpl implements FeignService {
    private final UserFeignClient userFeignClient;

    @Override
    public Set<String> getAllQuery(String jwt) {
        CommonResponse<Set<String>> response = userFeignClient.getAllQuery(jwt);
        if (response.getErrorCode() != null) {
            throw CommonException.builder().errorCode(response.getErrorCode()).httpStatus(response.getHttpStatus()).build();
        }
        return userFeignClient.getAllQuery(jwt).getData();
    }
}
