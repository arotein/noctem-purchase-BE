package noctem.purchaseService.purchase.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import noctem.purchaseService.global.common.CommonResponse;
import noctem.purchaseService.purchase.dto.request.AnonymousPurchaseReqDto;
import noctem.purchaseService.purchase.dto.request.GetAllUserPurchaseQueryReqDto;
import noctem.purchaseService.purchase.dto.request.UserPurchaseReqDto;
import noctem.purchaseService.purchase.service.PurchaseService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("${global.api.base-path}/purchase")
@RequiredArgsConstructor
public class PurchaseController {
    private final PurchaseService purchaseService;

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/user")
    public CommonResponse addPurchaseByUser(@Validated @RequestBody UserPurchaseReqDto dto) {
        return CommonResponse.builder()
                .data(purchaseService.addPurchaseByUser(dto))
                .build();
    }

    @PostMapping("/anonymous")
    public CommonResponse addPurchaseByAnonymous(@Validated @RequestBody AnonymousPurchaseReqDto dto) {
        return CommonResponse.builder()
                .data(purchaseService.addPurchaseByAnonymous(dto))
                .build();
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/user")
    public CommonResponse getAllUserPurchase(GetAllUserPurchaseQueryReqDto dto) {
        // date 형식: yyyy-HH-mm
        return CommonResponse.builder()
                .data(purchaseService.getAllUserPurchase(dto))
                .build();
    }

    // 당일~전날만 조회가능
    @GetMapping("/anonymous/{name}/{phoneNumber}")
    public CommonResponse getAllAnonymousPurchase(@PathVariable String name, @PathVariable String phoneNumber) {
        return CommonResponse.builder()
                .data(purchaseService.getAllAnonymousPurchase(name, phoneNumber))
                .build();
    }

    @GetMapping("/detail/{purchaseSerialNumber}")
    public CommonResponse getPurchaseDetail(@PathVariable String purchaseSerialNumber) {
        return CommonResponse.builder()
                .data(purchaseService.getPurchaseDetail(purchaseSerialNumber))
                .build();
    }
}
