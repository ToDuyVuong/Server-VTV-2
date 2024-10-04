package hcmute.kltn.vtv.model.data.shipping.request;

import hcmute.kltn.vtv.model.extra.EmailValidator;
import hcmute.kltn.vtv.util.exception.BadRequestException;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;


@Data
@ToString
@RequiredArgsConstructor
public class UpdateTransportProviderRequest {


    private String fullName;

    private String shortName;

    private String email;

    private String phone;



    public void validate() {


        if (this.fullName == null || this.fullName.isEmpty()) {
            throw new BadRequestException("Tên đầy đủ không được để trống!");
        }

        if (this.shortName == null || this.shortName.isEmpty()) {
            throw new BadRequestException("Tên viết tắt không được để trống!");
        }

        if (email == null || email.isEmpty()) {
            throw new BadRequestException("Email không được để trống.");
        } else if (!EmailValidator.isValidEmail(email)) {
            throw new BadRequestException("Email không hợp lệ.");
        }

        if (this.phone == null || this.phone.isEmpty()) {
            throw new BadRequestException("Số điện thoại không được để trống!");
        }

        if (!Pattern.matches("[0-9]+", phone)) {
            throw new BadRequestException("Số điện thoại chỉ được chứa ký tự số.");
        }

        if (phone.length() < 9 || phone.length() > 11) {
            throw new BadRequestException("Số điện thoại không hợp lệ.");
        }

        trim();
    }

    public void trim() {
        this.fullName = this.fullName.trim();
        this.shortName = this.shortName.trim();
        this.email = this.email.trim();
    }




}
