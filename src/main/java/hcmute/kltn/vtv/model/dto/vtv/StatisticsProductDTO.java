package hcmute.kltn.vtv.model.dto.vtv;

import hcmute.kltn.vtv.model.dto.vendor.ProductDTO;
import hcmute.kltn.vtv.model.entity.user.Order;
import hcmute.kltn.vtv.model.entity.user.OrderItem;
import hcmute.kltn.vtv.model.entity.vendor.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class StatisticsProductDTO {

    private Long totalSold;
    private Long totalMoney;
    private ProductDTO productDTO;

    public static List<StatisticsProductDTO> convertProductsAndOrdersToDTOs(List<Product> products, List<Order> orders) {
        List<StatisticsProductDTO> statisticsProductDTOS = new ArrayList<>();
        for (Product product : products) {
            statisticsProductDTOS.add(convertProductAndOrdersToDTO(product, orders));
        }
        statisticsProductDTOS.sort((o1, o2) -> o2.getTotalSold().compareTo(o1.getTotalSold()));
        return statisticsProductDTOS;
    }

    public static StatisticsProductDTO convertProductAndOrdersToDTO(Product product, List<Order> orders) {
        StatisticsProductDTO statisticsProductDTO = new StatisticsProductDTO();
        statisticsProductDTO.setProductDTO(ProductDTO.convertEntityToDTO(product));
        return calculateTotalSoldAndTotalMoney(orders, product.getProductId(), statisticsProductDTO);
    }

    public static StatisticsProductDTO calculateTotalSoldAndTotalMoney(List<Order> orders, Long productId, StatisticsProductDTO statisticsProductDTO) {
        long totalSold = 0;
        long totalMoney = 0;
        for (Order order : orders) {
            for (OrderItem orderItem : order.getOrderItems()) {
                if (orderItem.getCart().getProductVariant().getProduct().getProductId().equals(productId)) {
                    totalSold += orderItem.getCart().getQuantity();
                    totalMoney += orderItem.getCart().getQuantity() * orderItem.getPrice();
                }
            }
        }
        statisticsProductDTO.setTotalSold(totalSold);
        statisticsProductDTO.setTotalMoney(totalMoney);
        return statisticsProductDTO;
    }
}