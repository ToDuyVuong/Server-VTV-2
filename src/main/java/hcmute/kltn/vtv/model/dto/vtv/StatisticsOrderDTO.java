package hcmute.kltn.vtv.model.dto.vtv;

import hcmute.kltn.vtv.model.entity.user.Order;
import hcmute.kltn.vtv.service.vendor.impl.RevenueServiceImpl;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.*;

@Data
@ToString
@AllArgsConstructor
@RequiredArgsConstructor
public class StatisticsOrderDTO {

    private Long totalMoney;
    private int totalOrder;
    private int totalProduct;
    private Date date;

    public static StatisticsOrderDTO convertOrdersAndDateToDTO(List<Order> orders, Date date) {
        StatisticsOrderDTO statisticsOrderDTO = new StatisticsOrderDTO();
        statisticsOrderDTO.setDate(date);
        statisticsOrderDTO.setTotalMoney(totalMoney(orders));
        statisticsOrderDTO.setTotalOrder(orders.size());
        statisticsOrderDTO.setTotalProduct(orders.stream().mapToInt(Order::getCount).sum());

        return statisticsOrderDTO;
    }




    public static List<StatisticsOrderDTO> covertStatisticsOrderDTOs(List<Order> orders, Date startDate, Date endDate) {
        List<StatisticsOrderDTO> statisticsOrderDTOS = new ArrayList<>();
        Map<Date, List<Order>> ordersByDate = RevenueServiceImpl.getOrdersByDate(orders, startDate, endDate);
        for (Map.Entry<Date, List<Order>> entry : ordersByDate.entrySet()) {
            Date date = entry.getKey();
            List<Order> ordersOnDate = entry.getValue();
            statisticsOrderDTOS.add(StatisticsOrderDTO.convertOrdersAndDateToDTO(ordersOnDate, date));
        }
        statisticsOrderDTOS.sort(Comparator.comparing(StatisticsOrderDTO::getDate));

        return statisticsOrderDTOS;
    }


    public static Long totalMoney(List<Order> orders) {
        return orders.stream().mapToLong(order ->
                (long) (order.getTotalPrice() + order.getDiscountShop() - order.getTotalPrice() * 0.04)).sum();
    }



}
