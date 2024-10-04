package hcmute.kltn.vtv.model.data.user.request;

import hcmute.kltn.vtv.util.exception.BadRequestException;
import lombok.*;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ForgotPasswordRequest {

    private String username;

    private String otp;

    private String newPassword;

    public void validate() {
        if (username == null || username.isEmpty()) {
            throw new BadRequestException("Tài khoản không được để trống.");
        }

        if (otp == null || otp.isEmpty()) {
            throw new BadRequestException("Mã OTP không được để trống.");
        }

        if (newPassword == null || newPassword.isEmpty()) {
            throw new BadRequestException("Mật khẩu mới không được để trống.");
        }

        trim();
    }

    public void trim() {
        this.username = this.username.trim();
        this.otp = this.otp.trim();
        this.newPassword = this.newPassword.trim();
    }
}
