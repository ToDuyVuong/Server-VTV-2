package hcmute.kltn.vtv.model.dto.vtv;

import hcmute.kltn.vtv.model.entity.user.Customer;
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
public class StatisticsCustomerDTO {

    private Long totalCustomer;
    private Date date;

    public static StatisticsCustomerDTO convertCustomersAndDateToDTO(Long totalCustomer, Date date) {
        StatisticsCustomerDTO statisticsCustomerDTO = new StatisticsCustomerDTO();
        statisticsCustomerDTO.setDate(date);
        statisticsCustomerDTO.setTotalCustomer(totalCustomer);

        return statisticsCustomerDTO;
    }


    public static List<StatisticsCustomerDTO> covertStatisticsCustomerDTOs(List<Customer> customers, Date startDate, Date endDate) {
        List<Date> datesBetween = DateServiceImpl.getDatesBetween(startDate, endDate);
        List<StatisticsCustomerDTO> statisticsCustomerDTOS = new ArrayList<>();
        for (Date date : datesBetween) {
            Long totalCustomer = customers.stream()
                    .filter(customer -> DateServiceImpl.isSameDate(date, customer.getCreateAt()))
                    .count();
            statisticsCustomerDTOS.add(StatisticsCustomerDTO.convertCustomersAndDateToDTO(totalCustomer, date));
        }
        statisticsCustomerDTOS.sort(Comparator.comparing(StatisticsCustomerDTO::getDate));

        return statisticsCustomerDTOS;
    }

}
