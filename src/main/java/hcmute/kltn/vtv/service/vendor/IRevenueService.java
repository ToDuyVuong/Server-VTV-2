package hcmute.kltn.vtv.service.vendor;

import hcmute.kltn.vtv.model.data.vendor.response.RevenueProductResponse;
import hcmute.kltn.vtv.model.data.vtv.response.StatisticsOrdersResponse;
import hcmute.kltn.vtv.model.data.vtv.response.StatisticsProductsResponse;
import hcmute.kltn.vtv.model.extra.OrderStatus;

import java.util.Date;

public interface IRevenueService {


    StatisticsOrdersResponse statisticsOrderByDateAndStatus(Date startDate, Date endDate, OrderStatus status, String username);

    StatisticsProductsResponse getTopProductByLimitAndDate(int limit, Date startDate, Date endDate, String username);

    RevenueProductResponse getRevenueProductResponseByProductIdAndDate(Long productId, Date startDate, Date endDate, String username);

    String messageByOrderStatus(OrderStatus status);
}
