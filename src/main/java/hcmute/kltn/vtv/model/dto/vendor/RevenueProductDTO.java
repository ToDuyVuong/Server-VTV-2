package hcmute.kltn.vtv.model.dto.vendor;

import hcmute.kltn.vtv.model.entity.user.Order;
import hcmute.kltn.vtv.model.entity.user.OrderItem;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class RevenueProductDTO {

    private int totalSold;
    private Long totalMoney;
    private int totalOrder;
    private Date date;

    public static RevenueProductDTO convertOrdersToRevenueProductDTO(List<Order> orders, Long productId, Date date) {
        RevenueProductDTO revenueProductDTO = new RevenueProductDTO();
        revenueProductDTO.setTotalOrder(orders.size());
        revenueProductDTO.setDate(date);
        calculateTotalSoldAndTotalMoney(orders, productId, revenueProductDTO);
        return revenueProductDTO;
    }

    private static void calculateTotalSoldAndTotalMoney(List<Order> orders, Long productId, RevenueProductDTO revenueProductDTO) {
        int totalSold = 0;
        long totalMoney = 0;
        for (Order order : orders) {
            for (OrderItem orderItem : order.getOrderItems()) {
                if (orderItem.getCart().getProductVariant().getProduct().getProductId().equals(productId)) {
                    totalSold += orderItem.getCart().getQuantity();
                    totalMoney += orderItem.getCart().getQuantity() * orderItem.getPrice();
                }
            }
        }
        revenueProductDTO.setTotalSold(totalSold);
        revenueProductDTO.setTotalMoney(totalMoney);
    }

    public static List<RevenueProductDTO> convertMapOrdersToRevenueProductDTOs(Map<Date, List<Order>> mapOrders, Long productId) {
        List<RevenueProductDTO> revenueProductDTOs = new ArrayList<>();
        for (Map.Entry<Date, List<Order>> entry : mapOrders.entrySet()) {
            revenueProductDTOs.add(convertOrdersToRevenueProductDTO(entry.getValue(), productId, entry.getKey()));
        }

        revenueProductDTOs.sort((o1, o2) -> o1.getDate().compareTo(o2.getDate()));

        return revenueProductDTOs;
    }
}