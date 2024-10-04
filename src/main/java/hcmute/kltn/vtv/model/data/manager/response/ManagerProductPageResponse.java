package hcmute.kltn.vtv.model.data.manager.response;

import hcmute.kltn.vtv.model.dto.manager.ManagerProductDTO;
import hcmute.kltn.vtv.model.entity.manager.ManagerProduct;
import hcmute.kltn.vtv.model.extra.ResponseAbstract;
import lombok.*;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ManagerProductPageResponse extends ResponseAbstract {

    private int count;
    private int size;
    private int page;
    private int totalPage;
    private long totalManagerProduct;
    private List<ManagerProductDTO> managerProductDTOs;


    public static ManagerProductPageResponse managerProductPageResponse(Page<ManagerProduct> managerProductPage, String message, String status) {
        ManagerProductPageResponse response = new ManagerProductPageResponse();
        response.setCount(managerProductPage.getNumberOfElements());
        response.setTotalPage(managerProductPage.getTotalPages());
        response.setPage(managerProductPage.getNumber() + 1);
        response.setSize(managerProductPage.getSize());
        response.setManagerProductDTOs(ManagerProductDTO.convertEntitiesToDTOs(managerProductPage.getContent()));
        response.setTotalManagerProduct(managerProductPage.getTotalElements());
        response.setMessage(message);
        response.setStatus(status);
        response.setCode(200);

        return response;
    }
}
