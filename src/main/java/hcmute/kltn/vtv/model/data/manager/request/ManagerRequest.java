package hcmute.kltn.vtv.model.data.manager.request;


import hcmute.kltn.vtv.model.extra.Role;
import hcmute.kltn.vtv.util.exception.BadRequestException;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Data
@ToString
@RequiredArgsConstructor
public class ManagerRequest {

    private String usernameCustomer;
    private Role role;

    public void validate() {
        if (this.usernameCustomer == null || this.usernameCustomer.trim().isEmpty()) {
            throw new BadRequestException("Tên tài khoản không được để trống!");
        }

        if (this.role == null) {
            throw new BadRequestException("Vai trò không được để trống!");
        }

    }

}
