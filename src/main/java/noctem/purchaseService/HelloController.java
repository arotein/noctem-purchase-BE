package noctem.purchaseService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RefreshScope
@RequestMapping("/api/purchase-service")
public class HelloController {
    @Value("${global.value}")
    private String value;

    @GetMapping("/yml")
    public String yml() {
        return value;
    }
}
