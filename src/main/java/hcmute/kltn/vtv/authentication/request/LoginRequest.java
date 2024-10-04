package hcmute.kltn.vtv.authentication.request;

import hcmute.kltn.vtv.util.exception.BadRequestException;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {

    private String username;

    private String password;

    private String fcmToken;

    public void validate() {
        if (username == null || username.isEmpty()) {
            throw new BadRequestException("Tài khoản không được để trống.");
        }

        if (password == null || password.isEmpty()) {
            throw new BadRequestException("Mật khẩu không được để trống.");
        }

//        if (fcmToken == null || fcmToken.isEmpty()) {
//            throw new BadRequestException("FCM token không được để trống.");
//        }

        trim();
    }

    public void trim() {
        this.username = this.username.trim();
        this.password = this.password.trim();
        this.fcmToken = this.fcmToken != null ? this.fcmToken.trim() : null;
    }
}
