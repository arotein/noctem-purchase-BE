package noctem.purchaseService.dev;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("${global.api.base-path}/dev")
@RequiredArgsConstructor
public class DevController {
    private final DevService devService;

    // == dev용 코드 ==
//    @PostMapping("/dummy") // BaseEntity에 @CreatedDate 주석처리해야됨
//    public CommonResponse addDummy() {
//        return CommonResponse.builder()
//                .data(devService.addDummy())
//                .build();
//    }
}
