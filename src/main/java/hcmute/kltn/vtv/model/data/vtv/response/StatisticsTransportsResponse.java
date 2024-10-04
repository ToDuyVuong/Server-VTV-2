package hcmute.kltn.vtv.model.data.vtv.response;

import hcmute.kltn.vtv.model.dto.vtv.StatisticsTransportDTO;
import hcmute.kltn.vtv.model.entity.shipping.Transport;
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
public class StatisticsTransportsResponse  extends ResponseAbstract {

    private int count;
    private Long totalFee;
    private Long totalTransport;
    private Date dateStart;
    private Date dateEnd;
    private List<StatisticsTransportDTO> statisticsTransportDTOs;


    public static StatisticsTransportsResponse statisticsTransportsResponseByOrders(List<Order> orders, Date dateStart, Date dateEnd, String message) {
        StatisticsTransportsResponse response = new StatisticsTransportsResponse();
        response.setStatisticsTransportDTOs(StatisticsTransportDTO.convertStatisticsTransportDTOsByOrders(orders, dateStart, dateEnd));
        response.setCount(response.getStatisticsTransportDTOs().size());
        response.setTotalFee(response.getStatisticsTransportDTOs().stream().mapToLong(StatisticsTransportDTO::getTotalFeeShipping).sum());
        response.setTotalTransport(response.getStatisticsTransportDTOs().stream().mapToLong(StatisticsTransportDTO::getTotalTransport).sum());
        response.setDateStart(dateStart);
        response.setDateEnd(dateEnd);
        response.setMessage(message);
        response.setCode(200);
        response.setStatus("OK");

        return response;
    }




}
