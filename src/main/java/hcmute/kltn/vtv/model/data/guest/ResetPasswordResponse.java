package hcmute.kltn.vtv.model.data.guest;

import hcmute.kltn.vtv.model.extra.ResponseAbstract;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ResetPasswordResponse extends ResponseAbstract {

        private String username;

        private String email;

        private String password;

        private String otp;

        private long timeValid;
}
