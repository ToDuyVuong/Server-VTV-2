package hcmute.kltn.vtv.service.vendor.impl;

import hcmute.kltn.vtv.model.data.vendor.response.RevenueProductResponse;
import hcmute.kltn.vtv.model.data.vtv.response.StatisticsProductsResponse;
import hcmute.kltn.vtv.model.entity.vendor.Product;
import hcmute.kltn.vtv.model.extra.OrderStatus;
import hcmute.kltn.vtv.repository.vendor.ProductRepository;
import hcmute.kltn.vtv.service.vendor.IProductShopService;
import hcmute.kltn.vtv.service.vtv.impl.DateServiceImpl;
import hcmute.kltn.vtv.model.data.vtv.response.StatisticsOrdersResponse;
import hcmute.kltn.vtv.model.entity.user.Order;
import hcmute.kltn.vtv.model.entity.vendor.Shop;
import hcmute.kltn.vtv.repository.user.OrderRepository;
import hcmute.kltn.vtv.service.vendor.IRevenueService;
import hcmute.kltn.vtv.service.vendor.IShopService;
import hcmute.kltn.vtv.util.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class RevenueServiceImpl implements IRevenueService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final IShopService shopService;
    private final IProductShopService productShopService;


    @Override
    public StatisticsOrdersResponse statisticsOrderByDateAndStatus(Date startDate, Date endDate, OrderStatus status, String username) {
        Shop shop = shopService.getShopByUsername(username);
        startDate = DateServiceImpl.formatStartOfDate(startDate);
        endDate = DateServiceImpl.formatEndOfDate(endDate);
        List<Order> orders = orderRepository
                .findAllByShopShopIdAndStatusAndOrderDateBetween(shop.getShopId(), status, startDate, endDate)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy đơn hàng nào."));
        String message = "Thống kê " + messageByOrderStatus(status) + " từ ngày " + DateServiceImpl.formatStringDate(startDate)
                + " đến ngày " + DateServiceImpl.formatStringDate(endDate) + " thành công.";

        return StatisticsOrdersResponse.statisticsOrdersResponse(orders, startDate, endDate, message);
    }


    @Override
    public StatisticsProductsResponse getTopProductByLimitAndDate(int limit, Date startDate, Date endDate, String username) {
        Shop shop = shopService.getShopByUsername(username);
        startDate = DateServiceImpl.formatStartOfDate(startDate);
        endDate = DateServiceImpl.formatEndOfDate(endDate);
        List<Product> productsBestSeller = productRepository.getBestProductsByLimitAndShopIdAndOrderStatusAndOrderDateBetween(
                        limit, shop.getShopId(), OrderStatus.COMPLETED.toString(), startDate, endDate)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy danh sách sản phẩm bán chạy nào."));
        List<Order> orders = orderRepository
                .findAllByShopShopIdAndStatusAndOrderDateBetween(shop.getShopId(), OrderStatus.COMPLETED, startDate, endDate)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy đơn hàng nào."));


        String message = "Danh sách sản phẩm bán chạy của cửa hàng từ ngày " + DateServiceImpl.formatStringDate(startDate)
                + " đến ngày " + DateServiceImpl.formatStringDate(endDate) + " thành công.";

        return StatisticsProductsResponse.statisticsProductsResponse(productsBestSeller, orders, startDate, endDate, message);
    }


    @Override
    public RevenueProductResponse getRevenueProductResponseByProductIdAndDate(Long productId, Date startDate, Date endDate, String username) {
        productShopService.checkExistProductByProductIdAndUsername(productId, username);
        startDate = DateServiceImpl.formatStartOfDate(startDate);
        endDate = DateServiceImpl.formatEndOfDate(endDate);
        List<Order> orders = orderRepository
                .getRevenueProductResponseByProductIdAndDateAndOrderStatus(productId, startDate, endDate, OrderStatus.COMPLETED.toString())
                .orElseThrow(() -> new NotFoundException("Không tìm thấy đơn hàng nào."));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy sản phẩm nào."));
        Map<Date, List<Order>> ordersByDate = getOrdersByDate(orders, startDate, endDate);
        String message = "Thống kê doanh thu sản phẩm " + product.getName() + " từ ngày " + DateServiceImpl.formatStringDate(startDate)
                + " đến ngày " + DateServiceImpl.formatStringDate(endDate) + " thành công.";

        return RevenueProductResponse.revenueProductResponse(ordersByDate, product, message);
    }


    public static Map<Date, List<Order>> getOrdersByDate(List<Order> orders, Date startDate, Date endDate) {
        Map<Date, List<Order>> ordersByDate = new HashMap<>();
        List<Date> datesBetween = DateServiceImpl.getDatesBetween(startDate, endDate);

        for (Date date : datesBetween) {
            List<Order> ordersSameDate = new ArrayList<>();
            Date start = DateServiceImpl.formatStartOfDate(date);
            Date end = DateServiceImpl.formatEndOfDate(date);

            for (Order checkOrder : orders) {
                if (checkOrder.getOrderDate().after(start) && checkOrder.getOrderDate().before(end)) {
                    ordersSameDate.add(checkOrder);
                }
            }
            ordersByDate.put(start, ordersSameDate);
        }

        for (List<Order> ordersOnDate : ordersByDate.values()) {
            ordersOnDate.sort(Comparator.comparing(Order::getOrderDate));
        }

        return ordersByDate;
    }


    @Override
    public String messageByOrderStatus(OrderStatus status) {
        return switch (status) {
            case COMPLETED -> "doanh thu";
            case DELIVERED -> "đơn hàng đã giao";
            case SHIPPING -> "đơn hàng đang giao";
            case CANCEL -> "đơn hàng đã hủy";
            default -> "đơn hàng";
        };
    }


}
