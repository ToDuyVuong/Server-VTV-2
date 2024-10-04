package hcmute.kltn.vtv.authentication.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import hcmute.kltn.vtv.model.dto.user.CustomerDTO;
import hcmute.kltn.vtv.model.entity.user.Customer;
import hcmute.kltn.vtv.model.extra.ResponseAbstract;
import hcmute.kltn.vtv.model.extra.Status;
import lombok.*;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse extends ResponseAbstract {

    @JsonProperty("access_token")
    private String accessToken;
    @JsonProperty("refresh_token")
    private String refreshToken;

    private CustomerDTO customerDTO;

    public static LoginResponse loginResponse(Customer customer, String jwtToken, String refreshToken) {
        if (customer.getStatus().equals(Status.ACTIVE)) {
            LoginResponse loginResponse = new LoginResponse();
            loginResponse.setCustomerDTO(CustomerDTO.convertEntityToDTO(customer));
            loginResponse.setStatus("OK");
            loginResponse.setMessage("Đăng nhập thành công");
            loginResponse.setCode(200);
            loginResponse.setAccessToken(jwtToken);
            loginResponse.setRefreshToken(refreshToken);

            return loginResponse;
        } else {
            LoginResponse loginResponse = new LoginResponse();
            loginResponse.setStatus("ERROR");
            loginResponse.setMessage("Tài khoản " + LoginResponse.messageStatus(customer.getStatus()));
            loginResponse.setCode(400);

            return loginResponse;
        }
    }


    public static String messageStatus(Status status) {
        if (status.equals(Status.INACTIVE)) {
            return "không hoạt động.";
        } else if (status.equals(Status.LOCKED)) {
            return "bị khóa.";
        } else {
            return "không tồn tại.";
        }
    }

}
