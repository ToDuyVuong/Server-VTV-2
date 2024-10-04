package hcmute.kltn.vtv.model.data.vtv.response;

import hcmute.kltn.vtv.model.dto.vtv.StatisticsFeeOrderDTO;
import hcmute.kltn.vtv.model.entity.user.Order;
import hcmute.kltn.vtv.model.extra.ResponseAbstract;
import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class StatisticsFeeOrderResponse extends ResponseAbstract {

    private int count;
    private Long paymentTotal;
    private Long shopReceiveTotal;
    private Long feeShippingTotal;
    private Long feeSystemTotal;
    private Long discountSystemTotal;
    private Long discountShopTotal;
    private List<StatisticsFeeOrderDTO> statisticsFeeOrderDTOs;

    public static StatisticsFeeOrderResponse statisticsFeeOrderResponse(List<Order> orders, Date startDate, Date endDate, String message) {
        StatisticsFeeOrderResponse response = new StatisticsFeeOrderResponse();
        response.setStatisticsFeeOrderDTOs(StatisticsFeeOrderDTO.convertStatisticsFeeOrderDTOs(orders, startDate, endDate));
        response.setCount(orders.size());
        response.setPaymentTotal(response.statisticsFeeOrderDTOs.stream().mapToLong(StatisticsFeeOrderDTO::getPaymentTotal).sum());
        response.setShopReceiveTotal(response.statisticsFeeOrderDTOs.stream().mapToLong(StatisticsFeeOrderDTO::getShopReceiveTotal).sum());
        response.setFeeShippingTotal(response.statisticsFeeOrderDTOs.stream().mapToLong(StatisticsFeeOrderDTO::getFeeShippingTotal).sum());
        response.setFeeSystemTotal(response.statisticsFeeOrderDTOs.stream().mapToLong(StatisticsFeeOrderDTO::getFeeSystemTotal).sum());
        response.setDiscountSystemTotal(response.statisticsFeeOrderDTOs.stream().mapToLong(StatisticsFeeOrderDTO::getDiscountSystemTotal).sum());
        response.setDiscountShopTotal(response.statisticsFeeOrderDTOs.stream().mapToLong(StatisticsFeeOrderDTO::getDiscountShopTotal).sum());
        response.setCode(200);
        response.setStatus("OK");
        response.setMessage(message);
        return response;

    }

}
