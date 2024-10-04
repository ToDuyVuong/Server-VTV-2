package hcmute.kltn.vtv.service.manager;

import hcmute.kltn.vtv.model.data.user.response.OrderResponse;
import hcmute.kltn.vtv.model.data.vendor.response.PageOrderResponse;
import hcmute.kltn.vtv.model.extra.OrderStatus;

import java.util.Date;
import java.util.UUID;

public interface IMangerOrderService {
    PageOrderResponse getOrderPageByStatusAndDate(int page, int size, OrderStatus status, Date startDate, Date endDate);

    OrderResponse getOrderDetailByOrderId(UUID orderId);
}
