package hcmute.kltn.vtv.model.data.manager.response;

import hcmute.kltn.vtv.model.dto.manager.ManagerProductDTO;
import hcmute.kltn.vtv.model.entity.manager.ManagerProduct;
import hcmute.kltn.vtv.model.extra.ResponseAbstract;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ManagerProductResponse extends ResponseAbstract {

    private ManagerProductDTO managerProductDTO;

    public static ManagerProductResponse managerProductResponse(ManagerProduct managerProduct, String message, String status) {
        ManagerProductResponse response = new ManagerProductResponse();
        response.setManagerProductDTO(ManagerProductDTO.convertEntityToDTO(managerProduct));
        response.setStatus(status);
        response.setMessage(message);
        response.setCode(200);

        return response;
    }


}
