package noctem.purchaseService.purchase.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import noctem.purchaseService.AppConfig;
import noctem.purchaseService.global.common.CommonException;
import noctem.purchaseService.global.enumeration.CategorySmall;
import noctem.purchaseService.global.enumeration.CupType;
import noctem.purchaseService.global.enumeration.Sex;
import noctem.purchaseService.global.security.bean.ClientInfoLoader;
import noctem.purchaseService.purchase.domain.entity.PaymentInfo;
import noctem.purchaseService.purchase.domain.entity.Purchase;
import noctem.purchaseService.purchase.domain.entity.PurchaseMenu;
import noctem.purchaseService.purchase.domain.repository.PurchaseRepository;
import noctem.purchaseService.purchase.domain.repository.RedisRepository;
import noctem.purchaseService.purchase.dto.InnerDto;
import noctem.purchaseService.purchase.dto.request.AnonymousPurchaseReqDto;
import noctem.purchaseService.purchase.dto.request.GetAllUserPurchaseQueryReqDto;
import noctem.purchaseService.purchase.dto.request.UserPurchaseReqDto;
import noctem.purchaseService.purchase.dto.response.PurchaseListResDto;
import noctem.purchaseService.purchase.dto.response.PurchaseResDto;
import noctem.purchaseService.purchase.dto.response.ReceiptDetailResDto;
import noctem.purchaseService.purchase.dto.vo.PurchaseToStoreVo;
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
    private final String PURCHASE_FROM_USER_TOPIC = "purchase-from-user-alert";
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final PurchaseRepository purchaseRepository;
    private final RedisRepository redisRepository;
    private final ClientInfoLoader clientInfoLoader;

    // ?????? ?????? ??? ?????? API
    @Override
    public PurchaseResDto addPurchaseByUser(UserPurchaseReqDto dto) {
        // ????????? ????????? ?????? ?????????????????? ??????.
        try {
            List<PurchaseMenu> purchaseMenuList = dto.getMenuList().stream().map(e -> PurchaseMenu.builder()
                            .sizeId(e.getSizeId())
                            .categorySmall(CategorySmall.findByValue(e.getCategorySmall()))
                            .menuFullName(e.getMenuFullName())
                            .menuShortName(e.getMenuShortName())
                            .cupType(CupType.findByValue(e.getCupType()))
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
                    .storeOrderNumber(redisRepository.getStorePurchaseNumber(dto.getStoreId()))
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
            Integer totalMenuQty = 0;
            for (InnerDto.MenuReqDto menuDto : dto.getMenuList()) {
                totalMenuQty += menuDto.getQty();
            }
            // Store Service??? ??????
            kafkaTemplate.send(PURCHASE_TO_STORE_TOPIC,
                    AppConfig.objectMapper().writeValueAsString(new PurchaseToStoreVo(
                            dto.getStoreId(),
                            purchaseId,
                            dto.getMenuList().get(0).getMenuFullName(),
                            totalMenuQty)));
            // ????????? ?????? ???????????? ????????? ??????
            redisRepository.setOrderInProgress(clientInfoLoader.getUserAccountId(), purchase.getId());

            log.info("Send purchaseId through [{}] TOPIC", PURCHASE_TO_STORE_TOPIC);
            log.info("[{} {}] User's order has been completed", clientInfoLoader.getUserAccountId(), clientInfoLoader.getUserNickname());
            return new PurchaseResDto(dto.getStoreId(), purchaseId);
        } catch (JsonProcessingException e) {
            log.warn("JsonProcessingException in addPurchaseByUser");
            throw CommonException.builder().errorCode(6002).httpStatus(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } catch (Exception e) {
            log.warn("Redis roolback in addPurchaseByUser");
            redisRepository.rollbackStorePurchaseNumber(dto.getStoreId());
            throw CommonException.builder().errorCode(6004).httpStatus(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // ?????? ?????? ??? ?????? API
    @Override
    public PurchaseResDto addPurchaseByAnonymous(AnonymousPurchaseReqDto dto) {
        // ????????? ????????? ?????? ?????????????????? ??????.
        try {
            List<PurchaseMenu> purchaseMenuList = dto.getMenuList().stream().map(e -> PurchaseMenu.builder()
                            .sizeId(e.getSizeId())
                            .categorySmall(CategorySmall.findByValue(e.getCategorySmall()))
                            .menuFullName(e.getMenuFullName())
                            .menuShortName(e.getMenuShortName())
                            .cupType(CupType.findByValue(e.getCupType()))
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
                    .storeOrderNumber(redisRepository.getStorePurchaseNumber(dto.getStoreId()))
                    .anonymousName(dto.getAnonymousName())
                    .anonymousPhoneNumber(dto.getAnonymousPhoneNumber())
                    .age(dto.getAnonymousAge())
                    .sex(Sex.findByValue(dto.getAnonymousSex()))
                    .purchaseTotalPrice(dto.getPurchaseTotalPrice())
                    .build()
                    .linkToPaymentInfo(paymentInfo)
                    .linkToPurchaseMenuList(purchaseMenuList);

            Long purchaseId = purchaseRepository.save(purchase).getId();
            Integer totalMenuQty = 0;
            for (InnerDto.MenuReqDto menuDto : dto.getMenuList()) {
                totalMenuQty += menuDto.getQty();
            }
            // ?????? ?????? ????????? ??????
            // Store Service??? ??????
            log.info("[Anonymous] {} order has been completed", dto.getAnonymousName());
            return new PurchaseResDto(dto.getStoreId(), purchaseId);
        } catch (Exception e) {
            log.warn("Redis roolback in addPurchaseByAnonymous");
            redisRepository.rollbackStorePurchaseNumber(dto.getStoreId());
            throw CommonException.builder().errorCode(6005).httpStatus(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Transactional(readOnly = true)
    @Override
    public PurchaseListResDto getAllUserPurchase(GetAllUserPurchaseQueryReqDto dto) {
        LocalDateTime startDate = null;
        LocalDateTime endDate = null;
        // ?????? ?????? ??????
        if (dto.getStartDate() == null || dto.getEndDate() == null) {
            startDate = LocalDate.now().minusMonths(1L).atStartOfDay();
            endDate = LocalDateTime.now();
        } else {
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            startDate = LocalDate.parse(dto.getStartDate(), dateTimeFormatter).atStartOfDay();
            endDate = LocalDate.parse(dto.getEndDate(), dateTimeFormatter).plusDays(1L).atStartOfDay();
        }
        // Entity ??????
        List<Purchase> purchaseList = purchaseRepository.findAllByUserAccountIdAndPaymentInfoApprovedAtBetween(
                clientInfoLoader.getUserAccountId(),
                startDate,
                endDate);
        // DTO ??????
        List<InnerDto.UserPurchaseResDto> innerDtoList = purchaseList.stream()
                .map(InnerDto.UserPurchaseResDto::new)
                .collect(Collectors.toList());

        return new PurchaseListResDto(innerDtoList);
    }

    @Transactional(readOnly = true)
    @Override
    public PurchaseListResDto getAllAnonymousPurchase(String name, String phoneNumber) {
        // ?????? ?????? ??????
        LocalDateTime startDate = LocalDate.now().minusDays(1L).atStartOfDay();
        LocalDateTime endDate = LocalDate.now().plusDays(1L).atStartOfDay();
        // Entity ??????
        List<Purchase> purchaseList = purchaseRepository.findAllAnonymousPurchase(name, phoneNumber, startDate, endDate);
        // DTO ??????
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
