package hcmute.kltn.vtv.service.vtv.impl;

import hcmute.kltn.vtv.model.entity.shipping.CashOrder;
import hcmute.kltn.vtv.model.entity.user.Order;
import hcmute.kltn.vtv.model.extra.OrderStatus;
import hcmute.kltn.vtv.repository.shipping.CashOrderRepository;
import hcmute.kltn.vtv.repository.user.OrderRepository;
import hcmute.kltn.vtv.repository.wallet.TransactionRepository;
import hcmute.kltn.vtv.service.shipping.ICashOrderService;
import hcmute.kltn.vtv.service.vtv.ICashOrderSchedulerService;
import hcmute.kltn.vtv.service.wallet.IWalletService;
import hcmute.kltn.vtv.util.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CashOrderSchedulerServiceImpl implements ICashOrderSchedulerService {
    private final OrderRepository orderRepository;
    private final IWalletService walletService;
    private final ICashOrderService cashOrderService;
    private final CashOrderRepository cashOrderRepository;
    private final TransactionRepository transactionRepository;


    @Override
    @Transactional
    public void autoHandlePaymentCashOrderAfterFiveDayCompleted() {
        // start localDateTime start day
        LocalDateTime startDay = LocalDateTime.now().minusDays(5);
        // end localDateTime start day
        LocalDateTime endDay = LocalDateTime.now().minusDays(4);
        List<Order> orders = ordersByOrderPaymentMethodAndStatusWtihUpdateAtBetween("COD", OrderStatus.COMPLETED, startDay, endDay);

        try {
            orders.forEach(order -> handlePaymentCashOrderByOrderId(order.getOrderId()));
        }catch (Exception e) {
            e.printStackTrace();
        }

    }






    public void handlePaymentCashOrderByOrderId(UUID orderId) {
        if (transactionRepository.existsAllByOrderId(orderId)) {
            return;
        }
        CashOrder cashOrder = cashOrderByOrderId(orderId);

        if (cashOrder.getMoney() > 0L && !cashOrder.isHandlePayment() &&
                cashOrderService.checkStatusOrderIsCompletedByOrderId(cashOrder.getOrderId())) {
            walletService.updateWalletByOrderId(orderId, "COMPLETED_ORDER_COD_SYSTEM");
        }
    }



    private List<Order> ordersByOrderPaymentMethodAndStatusWtihUpdateAtBetween(String paymentMethod, OrderStatus status, LocalDateTime startDay, LocalDateTime endDay) {

        return orderRepository.findAllByPaymentMethodAndStatusAndUpdateAtBetween(paymentMethod, status, startDay, endDay)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy đơn hàng!"));
    }

    private CashOrder cashOrderByOrderId(UUID orderId) {
        return cashOrderRepository.findByOrderId(orderId)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy đơn hàng!"));
    }




}
