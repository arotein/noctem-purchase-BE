package noctem.purchaseService.purchase.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import noctem.purchaseService.global.enumeration.Amount;
import noctem.purchaseService.global.enumeration.Sex;
import noctem.purchaseService.global.security.bean.ClientInfoLoader;
import noctem.purchaseService.purchase.domain.entity.PaymentInfo;
import noctem.purchaseService.purchase.domain.entity.PersonalOption;
import noctem.purchaseService.purchase.domain.entity.Purchase;
import noctem.purchaseService.purchase.domain.entity.PurchaseMenu;
import noctem.purchaseService.purchase.domain.repository.PurchaseRepository;
import noctem.purchaseService.purchase.dto.InnerDto;
import noctem.purchaseService.purchase.dto.request.AnonymousPurchaseReqDto;
import noctem.purchaseService.purchase.dto.request.GetAllUserPurchaseQueryReqDto;
import noctem.purchaseService.purchase.dto.request.UserPurchaseReqDto;
import noctem.purchaseService.purchase.dto.response.PurchaseListResDto;
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
    private final PurchaseRepository purchaseRepository;
    private final ClientInfoLoader clientInfoLoader;

    // 결제 완료 후 받는 API
    @Override
    public Boolean addPurchaseByUser(UserPurchaseReqDto dto) {
        PaymentInfo paymentInfo = PaymentInfo.builder()
                .cardCorp(dto.getCardCorp())
                .cardPaymentPrice(dto.getCardPaymentPrice())
                .build();

        List<PurchaseMenu> puchaseMenuList = dto.getMenuList().stream()
                .map(e -> PurchaseMenu.builder()
                        .menuFullName(e.getMenuFullName())
                        .menuShortName(e.getMenuShortName())
                        .qty(e.getQty())
                        .menuTotalPrice(e.getMenuTotalPrice())
                        .build()
                        .linkToPersonalOptionList(
                                e.getOptionList().stream().map(
                                        o -> PersonalOption.builder()
                                                .personalOptionName(o.getPersonalOptionName())
                                                .amount(Amount.findByValue(o.getAmount()))
                                                .build()
                                ).collect(Collectors.toList())
                        )
                ).collect(Collectors.toList());

        Purchase purchase = Purchase.builder()
                .storeId(dto.getStoreId())
                .storeName(dto.getStoreName())
                .storePurchaseNumber(purchaseRepository.getStorePurchaseNumber(dto.getStoreId()))
                .storeAddress(dto.getStoreAddress())
                .storeContactNumber(dto.getStoreContactNumber())
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
        PaymentInfo paymentInfo = PaymentInfo.builder()
                .cardCorp(dto.getCardCorp())
                .cardPaymentPrice(dto.getCardPaymentPrice())
                .build();

        List<PurchaseMenu> puchaseMenuList = dto.getMenuList().stream()
                .map(e -> PurchaseMenu.builder()
                        .menuFullName(e.getMenuFullName())
                        .menuShortName(e.getMenuShortName())
                        .qty(e.getQty())
                        .menuTotalPrice(e.getMenuTotalPrice())
                        .build()
                        .linkToPersonalOptionList(
                                e.getOptionList().stream().map(
                                        o -> PersonalOption.builder()
                                                .personalOptionName(o.getPersonalOptionName())
                                                .amount(Amount.findByValue(o.getAmount()))
                                                .build()
                                ).collect(Collectors.toList())
                        )
                ).collect(Collectors.toList());

        Purchase purchase = Purchase.builder()
                .storeId(dto.getStoreId())
                .storeName(dto.getStoreName())
                .storePurchaseNumber(purchaseRepository.getStorePurchaseNumber(dto.getStoreId()))
                .storeAddress(dto.getStoreAddress())
                .storeContactNumber(dto.getStoreContactNumber())
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

        if (dto.getStartDate() == null || dto.getEndDate() == null) {
            startDate = LocalDate.now().minusMonths(1L).atStartOfDay();
            endDate = LocalDateTime.now();
        } else {
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            startDate = LocalDate.parse(dto.getStartDate(), dateTimeFormatter).atStartOfDay();
            endDate = LocalDate.parse(dto.getEndDate(), dateTimeFormatter).plusDays(1L).atStartOfDay();
        }
        List<Purchase> purchaseList = purchaseRepository.findAllByUserAccountIdAndPaymentInfoApprovedAtBetween(
                clientInfoLoader.getUserAccountId(),
                startDate,
                endDate);
        List<InnerDto.UserPurchaseResDto> innerDtoList = purchaseList.stream()
                .map(e -> new InnerDto.UserPurchaseResDto(
                        null,
                        e.getId(),
                        e.getStoreName(),
                        e.getPaymentInfo().getApprovedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                        e.getPurchaseTotalPrice()))
                .collect(Collectors.toList());

        return new PurchaseListResDto(innerDtoList);
    }

    @Transactional(readOnly = true)
    @Override
    public Object getUserPurchaseDetail(String purchaseId) {
        // 결제시 주문 정보 + 매장정보를 타 서비스에서 미리 다 조회해서 저장해야할까?
        // 전자영수증을 매번 타 서비스 조회해서 그때그때 생성하는것도 이상하지않나?
        return null;
    }

    @Transactional(readOnly = true)
    @Override
    public PurchaseListResDto getAllAnonymousPurchase(String name, String phoneNumber) {
        LocalDateTime startDate = LocalDate.now().minusDays(1L).atStartOfDay();
        LocalDateTime endDate = LocalDate.now().plusDays(1L).atStartOfDay();
        List<Purchase> purchaseList = purchaseRepository.findAllAnonymousPurchase(name, phoneNumber, startDate, endDate);
        List<InnerDto.UserPurchaseResDto> innerDtoList = purchaseList.stream()
                .map(e -> new InnerDto.UserPurchaseResDto(
                        null,
                        e.getId(),
                        e.getStoreName(),
                        e.getPaymentInfo().getApprovedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                        e.getPurchaseTotalPrice()))
                .collect(Collectors.toList());

        return new PurchaseListResDto(innerDtoList);
    }

    @Transactional(readOnly = true)
    @Override
    public Object getAnonymousPurchaseDetail(String anonymousName, String anonymousPhoneNumber, Long purchaseId) {
        return null;
    }
}
