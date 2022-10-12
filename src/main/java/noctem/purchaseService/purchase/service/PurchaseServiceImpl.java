package noctem.purchaseService.purchase.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import noctem.purchaseService.global.enumeration.Sex;
import noctem.purchaseService.global.security.bean.ClientInfoLoader;
import noctem.purchaseService.purchase.domain.entity.PaymentInfo;
import noctem.purchaseService.purchase.domain.entity.Purchase;
import noctem.purchaseService.purchase.domain.entity.PurchaseMenu;
import noctem.purchaseService.purchase.domain.repository.MenuFeignClient;
import noctem.purchaseService.purchase.domain.repository.PurchaseRepository;
import noctem.purchaseService.purchase.domain.repository.StoreFeignClient;
import noctem.purchaseService.purchase.dto.InnerDto;
import noctem.purchaseService.purchase.dto.request.AnonymousPurchaseReqDto;
import noctem.purchaseService.purchase.dto.request.GetAllUserPurchaseQueryReqDto;
import noctem.purchaseService.purchase.dto.request.UserPurchaseReqDto;
import noctem.purchaseService.purchase.dto.response.MenuReceiptInfoResFromServDto;
import noctem.purchaseService.purchase.dto.response.PurchaseListResDto;
import noctem.purchaseService.purchase.dto.response.ReceiptDetailResDto;
import noctem.purchaseService.purchase.dto.response.StoreReceiptInfoResFromServDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class PurchaseServiceImpl implements PurchaseService {
    private final PurchaseRepository purchaseRepository;
    private final ClientInfoLoader clientInfoLoader;
    private final StoreFeignClient storeFeignClient;
    private final MenuFeignClient menuFeignClient;

    // 결제 완료 후 받는 API
    @Override
    public Boolean addPurchaseByUser(UserPurchaseReqDto dto) {
        StoreReceiptInfoResFromServDto feignData = storeFeignClient.getStoreReceiptInfoToFeignClient(dto.getStoreId()).getData();
        List<MenuReceiptInfoResFromServDto> feignDataList = dto.getMenuList().stream()
                .map(e -> menuFeignClient.getMenuReceiptInfoToFeignClient(e.getSizeId()).getData())
                .collect(Collectors.toList());

        Map<Long, Integer> menuDtoMap = dto.getMenuList().stream()
                .collect(Collectors.toMap(InnerDto.MenuReqDto::getSizeId, InnerDto.MenuReqDto::getQty));

        PaymentInfo paymentInfo = PaymentInfo.builder()
                .cardCorp(dto.getCardCorp())
                .cardPaymentPrice(dto.getCardPaymentPrice())
                .build();

        List<PurchaseMenu> puchaseMenuList = feignDataList.stream()
                .map(e -> PurchaseMenu.builder()
                                .menuFullName(e.getMenuKorName())
                                .menuShortName(e.getMenuShortenName())
                                .qty(menuDtoMap.get(e.getSizeId()))
                                .menuTotalPrice(e.getTotalPrice())
                                .build()
                        //                        .linkToPersonalOptionList() // 미구현
                ).collect(Collectors.toList());

        Purchase purchase = Purchase.builder()
                .storeId(dto.getStoreId())
                .storeName(feignData.getStoreName())
                .storeOrderNumber(purchaseRepository.getStorePurchaseNumber(dto.getStoreId()))
                .storeAddress(feignData.getStoreAddress())
                .storeContactNumber(feignData.getStoreContactNumber())
                .userAccountId(clientInfoLoader.getUserAccountId())
                .userNickname(clientInfoLoader.getUserNickname())
                .purchaseTotalPrice(dto.getPurchaseTotalPrice())
                .build()
                .linkToPaymentInfo(paymentInfo)
                .linkToPurchaseMenuList(puchaseMenuList);

        purchaseRepository.save(purchase);

        // store에 주문 알림 전송
        // user에 주문 알림 전송
        return true;
    }

    // 결제 완료 후 받는 API
    @Override
    public Boolean addPurchaseByAnonymous(AnonymousPurchaseReqDto dto) {
        StoreReceiptInfoResFromServDto feignData = storeFeignClient.getStoreReceiptInfoToFeignClient(dto.getStoreId()).getData();
        List<MenuReceiptInfoResFromServDto> feignDataList = dto.getMenuList().stream()
                .map(e -> menuFeignClient.getMenuReceiptInfoToFeignClient(e.getSizeId()).getData())
                .collect(Collectors.toList());

        Map<Long, Integer> menuDtoMap = dto.getMenuList().stream()
                .collect(Collectors.toMap(InnerDto.MenuReqDto::getSizeId, InnerDto.MenuReqDto::getQty));

        PaymentInfo paymentInfo = PaymentInfo.builder()
                .cardCorp(dto.getCardCorp())
                .cardPaymentPrice(dto.getCardPaymentPrice())
                .build();

        List<PurchaseMenu> puchaseMenuList = feignDataList.stream()
                .map(e -> PurchaseMenu.builder()
                                .menuFullName(e.getMenuKorName())
                                .menuShortName(e.getMenuShortenName())
                                .qty(menuDtoMap.get(e.getSizeId()))
                                .menuTotalPrice(e.getTotalPrice())
                                .build()
//                        .linkToPersonalOptionList() // 미구현
                ).collect(Collectors.toList());

        Purchase purchase = Purchase.builder()
                .storeId(dto.getStoreId())
                .storeName(feignData.getStoreName())
                .storeOrderNumber(purchaseRepository.getStorePurchaseNumber(dto.getStoreId()))
                .storeAddress(feignData.getStoreAddress())
                .storeContactNumber(feignData.getStoreContactNumber())
                .anonymousName(dto.getAnonymousName())
                .anonymousPhoneNumber(dto.getAnonymousPhoneNumber())
                .anonymousSex(Sex.findByValue(dto.getAnonymousSex()))
                .anonymousAge(dto.getAnonymousAge())
                .purchaseTotalPrice(dto.getPurchaseTotalPrice())
                .build()
                .linkToPaymentInfo(paymentInfo)
                .linkToPurchaseMenuList(puchaseMenuList);

        purchaseRepository.save(purchase);

        // store에 주문 알림 전송
        return true;
    }

    @Transactional(readOnly = true)
    @Override
    public PurchaseListResDto getAllUserPurchase(GetAllUserPurchaseQueryReqDto dto) {
        LocalDateTime startDate = null;
        LocalDateTime endDate = null;
        // 검색 기간 설정
        if (dto.getStartDate() == null || dto.getEndDate() == null) {
            startDate = LocalDate.now().minusMonths(1L).atStartOfDay();
            endDate = LocalDateTime.now();
        } else {
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            startDate = LocalDate.parse(dto.getStartDate(), dateTimeFormatter).atStartOfDay();
            endDate = LocalDate.parse(dto.getEndDate(), dateTimeFormatter).plusDays(1L).atStartOfDay();
        }
        // Entity 조회
        List<Purchase> purchaseList = purchaseRepository.findAllByUserAccountIdAndPaymentInfoApprovedAtBetween(
                clientInfoLoader.getUserAccountId(),
                startDate,
                endDate);
        // DTO 생성
        List<InnerDto.UserPurchaseResDto> innerDtoList = purchaseList.stream()
                .map(InnerDto.UserPurchaseResDto::new)
                .collect(Collectors.toList());

        return new PurchaseListResDto(innerDtoList);
    }

    @Transactional(readOnly = true)
    @Override
    public PurchaseListResDto getAllAnonymousPurchase(String name, String phoneNumber) {
        // 검색 기간 설정
        LocalDateTime startDate = LocalDate.now().minusDays(1L).atStartOfDay();
        LocalDateTime endDate = LocalDate.now().plusDays(1L).atStartOfDay();
        // Entity 조회
        List<Purchase> purchaseList = purchaseRepository.findAllAnonymousPurchase(name, phoneNumber, startDate, endDate);
        // DTO 생성
        List<InnerDto.UserPurchaseResDto> innerDtoList = purchaseList.stream()
                .map(InnerDto.UserPurchaseResDto::new)
                .collect(Collectors.toList());

        return new PurchaseListResDto(innerDtoList);
    }

    @Transactional(readOnly = true)
    @Override
    public ReceiptDetailResDto getPurchaseDetail(String purchaseSerialNumber) {
        return new ReceiptDetailResDto(purchaseRepository.findByPurchaseSerialNumber(purchaseSerialNumber));
    }
}
