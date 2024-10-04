package hcmute.kltn.vtv.authentication.request;

import hcmute.kltn.vtv.model.extra.EmailValidator;
import hcmute.kltn.vtv.util.exception.BadRequestException;
import lombok.*;

import java.util.Date;

@Builder
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {

    private String username;

    private String password;

    private String email;

    private boolean gender;

    private String fullName;

    private Date birthday;


    public void validate() {
        if (username == null || username.isEmpty()) {
            throw new BadRequestException("Tài khoản không được để trống.");
        }

        if (password == null || password.isEmpty()) {
            throw new BadRequestException("Mật khẩu không được để trống.");
        }

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
        this.password = this.password.trim();
        this.email = this.email.trim();
        this.fullName = this.fullName.trim();
    }

}
