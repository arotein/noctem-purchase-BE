package noctem.purchaseService.purchase.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import noctem.purchaseService.global.security.bean.ClientInfoLoader;
import noctem.purchaseService.purchase.domain.entity.Purchase;
import noctem.purchaseService.purchase.domain.repository.PurchaseRepository;
import noctem.purchaseService.purchase.dto.InnerDto;
import noctem.purchaseService.purchase.dto.request.AnonymousPurchaseReqDto;
import noctem.purchaseService.purchase.dto.request.GetAllUserPurchaseQueryReqDto;
import noctem.purchaseService.purchase.dto.request.UserPurchaseReqDto;
import noctem.purchaseService.purchase.dto.response.PurchaseListResDto;
import noctem.purchaseService.purchase.dto.response.ReceiptDetailResDto;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class PurchaseServiceImpl implements PurchaseService {
    private final String PURCHASE_TO_STORE_TOPIC = "purchase-to-store";
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final PurchaseRepository purchaseRepository;
    private final ClientInfoLoader clientInfoLoader;

    // 결제 완료 후 받는 API
    @Override
    public Boolean addPurchaseByUser(UserPurchaseReqDto dto) {

//        Map<Long, Integer> menuDtoMap = dto.getMenuList().stream()
//                .collect(Collectors.toMap(InnerDto.MenuReqDto::getSizeId, InnerDto.MenuReqDto::getQty));
//
//        PaymentInfo paymentInfo = PaymentInfo.builder()
//                .cardCorp(dto.getCardCorp())
//                .cardPaymentPrice(dto.getCardPaymentPrice())
//                .build();
//
//
//        Purchase purchase = Purchase.builder()
//                .storeId(dto.getStoreId())
//                .storeOrderNumber(purchaseRepository.getStorePurchaseNumber(dto.getStoreId()))
//                .userAccountId(clientInfoLoader.getUserAccountId())
//                .userNickname(clientInfoLoader.getUserNickname())
//                .purchaseTotalPrice(dto.getPurchaseTotalPrice())
//                .build()
//                .linkToPaymentInfo(paymentInfo);
////                .linkToPurchaseMenuList(puchaseMenuList);
//
//        Long purchaseId = purchaseRepository.save(purchase).getId();
//        kafkaTemplate.send(PURCHASE_TO_STORE_TOPIC, purchaseId.toString());
        log.info("[{} {}] User's order has been completed", clientInfoLoader.getUserAccountId(), clientInfoLoader.getUserNickname());
        return true;
    }

    // 결제 완료 후 받는 API
    @Override
    public Boolean addPurchaseByAnonymous(AnonymousPurchaseReqDto dto) {

//        Map<Long, Integer> menuDtoMap = dto.getMenuList().stream()
//                .collect(Collectors.toMap(InnerDto.MenuReqDto::getSizeId, InnerDto.MenuReqDto::getQty));
//
//        PaymentInfo paymentInfo = PaymentInfo.builder()
//                .cardCorp(dto.getCardCorp())
//                .cardPaymentPrice(dto.getCardPaymentPrice())
//                .build();
//
//
//        Purchase purchase = Purchase.builder()
//                .storeId(dto.getStoreId())
//                .storeOrderNumber(purchaseRepository.getStorePurchaseNumber(dto.getStoreId()))
//                .anonymousName(dto.getAnonymousName())
//                .anonymousPhoneNumber(dto.getAnonymousPhoneNumber())
//                .purchaseTotalPrice(dto.getPurchaseTotalPrice())
//                .build()
//                .linkToPaymentInfo(paymentInfo);
//                .linkToPurchaseMenuList(puchaseMenuList);

//        Long purchaseId = purchaseRepository.save(purchase).getId();
//        kafkaTemplate.send(PURCHASE_TO_STORE_TOPIC, purchaseId.toString());
        log.info("[Anonymous] {} order has been completed", dto.getAnonymousName());
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
