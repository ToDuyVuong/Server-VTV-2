package hcmute.kltn.vtv.vnpay.service.impl;

import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

public interface IOrderVNPayService {
    @Async
    @Transactional
    void updateOrderStatusAfterPayment(UUID orderId);
}
