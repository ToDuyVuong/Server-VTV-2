package hcmute.kltn.vtv.model.data.manager.response;


import hcmute.kltn.vtv.model.dto.user.CustomerDTO;
import hcmute.kltn.vtv.model.entity.user.Customer;
import hcmute.kltn.vtv.model.extra.ResponseAbstract;
import lombok.*;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PageCustomerResponse  extends ResponseAbstract {

    private int count;
    private int page;
    private int size;
    private int totalPage;
    private long totalCustomer;
    private List<CustomerDTO> customerDTOs;


    public static PageCustomerResponse pageCustomerResponse(Page<Customer> customers, String message) {
        PageCustomerResponse response = new PageCustomerResponse();
        response.setCustomerDTOs(CustomerDTO.convertEntitiesToDTOs(customers.getContent()));
        response.setCount(customers.getNumberOfElements());
        response.setSize(customers.getSize());
        response.setPage(customers.getNumber() + 1);
        response.setTotalPage(customers.getTotalPages());
        response.setTotalCustomer(customers.getTotalElements());
        response.setMessage(message);
        response.setStatus("OK");
        response.setCode(200);


        return response;
    }

}
