package hcmute.kltn.vtv.model.data.manager.response;

import hcmute.kltn.vtv.model.dto.manager.ManagerDTO;
import hcmute.kltn.vtv.model.entity.manager.Manager;
import hcmute.kltn.vtv.model.extra.ResponseAbstract;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ManagerResponse extends ResponseAbstract {

    private ManagerDTO managerDTO;


    public static ManagerResponse managerResponse(Manager manager, String message, String status) {
        ManagerResponse managerResponse = new ManagerResponse();
        managerResponse.setManagerDTO(ManagerDTO.convertEntityToDTO(manager));
        managerResponse.setStatus(status);
        managerResponse.setCode(200);
        managerResponse.setMessage(message);
        return managerResponse;
    }
}
