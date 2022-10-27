package noctem.purchaseService.purchase.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import noctem.purchaseService.AppConfig;
import noctem.purchaseService.global.common.CommonException;
import noctem.purchaseService.global.enumeration.Sex;
import noctem.purchaseService.global.security.bean.ClientInfoLoader;
import noctem.purchaseService.purchase.domain.entity.PaymentInfo;
import noctem.purchaseService.purchase.domain.entity.Purchase;
import noctem.purchaseService.purchase.domain.entity.PurchaseMenu;
import noctem.purchaseService.purchase.domain.repository.PurchaseRepository;
import noctem.purchaseService.purchase.dto.InnerDto;
import noctem.purchaseService.purchase.dto.request.AnonymousPurchaseReqDto;
import noctem.purchaseService.purchase.dto.request.GetAllUserPurchaseQueryReqDto;
import noctem.purchaseService.purchase.dto.request.UserPurchaseReqDto;
import noctem.purchaseService.purchase.dto.response.PurchaseListResDto;
import noctem.purchaseService.purchase.dto.response.PurchaseResDto;
import noctem.purchaseService.purchase.dto.response.ReceiptDetailResDto;
import noctem.purchaseService.purchase.dto.vo.PurchaseResultVo;
import org.springframework.http.HttpStatus;
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
    public PurchaseResDto addPurchaseByUser(UserPurchaseReqDto dto) {
        // 퍼스널 옵션은 현재 미구현이므로 제외.
        List<PurchaseMenu> purchaseMenuList = dto.getMenuList().stream().map(e -> PurchaseMenu.builder()
                        .sizeId(e.getSizeId())
                        .menuFullName(e.getMenuFullName())
                        .menuShortName(e.getMenuShortName())
                        .menuTotalPrice(e.getMenuTotalPrice())
                        .qty(e.getQty())
                        .build())
                .collect(Collectors.toList());

        PaymentInfo paymentInfo = PaymentInfo.builder()
                .cardCorp(dto.getCardCorp())
                .cardPaymentPrice(dto.getCardPaymentPrice())
                .build();

        Purchase purchase = Purchase.builder()
                .storeId(dto.getStoreId())
                .storeOrderNumber(purchaseRepository.getStorePurchaseNumber(dto.getStoreId()))
                .storeName(dto.getStoreName())
                .storeAddress(dto.getStoreAddress())
                .storeContactNumber(dto.getStoreContactNumber())
                .userAccountId(clientInfoLoader.getUserAccountId())
                .userNickname(clientInfoLoader.getUserNickname())
                .age(dto.getUserAge())
                .sex(Sex.findByValue(dto.getUserSex()))
                .purchaseTotalPrice(dto.getPurchaseTotalPrice())
                .build()
                .linkToPaymentInfo(paymentInfo)
                .linkToPurchaseMenuList(purchaseMenuList);

        Long purchaseId = purchaseRepository.save(purchase).getId();
        try {
            kafkaTemplate.send(PURCHASE_TO_STORE_TOPIC,
                    AppConfig.objectMapper().writeValueAsString(new PurchaseResultVo(dto.getStoreId(), purchaseId)));
            log.info("Send purchaseId through [{}] TOPIC", PURCHASE_TO_STORE_TOPIC);
        } catch (JsonProcessingException e) {
            log.warn("JsonProcessingException in addPurchaseByUser");
            throw CommonException.builder().errorCode(6002).httpStatus(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        log.info("[{} {}] User's order has been completed", clientInfoLoader.getUserAccountId(), clientInfoLoader.getUserNickname());
        return new PurchaseResDto(dto.getStoreId(), purchaseId);
    }

    // 결제 완료 후 받는 API
    @Override
    public PurchaseResDto addPurchaseByAnonymous(AnonymousPurchaseReqDto dto) {
        // 퍼스널 옵션은 현재 미구현이므로 제외.
        List<PurchaseMenu> purchaseMenuList = dto.getMenuList().stream().map(e -> PurchaseMenu.builder()
                        .sizeId(e.getSizeId())
                        .menuFullName(e.getMenuFullName())
                        .menuShortName(e.getMenuShortName())
                        .menuTotalPrice(e.getMenuTotalPrice())
                        .qty(e.getQty())
                        .build())
                .collect(Collectors.toList());

        PaymentInfo paymentInfo = PaymentInfo.builder()
                .cardCorp(dto.getCardCorp())
                .cardPaymentPrice(dto.getCardPaymentPrice())
                .build();

        Purchase purchase = Purchase.builder()
                .storeId(dto.getStoreId())
                .storeOrderNumber(purchaseRepository.getStorePurchaseNumber(dto.getStoreId()))
                .anonymousName(dto.getAnonymousName())
                .anonymousPhoneNumber(dto.getAnonymousPhoneNumber())
                .age(dto.getAnonymousAge())
                .sex(Sex.findByValue(dto.getAnonymousSex()))
                .purchaseTotalPrice(dto.getPurchaseTotalPrice())
                .build()
                .linkToPaymentInfo(paymentInfo)
                .linkToPurchaseMenuList(purchaseMenuList);

        Long purchaseId = purchaseRepository.save(purchase).getId();
        kafkaTemplate.send(PURCHASE_TO_STORE_TOPIC, purchaseId.toString());
        log.info("Send purchaseId through [{}] TOPIC", PURCHASE_TO_STORE_TOPIC);
        log.info("[Anonymous] {} order has been completed", dto.getAnonymousName());
        return new PurchaseResDto(dto.getStoreId(), purchaseId);
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
