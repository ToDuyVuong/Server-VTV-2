package hcmute.kltn.vtv.model.data.vendor.response;


import hcmute.kltn.vtv.model.dto.vendor.ProductDTO;
import hcmute.kltn.vtv.model.dto.vendor.RevenueProductDTO;
import hcmute.kltn.vtv.model.entity.user.Order;
import hcmute.kltn.vtv.model.entity.vendor.Product;
import hcmute.kltn.vtv.model.extra.ResponseAbstract;
import lombok.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class RevenueProductResponse extends ResponseAbstract {

    private int totalSold;
    private Long totalMoney;
    private int totalOrder;
    private ProductDTO productDTO;
    private List<RevenueProductDTO> revenueProductDTOs;

    public static RevenueProductResponse revenueProductResponse(Map<Date, List<Order>> mapOrders, Product product, String message) {
        RevenueProductResponse response = new RevenueProductResponse();
        response.setProductDTO(ProductDTO.convertEntityToDTO(product));
        response.setRevenueProductDTOs(RevenueProductDTO.convertMapOrdersToRevenueProductDTOs(mapOrders, product.getProductId()));
        response.setTotalOrder(response.getRevenueProductDTOs().stream().mapToInt(RevenueProductDTO::getTotalOrder).sum());
        response.setTotalSold(response.getRevenueProductDTOs().stream().mapToInt(RevenueProductDTO::getTotalSold).sum());
        response.setTotalMoney(response.getRevenueProductDTOs().stream().mapToLong(RevenueProductDTO::getTotalMoney).sum());
        response.setMessage(message);
        response.setCode(200);
        response.setStatus("OK");

        return response;
    }

}
