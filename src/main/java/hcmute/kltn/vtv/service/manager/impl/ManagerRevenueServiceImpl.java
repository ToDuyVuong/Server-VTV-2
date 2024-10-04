package hcmute.kltn.vtv.service.manager.impl;

import hcmute.kltn.vtv.model.data.vtv.response.*;
import hcmute.kltn.vtv.model.entity.user.Customer;
import hcmute.kltn.vtv.model.entity.user.Order;
import hcmute.kltn.vtv.model.entity.vendor.Product;
import hcmute.kltn.vtv.model.extra.OrderStatus;
import hcmute.kltn.vtv.model.extra.Status;
import hcmute.kltn.vtv.repository.shipping.TransportProviderRepository;
import hcmute.kltn.vtv.repository.user.CustomerRepository;
import hcmute.kltn.vtv.repository.user.OrderRepository;
import hcmute.kltn.vtv.repository.vendor.ProductRepository;
import hcmute.kltn.vtv.service.manager.IManagerRevenueService;
import hcmute.kltn.vtv.service.vendor.IRevenueService;
import hcmute.kltn.vtv.service.vtv.impl.DateServiceImpl;
import hcmute.kltn.vtv.util.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;


@Service
@RequiredArgsConstructor
public class ManagerRevenueServiceImpl implements IManagerRevenueService {


    private final CustomerRepository customerRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final IRevenueService revenueService;
    private final TransportProviderRepository transportProviderRepository;

    @Override
    public StatisticsCustomersResponse statisticsCustomersByDateAndStatus(Date startDate, Date endDate) {
        startDate = DateServiceImpl.formatStartOfDate(startDate);
        endDate = DateServiceImpl.formatEndOfDate(endDate);
        List<Customer> customers = customerRepository
                .findAllByStatusAndCreateAtBetween(Status.ACTIVE,
                        DateServiceImpl.convertDateToLocalDateTime(startDate),
                        DateServiceImpl.convertDateToLocalDateTime(endDate))
                .orElseThrow(() -> new NotFoundException("Không tìm thấy khách hàng nào."));
        String message = "Thống kê khách hàng từ ngày " + DateServiceImpl.formatStringDate(startDate)
                + " đến ngày " + DateServiceImpl.formatStringDate(endDate) + " thành công.";

        return StatisticsCustomersResponse.statisticsCustomersResponse(customers, startDate, endDate, message);
    }


    @Override
    public StatisticsOrdersResponse statisticsOrderByDateAndStatus(Date startDate, Date endDate, OrderStatus status) {
        startDate = DateServiceImpl.formatStartOfDate(startDate);
        endDate = DateServiceImpl.formatEndOfDate(endDate);
        List<Order> orders = orderRepository
                .findAllByStatusAndOrderDateBetween(status, startDate, endDate)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy đơn hàng nào."));
        String message = "Thống kê " + revenueService.messageByOrderStatus(status) + " từ ngày " + DateServiceImpl.formatStringDate(startDate)
                + " đến ngày " + DateServiceImpl.formatStringDate(endDate) + " thành công.";

        return StatisticsOrdersResponse.statisticsOrdersResponse(orders, startDate, endDate, message);
    }


    @Override
    public StatisticsProductsResponse getTopProductByLimitAndDate(int limit, Date startDate, Date endDate) {
        startDate = DateServiceImpl.formatStartOfDate(startDate);
        endDate = DateServiceImpl.formatEndOfDate(endDate);
        List<Product> productsBestSeller = productRepository.getBestProductsByLimitAndOrderStatusAndOrderDateBetween(
                        limit, OrderStatus.COMPLETED.toString(), startDate, endDate)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy danh sách sản phẩm bán chạy nào."));
        List<Order> orders = orderRepository
                .findAllByStatusAndOrderDateBetween(OrderStatus.COMPLETED, startDate, endDate)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy đơn hàng nào."));


        String message = "Danh sách sản phẩm bán chạy của cửa hàng từ ngày " + DateServiceImpl.formatStringDate(startDate)
                + " đến ngày " + DateServiceImpl.formatStringDate(endDate) + " thành công.";

        return StatisticsProductsResponse.statisticsProductsResponse(productsBestSeller, orders, startDate, endDate, message);
    }


    @Override
    public StatisticsTransportsResponse statisticsTransportsByDateAndShippingMethod(Date startDate, Date endDate, String shippingMethod) {
        if (!transportProviderRepository.existsByAndShortName(shippingMethod)) {
            throw new NotFoundException("Không tìm thấy phương thức vận chuyển theo yêu cầu: " + shippingMethod);
        }
        startDate = DateServiceImpl.formatStartOfDate(startDate);
        endDate = DateServiceImpl.formatEndOfDate(endDate);
        List<Order> orders = orderRepository
                .findAllByShippingMethodAndOrderDateBetween(shippingMethod, startDate, endDate)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy đơn hàng nào theo phương thức vận chuyển: " + shippingMethod));
        String message = "Thống kê vận chuyển từ ngày " + DateServiceImpl.formatStringDate(startDate)
                + " đến ngày " + DateServiceImpl.formatStringDate(endDate) + " thành công.";

        return StatisticsTransportsResponse.statisticsTransportsResponseByOrders(orders, startDate, endDate, message);
    }

    @Override
    public StatisticsFeeOrderResponse statisticsFeeOrderByDate(Date startDate, Date endDate) {
        List<Order> orders = orderRepository.findAllByStatusAndOrderDateBetween(OrderStatus.COMPLETED, startDate, endDate)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy đơn hàng nào."));
        String message = "Thống kê doanh thu từ ngày " + DateServiceImpl.formatStringDate(startDate) + " đến ngày " + DateServiceImpl.formatStringDate(endDate) + " thành công.";
        return StatisticsFeeOrderResponse.statisticsFeeOrderResponse(orders, startDate, endDate, message);

    }


}
