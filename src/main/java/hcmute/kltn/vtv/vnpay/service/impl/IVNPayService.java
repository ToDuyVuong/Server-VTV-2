package hcmute.kltn.vtv.vnpay.service.impl;

import hcmute.kltn.vtv.vnpay.model.VNPayDTO;
import hcmute.kltn.vtv.vnpay.model.VNPayResponse;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.UUID;

public interface IVNPayService {
    VNPayResponse createPaymentByVNPay(UUID orderId, String ipAddress, String username);
    VNPayResponse createPaymentByVNPayForWeb(UUID orderId, String ipAddress, String username);

    VNPayResponse createPaymentByVNPayWithMultipleOrder(List<UUID> orderIds, String ipAddress, String username);
    VNPayResponse createPaymentByVNPayWithMultipleOrderForWeb(List<UUID> orderIds, String ipAddress, String username);

    VNPayDTO checkPaymentByVNPay(String vnp_TxnRef, String ipAddress, HttpServletRequest req) throws Exception;
}
