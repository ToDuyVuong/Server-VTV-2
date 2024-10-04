package hcmute.kltn.vtv.service.vtv.impl;

import hcmute.kltn.vtv.model.entity.user.Order;
import hcmute.kltn.vtv.model.extra.OrderStatus;
import hcmute.kltn.vtv.model.extra.Status;
import hcmute.kltn.vtv.repository.user.OrderRepository;
import hcmute.kltn.vtv.service.shipping.ICashOrderService;
import hcmute.kltn.vtv.service.vtv.IOrderSchedulerService;
import hcmute.kltn.vtv.service.wallet.IWalletService;
import hcmute.kltn.vtv.util.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class OrderSchedulerServiceImpl implements IOrderSchedulerService {

    private final OrderRepository orderRepository;
    private final IWalletService walletService;
    private final ICashOrderService cashOrderService;

    @Override
    @Transactional
    public void autoCompleteOrderAfterFiveDayDelivered() {
        List<Order> orders = orderRepository.findAllByStatus(OrderStatus.DELIVERED)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy đơn hàng!"));

        checkAndUpdateOrderStatus(orders);
    }

    private void checkAndUpdateOrderStatus(List<Order> orders) {
        for (Order order : orders) {
            LocalDateTime deliveredDate = order.getUpdateAt();
            LocalDateTime fiveDaysAfterDelivery = deliveredDate.plusDays(5);
            if (LocalDateTime.now().isAfter(fiveDaysAfterDelivery)) {
                // Nếu đã qua 5 ngày kể từ ngày giao hàng, cập nhật trạng thái đơn hàng thành đã nhận hàng
                order.setStatus(OrderStatus.COMPLETED);
                try {
                    if (!order.getPaymentMethod().equals("COD")) {
                        handlePayment(order, order.getOrderId(), "COMPLETED_ORDER");
                    } else if (cashOrderService.checkCashOrderByOrderIdWithHandlePaymentAndStatus(
                            order.getOrderId(), true, false, false, Status.ACTIVE)) {
                        handlePayment(order,  order.getOrderId(), "COMPLETED_ORDER_COD");
                    }else {
                        // chuyen tien cho shop khi giao hang chua chuyen tien
                        handlePayment(order, order.getOrderId(), "COMPLETED_ORDER_COD_SYSTEM");
//                        cashOrderService.updateCashOrderByOrderIdWithSchedule(order.getOrderId());
                    }
                    orderRepository.save(order);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }


    private void handlePayment(Order order, UUID orderId, String paymentType) {
        cashOrderService.updateCashOrderByOrderIdWithHandlePayment(orderId);
        walletService.updateWalletByUsername(
                order.getShop().getCustomer().getUsername(),
                order.getOrderId(),
                order.getTotalPrice() * 96 / 100 - order.getDiscountShop(),
                paymentType);
    }


}
