package hcmute.kltn.vtv.model.dto.manager;

import hcmute.kltn.vtv.model.entity.manager.Manager;
import hcmute.kltn.vtv.model.entity.user.Customer;
import hcmute.kltn.vtv.model.extra.Role;
import hcmute.kltn.vtv.model.extra.Status;
import hcmute.kltn.vtv.util.exception.InternalServerErrorException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ManagerDTO {

    private Long managerId;
    private Status status;
    private Long customerId;
    private String username;
    private Set<Role> roles;


    public static ManagerDTO convertEntityToDTO(Manager manager) {
        ManagerDTO managerDTO = new ManagerDTO();
        managerDTO.setManagerId(manager.getManagerId());
        managerDTO.setStatus(manager.getStatus());
        managerDTO.setCustomerId(manager.getManager().getCustomerId());
        managerDTO.setUsername(manager.getManager().getUsername());
        managerDTO.setRoles(manager.getManager().getRoles());
        return managerDTO;
    }

    public static List<ManagerDTO> convertEntitiesToDTOs(List<Manager> managers) {
        List<ManagerDTO> managerDTOs = new ArrayList<>();
        if (managers != null) {
//            managers.sort(Comparator.comparing(Manager::getManagerId));
            for (Manager manager : managers) {
                managerDTOs.add(convertEntityToDTO(manager));
            }
        }

        return managerDTOs;
    }
}
