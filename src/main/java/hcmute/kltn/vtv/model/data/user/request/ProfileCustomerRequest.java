package hcmute.kltn.vtv.model.data.user.request;

import hcmute.kltn.vtv.model.extra.EmailValidator;
import hcmute.kltn.vtv.util.exception.BadRequestException;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfileCustomerRequest {

    private String username;

    private String email;

    private String fullName;

    // @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    private Date birthday;

    private boolean gender;

    public void validate() {

        if (email == null || email.isEmpty()) {
            throw new BadRequestException("Email không được để trống.");
        } else if (!EmailValidator.isValidEmail(email)) {
            throw new BadRequestException("Email không hợp lệ.");
        }

        if (fullName == null || fullName.isEmpty()) {
            throw new BadRequestException("Tên đầy đủ không được để trống.");
        }

        if (birthday == null) {
            throw new BadRequestException("Ngày sinh không được để trống.");
        }

        trim();

    }

    public void trim() {
        this.username = this.username.trim();
        this.email = this.email.trim();
        this.fullName = this.fullName.trim();
    }
}
