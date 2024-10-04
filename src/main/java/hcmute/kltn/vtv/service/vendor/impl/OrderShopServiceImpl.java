package hcmute.kltn.vtv.service.vendor.impl;

import hcmute.kltn.vtv.model.dto.shipping.ShippingDTO;
import hcmute.kltn.vtv.model.entity.shipping.Transport;
import hcmute.kltn.vtv.model.extra.OrderStatus;
import hcmute.kltn.vtv.model.extra.TransportStatus;
import hcmute.kltn.vtv.service.shipping.IShippingService;
import hcmute.kltn.vtv.service.shipping.ITransportHandleService;
import hcmute.kltn.vtv.service.shipping.ITransportService;
import hcmute.kltn.vtv.service.vtv.IMailService;
import hcmute.kltn.vtv.service.vtv.INotificationService;
import hcmute.kltn.vtv.service.vtv.impl.DateServiceImpl;
import hcmute.kltn.vtv.service.wallet.ILoyaltyPointService;
import hcmute.kltn.vtv.service.wallet.IWalletService;
import hcmute.kltn.vtv.util.exception.BadRequestException;
import hcmute.kltn.vtv.model.data.vendor.response.PageOrderResponse;
import hcmute.kltn.vtv.model.data.user.response.ListOrderResponse;
import hcmute.kltn.vtv.model.data.user.response.OrderResponse;
import hcmute.kltn.vtv.model.entity.user.Order;
import hcmute.kltn.vtv.model.entity.user.OrderItem;
import hcmute.kltn.vtv.model.entity.vendor.Shop;
import hcmute.kltn.vtv.model.entity.user.VoucherOrder;
import hcmute.kltn.vtv.repository.user.OrderRepository;
import hcmute.kltn.vtv.service.user.*;
import hcmute.kltn.vtv.service.vendor.IOrderShopService;
import hcmute.kltn.vtv.service.vendor.IShopService;
import hcmute.kltn.vtv.util.exception.InternalServerErrorException;
import hcmute.kltn.vtv.util.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderShopServiceImpl implements IOrderShopService {

    private final OrderRepository orderRepository;
    private final IOrderItemService orderItemService;
    private final IVoucherOrderService voucherOrderService;
    private final IShopService shopService;
    private final IOrderService orderService;
    private final IMailService mailService;
    private final IShippingService shippingService;
    private final ILoyaltyPointService loyaltyPointService;
    private final INotificationService notificationService;
    private final ITransportService transportService;
    private final ITransportHandleService transportHandleService;
    private final IWalletService walletService;

    @Override
    public PageOrderResponse getPageOrder(String username, int page, int size) {
        Shop shop = shopService.getShopByUsername(username);
        Page<Order> pageOrder = orderRepository.findAllByShopShopIdOrderByCreateAtDesc(shop.getShopId(),
                        PageRequest.of(page - 1, size))
                .orElseThrow(() -> new NotFoundException("Không tìm danh sách đơn hàng nào trong cửa hàng sắp xếp theo thời gian mới nhất!"));

        return PageOrderResponse.pageOrderResponse(pageOrder, "Lấy danh sách đơn hàng thành công!", "OK");
    }


    @Override
    public PageOrderResponse getPageOrderByStatus(String username, OrderStatus status, int page, int size) {
        Shop shop = shopService.getShopByUsername(username);
        Page<Order> pageOrder = orderRepository.findAllByShopShopIdAndStatusOrderByCreateAtDesc(shop.getShopId(), status,
                        PageRequest.of(page - 1, size))
                .orElseThrow(() -> new NotFoundException("Không tìm thấy đơn hàng nào trong cửa hàng sắp xếp theo thời gian mới nhất và trạng thái!"));
        String message = orderService.messageByOrderStatus(status);

        return PageOrderResponse.pageOrderResponse(pageOrder, message, "OK");
    }


    @Override
    public ListOrderResponse getOrders(String username) {
        Shop shop = shopService.getShopByUsername(username);
        List<Order> orders = orderRepository.findAllByShopShopId(shop.getShopId())
                .orElseThrow(() -> new NotFoundException("Không tìm thấy đơn hàng nào!"));

        return ListOrderResponse.listOrderResponse(orders, "Lấy danh sách đơn hàng thành công!", "OK");
    }


    @Override
    public ListOrderResponse getOrdersByStatus(String username, OrderStatus status) {
        Shop shop = shopService.getShopByUsername(username);

        List<Order> orders = orderRepository.findAllByShopShopIdAndStatus(shop.getShopId(), status)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy đơn hàng nào!"));

        String message = orderService.messageByOrderStatus(status);

        return ListOrderResponse.listOrderResponse(orders, message, "OK");
    }


    @Override
    public ListOrderResponse getOrdersOnSameDay(String username, Date orderDate) {
        Shop shop = shopService.getShopByUsername(username);
        Date startOfDay = DateServiceImpl.formatStartOfDate(orderDate);
        Date endOfDay = DateServiceImpl.formatEndOfDate(orderDate);
        List<Order> orders = orderRepository.findAllByShopShopIdAndOrderDateBetween(shop.getShopId(), startOfDay, endOfDay)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy đơn hàng nào!"));

        return ListOrderResponse.listOrderResponse(orders, "Lấy danh sách đơn hàng trong cùng ngày thành công.", "OK");
    }


    @Override
    public ListOrderResponse getOrdersOnSameDayByStatus(String username, Date orderDate, OrderStatus status) {
        Shop shop = shopService.getShopByUsername(username);
        Date startOfDay = DateServiceImpl.formatStartOfDate(orderDate);
        Date endOfDay = DateServiceImpl.formatEndOfDate(orderDate);
        List<Order> orders = orderRepository
                .findAllByShopShopIdAndOrderDateBetweenAndStatus(shop.getShopId(), startOfDay, endOfDay, status)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy đơn hàng nào!"));
        String message = orderService.messageByOrderStatus(status);

        return ListOrderResponse.listOrderResponse(orders, message, "OK");
    }


    @Override
    public ListOrderResponse getOrdersBetweenDate(String username, Date startOfDay, Date endOfDay) {
        Shop shop = shopService.getShopByUsername(username);
        startOfDay = DateServiceImpl.formatStartOfDate(startOfDay);
        endOfDay = DateServiceImpl.formatEndOfDate(endOfDay);
        List<Order> orders = orderRepository.findAllByShopShopIdAndOrderDateBetween(shop.getShopId(), startOfDay, endOfDay)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy đơn hàng nào!"));

        return ListOrderResponse.listOrderResponse(orders, "Lấy danh sách đơn hàng trong khoảng thời gian thành công.", "OK");
    }


    @Override
    public ListOrderResponse getOrdersBetweenDateByStatus(String username, Date startOfDay, Date endOfDay,
                                                          OrderStatus status) {
        Shop shop = shopService.getShopByUsername(username);
        startOfDay = DateServiceImpl.formatStartOfDate(startOfDay);
        endOfDay = DateServiceImpl.formatEndOfDate(endOfDay);
        List<Order> orders = orderRepository
                .findAllByShopShopIdAndOrderDateBetweenAndStatus(shop.getShopId(), startOfDay, endOfDay, status)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy đơn hàng nào!"));

        String message = orderService.messageByOrderStatus(status);

        return ListOrderResponse.listOrderResponse(orders, message, "OK");
    }

    @Override
    public OrderResponse getOrderById(String username, UUID orderId) {
        Shop shop = shopService.getShopByUsername(username);
        Order order = orderRepository.findByOrderIdAndShopShopId(orderId, shop.getShopId())
                .orElseThrow(() -> new NotFoundException("Ma đơn hàng không có trong danh sách đơn hàng của cửa hàng!"));
        ShippingDTO shippingDTO = shippingService.getAndCalculateShippingDTO(order.getAddress().getWard().getWardCode(),
                shop.getWard().getWardCode(), order.getShippingMethod(), order.getCreateAt());
        Transport transport = transportService.getTransportByOrderId(orderId);
        return OrderResponse.orderResponse(order, transport, shippingDTO, "Lấy đơn hàng thành công!", "OK");
    }


    @Override
    @Transactional
    public OrderResponse updateStatusOrder(String username, UUID orderId, OrderStatus status) {
        Shop shop = shopService.getShopByUsername(username);
        checkExistOrderById(orderId);
        checkExistOrderByShop(orderId, shop.getShopId());
        Order order = orderRepository.findByOrderIdAndShopShopId(orderId, shop.getShopId())
                .orElseThrow(() -> new NotFoundException("Không tìm thấy đơn hàng trong cửa hàng!"));
        checkStatus(order, status);

        if (status.equals(OrderStatus.CANCEL) || status.equals(OrderStatus.RETURNED)) {
            return cancelOrderByShop(order);
        }


        order.setStatus(status);
        order.setUpdateAt(LocalDateTime.now());
        try {
            orderRepository.save(order);

            String messageResponse = "Cập nhật trạng " + messageUpdateStatusOrder(status) + " cho đơn hàng thành công!";
            TransportStatus transportStatus = convertOrderStatusToTransportStatus(status);

            return handleAfterSaveOrderShop(order,  messageResponse,  transportStatus);
        } catch (Exception e) {
            throw new InternalServerErrorException("Cập nhật trạng thái đơn hàng thất bại! " + e.getMessage());
        }
    }


    @Transactional
    public OrderResponse cancelOrderByShop(Order order) {

        if ((order.getPaymentMethod().equals("VNPay") || order.getPaymentMethod().equals("Wallet")) && !order.getStatus().equals(OrderStatus.UNPAID)) {
            walletService.updateWalletByUsername(order.getCustomer().getUsername(), order.getOrderId(), order.getTotalPrice(), "REFUND");
        }

        order.setStatus(OrderStatus.CANCEL);
        order.setUpdateAt(LocalDateTime.now());
        try {
            orderRepository.save(order);

            if (order.getVoucherOrders() != null) {
                for (VoucherOrder voucherOrder : order.getVoucherOrders()) {
                    voucherOrderService.cancelVoucherOrder(voucherOrder.getVoucherOrderId());
                }
            }
            if (order.getLoyaltyPointHistory() != null) {
                Long point = -order.getLoyaltyPointHistory().getPoint();
                loyaltyPointService.updatePointInLoyaltyPointByUsername(order.getCustomer().getUsername(), point, "REFUND");
            }
            List<OrderItem> orderItems = orderItemService.cancelOrderItem(order);
            order.setOrderItems(orderItems);

            String messageEmail = "Đơn hàng của bạn đã bị hủy bởi cửa hàng!";
            String messageResponse = "Đơn hàng của bạn đã bị hủy bởi cửa hàng!";
            String titleNotification = "Hủy đơn hàng";
            String bodyNotification = "Đơn hàng của bạn đã bị hủy bởi cửa hàng! Mã đơn hàng: #" + order.getOrderId();
            TransportStatus status = convertOrderStatusToTransportStatus(order.getStatus());

            return handleAfterSaveOrderShopWithMailAndNotification(order, messageEmail, messageResponse, titleNotification, bodyNotification, status);
        } catch (Exception e) {
            throw new InternalServerErrorException("Hủy đơn hàng từ cửa hàng thất bại!");
        }
    }


    public OrderResponse handleAfterSaveOrderShopWithMailAndNotification(Order order, String messageEmail, String messageResponse,
                                                                         String titleNotification, String bodyNotification, TransportStatus status) {
        try {
            transportHandleService.addNewTransportHandleByOrderId(
                    order.getOrderId(), order.getShop().getWard().getWardCode(),
                    order.getShop().getCustomer().getUsername(), true, status);
            ShippingDTO shippingDTO = shippingService.getCalculateShippingByWardAndTransportProvider(order.getAddress().getWard().getWardCode(),
                    order.getShop().getWard().getWardCode(), order.getShippingMethod()).getShippingDTO();
            Transport transport = transportService.getTransportByOrderId(order.getOrderId());
            mailService.sendOrderConfirmationEmail(order, shippingDTO, messageEmail);
            notificationService.addNewNotification(
                    titleNotification,
                    bodyNotification,
                    order.getCustomer().getUsername(),
                    order.getShop().getCustomer().getUsername(),
                    "ORDER"
            );

            return OrderResponse.orderResponse(order, transport, shippingDTO, messageResponse, "Success");
        } catch (Exception e) {
            throw new InternalServerErrorException("Xử lý đơn hàng thất bại!");
        }
    }

    public OrderResponse handleAfterSaveOrderShop(Order order, String messageResponse, TransportStatus status) {
        try {
            transportHandleService.addNewTransportHandleByOrderId(
                    order.getOrderId(), order.getShop().getWard().getWardCode(),
                    order.getShop().getCustomer().getUsername(), true, status);
            ShippingDTO shippingDTO = shippingService.getCalculateShippingByWardAndTransportProvider(order.getAddress().getWard().getWardCode(),
                    order.getShop().getWard().getWardCode(), order.getShippingMethod()).getShippingDTO();
            Transport transport = transportService.getTransportByOrderId(order.getOrderId());

            return OrderResponse.orderResponse(order, transport, shippingDTO, messageResponse, "Success");
        } catch (Exception e) {
            throw new InternalServerErrorException("Xử lý đơn hàng thất bại!");
        }
    }




    private void checkExistOrderById(UUID orderId) {
        if (!orderRepository.existsByOrderId(orderId)) {
            throw new NotFoundException("Mã đơn hàng không tồn tại!");
        }
    }


    private void checkExistOrderByShop(UUID orderId, Long shopId) {
        if (!orderRepository.existsByOrderIdAndShopShopId(orderId, shopId)) {
            throw new NotFoundException("Mã đơn hàng không tồn tại trong cửa hàng!");
        }
    }


    private String messageUpdateStatusOrder(OrderStatus status) {
        return switch (status) {
            case CANCEL -> "hủy";
            case PENDING -> "chờ xác nhận";
            case PROCESSING -> "xác nhận";
            case PICKUP_PENDING -> "chờ đơn vị vận chuyển lấy hàng";
            default -> "chờ xử lý";
        };
    }

    private TransportStatus convertOrderStatusToTransportStatus(OrderStatus status) {
        return switch (status) {
            case CANCEL -> TransportStatus.CANCEL;
            case PENDING -> TransportStatus.PENDING;
            case PROCESSING -> TransportStatus.PROCESSING;
            case PICKUP_PENDING -> TransportStatus.PICKUP_PENDING;
            default -> TransportStatus.WAITING;
        };
    }


    private void checkStatus(Order order, OrderStatus status) {

        if(order.getStatus().equals(OrderStatus.DELIVERED)){
            throw new BadRequestException("Đơn hàng đã được giao!");
        }

        if (order.getStatus().equals(OrderStatus.UNPAID)) {
            throw new BadRequestException("Đơn hàng chưa được thanh toán!");
        }

        if (order.getStatus().equals(OrderStatus.CANCEL)) {
            throw new BadRequestException("Đơn hàng đã bị hủy!");
        }

        if (order.getStatus().equals(OrderStatus.COMPLETED)) {
            throw new BadRequestException("Đơn hàng đã được giao!");
        }

        if (order.getStatus().equals(OrderStatus.RETURNED)) {
            throw new BadRequestException("Đơn hàng đã được trả!");
        }

        if (order.getStatus().equals(OrderStatus.REFUNDED)) {
            throw new BadRequestException("Đơn hàng đã được hoàn tiền!");
        }

        if (!status.equals(OrderStatus.PENDING) && !status.equals(OrderStatus.PROCESSING) &&
                !status.equals(OrderStatus.PICKUP_PENDING) && !status.equals(OrderStatus.WAITING) &&
                !status.equals(OrderStatus.CANCEL)) {
            throw new BadRequestException("Trạng thái không hợp lệ! Cửa hàng chỉ có thể cập nhật trạng thái: " +
                    "PENDING, PROCESSING, PICKUP_PENDING, WAITING, CANCEL");
        }

    }


}
