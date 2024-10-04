package hcmute.kltn.vtv.model.data.manager.response;

import hcmute.kltn.vtv.model.dto.manager.ManagerDTO;
import hcmute.kltn.vtv.model.entity.manager.Manager;
import hcmute.kltn.vtv.model.extra.ResponseAbstract;
import lombok.*;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ManagerPageResponse extends ResponseAbstract {

        private int count;
        private int size;
        private int page;
        private int totalPage;
        private long totalManager;
        private List<ManagerDTO> managerDTOs;

        public static ManagerPageResponse managerPageResponse(Page<Manager> managerPage, String message) {
            ManagerPageResponse managerPageResponse = new ManagerPageResponse();
            managerPageResponse.setCount(managerPage.getContent().size());
            managerPageResponse.setSize(managerPage.getSize());
            managerPageResponse.setPage(managerPage.getNumber());
            managerPageResponse.setTotalPage(managerPage.getTotalPages());
            managerPageResponse.setTotalManager(managerPage.getTotalElements());
            managerPageResponse.setManagerDTOs(ManagerDTO.convertEntitiesToDTOs(managerPage.getContent()));
            managerPageResponse.setStatus("Success");
            managerPageResponse.setMessage(message);
            managerPageResponse.setCode(200);

            return managerPageResponse;
        }

}
