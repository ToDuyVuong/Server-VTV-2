package hcmute.kltn.vtv.model.data.manager.request;

import hcmute.kltn.vtv.authentication.request.RegisterRequest;
import hcmute.kltn.vtv.model.dto.location.ProvinceDTO;
import hcmute.kltn.vtv.model.extra.EmailValidator;
import hcmute.kltn.vtv.model.extra.Status;
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
public class TransportProviderRegisterRequest {

    private String fullName;

    private String shortName;

    private String email;

    private String phone;

    private List<String> provincesCode;

    private String usernameAdded;

    private FeeShippingRequest feeShippingRequest;

    private RegisterRequest registerRequest;

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

        if (this.usernameAdded == null || this.usernameAdded.isEmpty()) {
            throw new BadRequestException("Tên đăng nhập không được để trống!");
        }

        if (this.provincesCode == null || this.provincesCode.isEmpty()) {
            throw new BadRequestException("Tỉnh thành không được để trống!");
        }

        if (hasDuplicates(this.provincesCode)) {
            throw new BadRequestException("Mã tỉnh thành không được trùng lặp.");
        }

        feeShippingRequest.validate();
        registerRequest.validate();

        trim();
    }

    public void trim() {
        this.fullName = this.fullName.trim();
        this.shortName = this.shortName.trim();
        this.usernameAdded = this.usernameAdded.trim();
    }

    public static boolean hasDuplicates(List<String> list) {
        Set<String> uniqueSet = new HashSet<>();

        for (String code : list) {
            if (!uniqueSet.add(code)) {
                return true;
            }
        }

        return false;
    }

}
