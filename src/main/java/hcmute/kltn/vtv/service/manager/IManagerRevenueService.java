package hcmute.kltn.vtv.service.manager;

import hcmute.kltn.vtv.model.data.vtv.response.*;
import hcmute.kltn.vtv.model.extra.OrderStatus;

import java.util.Date;

public interface IManagerRevenueService {
    StatisticsCustomersResponse statisticsCustomersByDateAndStatus(Date startDate, Date endDate);

    StatisticsOrdersResponse statisticsOrderByDateAndStatus(Date startDate, Date endDate, OrderStatus status);

    StatisticsProductsResponse getTopProductByLimitAndDate(int limit, Date startDate, Date endDate);

    StatisticsTransportsResponse statisticsTransportsByDateAndShippingMethod(Date startDate, Date endDate, String shippingMethod);

    StatisticsFeeOrderResponse statisticsFeeOrderByDate(Date startDate, Date endDate);
}
