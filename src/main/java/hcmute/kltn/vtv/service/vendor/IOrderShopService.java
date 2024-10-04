package hcmute.kltn.vtv.service.vendor;

import hcmute.kltn.vtv.model.data.vendor.response.PageOrderResponse;
import hcmute.kltn.vtv.model.data.user.response.ListOrderResponse;
import hcmute.kltn.vtv.model.data.user.response.OrderResponse;
import hcmute.kltn.vtv.model.extra.OrderStatus;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.UUID;

public interface IOrderShopService {
    PageOrderResponse getPageOrder(String username, int page, int size);

    PageOrderResponse getPageOrderByStatus(String username, OrderStatus status, int page, int size);

    ListOrderResponse getOrders(String username);

    ListOrderResponse getOrdersByStatus(String username, OrderStatus status);

    ListOrderResponse getOrdersOnSameDay(String username, Date orderDate);

    ListOrderResponse getOrdersOnSameDayByStatus(String username, Date orderDate, OrderStatus status);

    ListOrderResponse getOrdersBetweenDate(String username, Date startOrderDate, Date endOrderDate);

    ListOrderResponse getOrdersBetweenDateByStatus(String username, Date startOrderDate, Date endOrderDate,
                                                   OrderStatus status);

    OrderResponse getOrderById(String username, UUID orderId);

    @Transactional
    OrderResponse updateStatusOrder(String username, UUID orderId, OrderStatus status);


}
