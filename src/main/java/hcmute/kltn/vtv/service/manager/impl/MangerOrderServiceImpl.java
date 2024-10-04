package hcmute.kltn.vtv.service.manager.impl;

import hcmute.kltn.vtv.model.data.user.response.OrderResponse;
import hcmute.kltn.vtv.model.data.vendor.response.PageOrderResponse;
import hcmute.kltn.vtv.model.dto.shipping.ShippingDTO;
import hcmute.kltn.vtv.model.entity.shipping.Transport;
import hcmute.kltn.vtv.model.entity.user.Order;
import hcmute.kltn.vtv.model.extra.OrderStatus;
import hcmute.kltn.vtv.repository.user.OrderRepository;
import hcmute.kltn.vtv.service.manager.IMangerOrderService;
import hcmute.kltn.vtv.service.shipping.IShippingService;
import hcmute.kltn.vtv.service.shipping.ITransportService;
import hcmute.kltn.vtv.service.vendor.IRevenueService;
import hcmute.kltn.vtv.service.vtv.impl.DateServiceImpl;
import hcmute.kltn.vtv.util.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MangerOrderServiceImpl implements IMangerOrderService {

    private final OrderRepository orderRepository;
    private final IRevenueService revenueService;
    private final IShippingService shippingService;
    private final ITransportService transportService;

    @Override
    public PageOrderResponse getOrderPageByStatusAndDate(int page, int size, OrderStatus status, Date startDate, Date endDate) {
        Page<Order> orders = orderRepository
                .findAllByStatusAndOrderDateBetweenOrderByUpdateAtDesc(status, startDate, endDate, PageRequest.of(page - 1, size))
                .orElseThrow(() -> new NotFoundException("Không tìm thấy đơn hàng nào."));
        String message = "Danh sách đơn hàng " + revenueService.messageByOrderStatus(status) + " từ ngày " + DateServiceImpl.formatStringDate(startDate)
                + " đến ngày " + DateServiceImpl.formatStringDate(endDate) + ".";

        return PageOrderResponse.pageOrderResponse(orders, message, "OK");
    }


    @Override
    public OrderResponse getOrderDetailByOrderId(UUID orderId) {
        if (!orderRepository.existsByOrderId(orderId)) {
            throw new NotFoundException("Không tìm thấy đơn hàng nào với mã đơn hàng: " + orderId);
        }

        Order order = orderRepository.findById(orderId).orElseThrow(() -> new NotFoundException("Không tìm thấy đơn hàng nào."));
        ShippingDTO shippingDTO = shippingService.getAndCalculateShippingDTO(order.getAddress().getWard().getWardCode(),
                order.getShop().getWard().getWardCode(), order.getShippingMethod(), order.getCreateAt());
        Transport transport = transportService.getTransportByOrderId(orderId);

        return OrderResponse.orderResponse(order,  transport, shippingDTO,  "Xem chi tiết đơn hàng thành công.", "OK");
    }


}
