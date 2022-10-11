package noctem.purchaseService.purchase.service;

import noctem.purchaseService.purchase.dto.request.AnonymousPurchaseReqDto;
import noctem.purchaseService.purchase.dto.request.GetAllUserPurchaseQueryReqDto;
import noctem.purchaseService.purchase.dto.request.UserPurchaseReqDto;
import noctem.purchaseService.purchase.dto.response.PurchaseListResDto;

public interface PurchaseService {
    Boolean addPurchaseByUser(UserPurchaseReqDto dto);

    Boolean addPurchaseByAnonymous(AnonymousPurchaseReqDto dto);

    PurchaseListResDto getAllUserPurchase(GetAllUserPurchaseQueryReqDto dto);

    Object getUserPurchaseDetail(String purchaseId);

    PurchaseListResDto getAllAnonymousPurchase(String name, String phoneNumber);

    Object getAnonymousPurchaseDetail(String anonymousName, String anonymousPhoneNumber, Long purchaseId);
}
