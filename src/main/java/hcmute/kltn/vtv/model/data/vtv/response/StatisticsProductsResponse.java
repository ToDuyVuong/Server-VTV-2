package hcmute.kltn.vtv.model.data.vtv.response;

import hcmute.kltn.vtv.model.dto.vtv.StatisticsProductDTO;
import hcmute.kltn.vtv.model.entity.user.Order;
import hcmute.kltn.vtv.model.entity.vendor.Product;
import hcmute.kltn.vtv.model.extra.ResponseAbstract;
import hcmute.kltn.vtv.service.vtv.impl.DateServiceImpl;
import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class StatisticsProductsResponse extends ResponseAbstract {

    private int count;
    private int totalOrder;
    private Long totalMoney;
    private Long totalSold;
    private Date dateStart;
    private Date dateEnd;
    private List<StatisticsProductDTO> statisticsProductDTOs;


    public static StatisticsProductsResponse statisticsProductsResponse(List<Product> products, List<Order> orders, Date startDate, Date endDate, String message) {
        StatisticsProductsResponse response = new StatisticsProductsResponse();
        response.setStatisticsProductDTOs(StatisticsProductDTO.convertProductsAndOrdersToDTOs(products, orders));
        response.setCount(products.size());
        response.setTotalOrder(orders.size());
        response.setTotalMoney(response.getStatisticsProductDTOs().stream().mapToLong(StatisticsProductDTO::getTotalMoney).sum());
        response.setTotalSold(response.getStatisticsProductDTOs().stream().mapToLong(StatisticsProductDTO::getTotalSold).sum());
        response.setDateStart(DateServiceImpl.formatStartOfDate(startDate));
        response.setDateEnd(DateServiceImpl.formatStartOfDate(endDate));
        response.setMessage(message);
        response.setCode(200);
        response.setStatus("OK");

        return response;
    }



}
