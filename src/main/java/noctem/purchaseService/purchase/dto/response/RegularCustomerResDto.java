package noctem.purchaseService.purchase.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import noctem.purchaseService.global.enumeration.Sex;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegularCustomerResDto {
    private Integer index;
    private Integer rank;
    private Integer age;
    private String sex;
    private Long totalVisitCount;

    public RegularCustomerResDto(Integer age, Sex sex, Long totalVisitCount) {
        this.age = age;
        this.sex = sex.getValue();
        this.totalVisitCount = totalVisitCount;
    }
}
