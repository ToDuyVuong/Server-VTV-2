package hcmute.kltn.vtv.model.dto.vtv;

import hcmute.kltn.vtv.model.entity.user.Order;
import hcmute.kltn.vtv.service.vtv.impl.DateServiceImpl;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Data
@ToString
@AllArgsConstructor
@RequiredArgsConstructor
public class StatisticsFeeOrderDTO {
    private int count;
    private Long paymentTotal;
    private Long shopReceiveTotal;
    private Long feeShippingTotal;
    private Long feeSystemTotal;
    private Long discountSystemTotal;
    private Long discountShopTotal;
    private Date date;

    public static StatisticsFeeOrderDTO convertFeeOrderAndDateToDTO(int count, Long paymentTotal, Long shopReceiveTotal,
                                                                    Long feeShippingTotal, Long feeSystemTotal,
                                                                    Long discountSystemTotal, Long discountShopTotal,
                                                                    Date date) {
        return new StatisticsFeeOrderDTO(count, paymentTotal, shopReceiveTotal, feeShippingTotal,
                feeSystemTotal, discountSystemTotal, discountShopTotal, date);
    }

    public static List<StatisticsFeeOrderDTO> convertStatisticsFeeOrderDTOs(List<Order> orders, Date startDate, Date endDate) {
        List<Date> datesBetween = DateServiceImpl.getDatesBetween(startDate, endDate);
        List<StatisticsFeeOrderDTO> statisticsFeeOrderDTOS = new ArrayList<>();

        for (Date date : datesBetween) {
            List<Order> ordersOnDate = orders.stream()
                    .filter(order -> DateServiceImpl.isSameDate(date, order.getCreateAt()))
                    .collect(Collectors.toList());

            Long totalPayment = ordersOnDate.stream()
                    .mapToLong(Order::getPaymentTotal)
                    .sum();

            Long totalPrice = ordersOnDate.stream()
                    .mapToLong(Order::getTotalPrice)
                    .sum();

            Long totalFeeSystem = (long) (totalPrice * 0.04);
            Long totalShopReceive = totalPrice - totalFeeSystem;

            Long totalFeeShipping = ordersOnDate.stream()
                    .mapToLong(Order::getShippingFee)
                    .sum();

            Long totalDiscountSystem = ordersOnDate.stream()
                    .mapToLong(Order::getDiscountSystem)
                    .sum();

            Long totalDiscountShop = ordersOnDate.stream()
                    .mapToLong(Order::getDiscountShop)
                    .sum();

            statisticsFeeOrderDTOS.add(StatisticsFeeOrderDTO.convertFeeOrderAndDateToDTO(
                    ordersOnDate.size(), totalPayment, totalShopReceive, totalFeeShipping,
                    totalFeeSystem, totalDiscountSystem, totalDiscountShop, date));
        }

        statisticsFeeOrderDTOS.sort(Comparator.comparing(StatisticsFeeOrderDTO::getDate));
        return statisticsFeeOrderDTOS;
    }
}
