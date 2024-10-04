package hcmute.kltn.vtv.model.data.vtv.response;

import hcmute.kltn.vtv.model.dto.vtv.StatisticsCustomerDTO;
import hcmute.kltn.vtv.model.dto.vtv.StatisticsProductDTO;
import hcmute.kltn.vtv.model.entity.user.Customer;
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
public class StatisticsCustomersResponse extends ResponseAbstract {

    private int count;
    private int totalCustomer;
    private Date dateStart;
    private Date dateEnd;
    private List<StatisticsCustomerDTO> statisticsCustomerDTOs;

    public static StatisticsCustomersResponse statisticsCustomersResponse(List<Customer> customers, Date startDate, Date endDate, String message) {
        StatisticsCustomersResponse response = new StatisticsCustomersResponse();
        response.setStatisticsCustomerDTOs(StatisticsCustomerDTO.covertStatisticsCustomerDTOs(customers, startDate, endDate));
        response.setCount(response.statisticsCustomerDTOs.size());
        response.setTotalCustomer(customers.size());
        response.setDateStart(startDate);
        response.setDateEnd(endDate);
        response.setMessage(message);
        response.setCode(200);
        response.setStatus("OK");

        return response;
    }

}
