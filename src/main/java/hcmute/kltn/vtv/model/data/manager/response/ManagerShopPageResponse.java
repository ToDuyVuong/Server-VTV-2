package hcmute.kltn.vtv.model.data.manager.response;

import hcmute.kltn.vtv.model.dto.manager.ManagerShopDTO;
import hcmute.kltn.vtv.model.entity.manager.ManagerShop;
import hcmute.kltn.vtv.model.extra.ResponseAbstract;
import lombok.*;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ManagerShopPageResponse extends ResponseAbstract {

    private int count;
    private int size;
    private int page;
    private int totalPage;
    private long totalManagerShop;
    private List<ManagerShopDTO> managerShopDTOs;


    public static ManagerShopPageResponse managerShopPageResponse(Page<ManagerShop> managerShopPage, String message) {
        ManagerShopPageResponse response = new ManagerShopPageResponse();
        response.setManagerShopDTOs(ManagerShopDTO.convertEntitiesToDTOs(managerShopPage.getContent()));
        response.setSize(managerShopPage.getSize());
        response.setPage(managerShopPage.getNumber() + 1);
        response.setTotalPage(managerShopPage.getTotalPages());
        response.setCount(managerShopPage.getNumberOfElements());
        response.setTotalManagerShop(managerShopPage.getTotalElements());
        response.setMessage(message);
        response.setCode(200);
        response.setStatus("OK");

        return response;
    }
}
