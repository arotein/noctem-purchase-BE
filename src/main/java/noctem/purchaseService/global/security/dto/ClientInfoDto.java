package noctem.purchaseService.global.security.dto;

import lombok.*;
import noctem.purchaseService.global.enumeration.Role;

@Data
@Builder
public class ClientInfoDto {
    private Long userAccountId;
    private String nickname;
    private String email;
    private Long storeAccountId;
    private Long storeId;
    private Role role;
}
