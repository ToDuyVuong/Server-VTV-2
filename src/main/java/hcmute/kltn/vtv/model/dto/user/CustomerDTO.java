package hcmute.kltn.vtv.model.dto.user;

import hcmute.kltn.vtv.model.entity.user.Customer;
import hcmute.kltn.vtv.model.extra.Role;
import hcmute.kltn.vtv.model.extra.Status;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

import java.text.Collator;
import java.util.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDTO {

    private Long customerId;

    private String username;

    private String email;

    private boolean gender;

    private String fullName;

    // private String phone;

    private Date birthday;

    private Status status;

    @Enumerated(EnumType.STRING)
    private Set<Role> roles;

    public static CustomerDTO convertEntityToDTO(Customer customer) {
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setCustomerId(customer.getCustomerId());
        customerDTO.setUsername(customer.getUsername());
        customerDTO.setEmail(customer.getEmail());
        customerDTO.setGender(customer.isGender());
        customerDTO.setFullName(customer.getFullName());
        customerDTO.setBirthday(customer.getBirthday());
        customerDTO.setRoles(customer.getRoles());
        customerDTO.setStatus(customer.getStatus());
        return customerDTO;
    }

    public static List<CustomerDTO> convertEntitiesToDTOs(List<Customer> customers) {
        List<CustomerDTO> customerDTOs = new ArrayList<>();
        for (Customer customer : customers) {
            customerDTOs.add(convertEntityToDTO(customer));
        }
        Collator collator = Collator.getInstance(new Locale("vi", "VN"));
        customerDTOs.sort((c1, c2) -> collator.compare(c1.getFullName(), c2.getFullName()));
        return customerDTOs;
    }

}
