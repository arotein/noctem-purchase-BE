package noctem.purchaseService.purchase.service;

import noctem.purchaseService.purchase.dto.request.AnonymousPurchaseReqDto;
import noctem.purchaseService.purchase.dto.request.GetAllUserPurchaseQueryReqDto;
import noctem.purchaseService.purchase.dto.request.UserPurchaseReqDto;
import noctem.purchaseService.purchase.dto.response.PurchaseListResDto;
import noctem.purchaseService.purchase.dto.response.PurchaseResDto;
import noctem.purchaseService.purchase.dto.response.ReceiptDetailResDto;

public interface PurchaseService {
    PurchaseResDto addPurchaseByUser(UserPurchaseReqDto dto);

    PurchaseResDto addPurchaseByAnonymous(AnonymousPurchaseReqDto dto);

    PurchaseListResDto getAllUserPurchase(GetAllUserPurchaseQueryReqDto dto);

    PurchaseListResDto getAllAnonymousPurchase(String name, String phoneNumber);

    ReceiptDetailResDto getPurchaseDetail(String purchaseSerialNumber);
}
