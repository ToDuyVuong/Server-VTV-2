package hcmute.kltn.vtv.model.data.vtv.response;

import hcmute.kltn.vtv.model.dto.vtv.StatisticsOrderDTO;
import hcmute.kltn.vtv.model.entity.user.Order;
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
public class StatisticsOrdersResponse extends ResponseAbstract {

    private int count;
    private int totalOrder;
    private Long totalMoney;
    private Date dateStart;
    private Date dateEnd;
    private List<StatisticsOrderDTO> statisticsOrderDTOs;


    public static StatisticsOrdersResponse statisticsOrdersResponse(List<Order> orders, Date startDate, Date endDate, String message) {
        StatisticsOrdersResponse response = new StatisticsOrdersResponse();
        response.setStatisticsOrderDTOs(StatisticsOrderDTO.covertStatisticsOrderDTOs(orders, startDate, endDate));
        response.setCount(response.getStatisticsOrderDTOs().size());
        response.setTotalOrder(orders.size());
        response.setTotalMoney(totalMoney(orders));
        response.setDateStart(DateServiceImpl.formatStartOfDate(startDate));
        response.setDateEnd(DateServiceImpl.formatStartOfDate(endDate));
        response.setMessage(message);
        response.setCode(200);
        response.setStatus("OK");

        return response;
    }


    public static Long totalMoney(List<Order> orders) {
        long totalMoney = 0;
        for (Order order : orders) {
            totalMoney += (long) (order.getTotalPrice() + order.getDiscountShop() - order.getTotalPrice() * 0.04);
        }
        return totalMoney;
    }




}
