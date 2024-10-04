package hcmute.kltn.vtv.vnpay.service;


import hcmute.kltn.vtv.model.dto.shipping.ShippingDTO;
import hcmute.kltn.vtv.model.entity.user.Order;
import hcmute.kltn.vtv.model.extra.OrderStatus;
import hcmute.kltn.vtv.repository.user.OrderRepository;
import hcmute.kltn.vtv.service.shipping.IShippingService;
import hcmute.kltn.vtv.service.vtv.IMailService;
import hcmute.kltn.vtv.service.vtv.INotificationService;
import hcmute.kltn.vtv.service.wallet.ITransactionService;
import hcmute.kltn.vtv.service.wallet.IWalletService;
import hcmute.kltn.vtv.util.exception.BadRequestException;
import hcmute.kltn.vtv.util.exception.InternalServerErrorException;
import hcmute.kltn.vtv.util.exception.NotFoundException;
import hcmute.kltn.vtv.vnpay.service.impl.IOrderVNPayService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderVNPayServiceImpl implements IOrderVNPayService {

    private final OrderRepository orderRepository;
    private final INotificationService notificationService;
    private final IMailService mailService;
    private final IShippingService shippingService;
    private final IWalletService walletService;
    private final ITransactionService transactionService;


    @Async
    @Override
    @Transactional
    public void updateOrderStatusAfterPayment(UUID orderId) {
        Order order = orderRepository.findByOrderId(orderId)
                .orElseThrow(() -> new NotFoundException("Đơn hàng không tồn tại!"));
        if (!order.getStatus().equals(OrderStatus.UNPAID)) {
            throw new BadRequestException("Đơn hàng này không ở trạng thái chưa thanh toán!");
        }
        try {
            order.setStatus(OrderStatus.PENDING);
            order.setUpdateAt(LocalDateTime.now());

            orderRepository.save(order);
            handleOrderAfterPayment(order);
        } catch (Exception e) {
            throw new InternalServerErrorException("Cập nhật trạng thái đã thanh toán cho đơn hàng không thành công!");
        }

    }


    private void handleOrderAfterPayment(Order order) {
        handleNotificationAfterPayment(order);
        handleMailAfterPayment(order);
        transactionService.addNewTransaction(order.getCustomer().getUsername(), order.getOrderId(), -order.getPaymentTotal(), "PAYMENT_VNPAY");
    }


    private void handleNotificationAfterPayment(Order order) {
        String titleNotification = "Có đơn hàng: #" + order.getOrderId() + " đã được thanh toán!";
        String bodyNotification = "Bạn có đơn hàng mới từ tài khoản " + order.getCustomer().getUsername()
                + " với mã đơn hàng #" + order.getOrderId() + " đã được thanh toán!";
        notificationService.addNewNotification(
                titleNotification,
                bodyNotification,
                order.getShop().getCustomer().getUsername(),
                "system",
                "PAYMENT"
        );
    }


    private void handleMailAfterPayment(Order order) {
        ShippingDTO shippingDTO = shippingService.getCalculateShippingByWardAndTransportProvider(order.getAddress().getWard().getWardCode(),
                order.getShop().getWard().getWardCode(), order.getShippingMethod()).getShippingDTO();
        String messageEmail = "Đặt hàng thành công.";
        mailService.sendOrderConfirmationEmail(order, shippingDTO, messageEmail);
    }


}
