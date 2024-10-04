package hcmute.kltn.vtv.model.data.manager.response;

import hcmute.kltn.vtv.model.dto.user.CustomerDTO;
import hcmute.kltn.vtv.model.extra.ResponseAbstract;
import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ManagerCustomerPageResponse extends ResponseAbstract {

    private int count;
    private int size;
    private int page;
    private int totalPage;
    private List<CustomerDTO> customerDTOs;
}
