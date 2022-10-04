package noctem.purchaseService.global.common;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CommonResponse<T> {
    private T data;
    private Integer errorCode;
}
