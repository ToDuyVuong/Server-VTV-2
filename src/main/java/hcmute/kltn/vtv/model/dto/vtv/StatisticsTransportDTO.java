package hcmute.kltn.vtv.model.dto.vtv;

import hcmute.kltn.vtv.model.entity.shipping.Transport;
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

@Data
@ToString
@AllArgsConstructor
@RequiredArgsConstructor
public class StatisticsTransportDTO {
    private int totalTransport;
    private Long totalFeeShipping;
    private Date date;

    public static StatisticsTransportDTO convertTransportsAndDateToDTO(int totalTransport, Long totalFeeShipping, Date date) {
        return new StatisticsTransportDTO(totalTransport, totalFeeShipping, date);
    }

    public static List<StatisticsTransportDTO> convertStatisticsTransportDTOsByOrders(List<Order> orders, Date startDate, Date endDate) {
        List<Date> datesBetween = DateServiceImpl.getDatesBetween(startDate, endDate);
        List<StatisticsTransportDTO> statisticsTransportDTOS = new ArrayList<>();

        for (Date date : datesBetween) {
            List<Order> ordersOnDate = orders.stream()
                    .filter(order -> DateServiceImpl.isSameDate(date, order.getUpdateAt()))
                    .toList();

            Long totalTransport = ordersOnDate.stream()
                    .mapToLong(Order::getShippingFee)
                    .sum();

            statisticsTransportDTOS.add(StatisticsTransportDTO.convertTransportsAndDateToDTO(ordersOnDate.size(), totalTransport, date));
        }

        statisticsTransportDTOS.sort(Comparator.comparing(StatisticsTransportDTO::getDate));
        return statisticsTransportDTOS;
    }





}
