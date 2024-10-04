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
public class UpdateTransportProviderWithProvincesRequest {

    private Long transportProviderId;

    private List<String> provincesCode;

    private String usernameAdded;

    public void validate() {
        if (this.transportProviderId == null) {
            throw new BadRequestException("Id nhà vận chuyển không được để trống!");
        }

        if (this.usernameAdded == null || this.usernameAdded.isEmpty()) {
            throw new BadRequestException("Tên đăng nhập không được để trống!");
        }

//        if (this.provincesCode == null || this.provincesCode.isEmpty()) {
//            throw new BadRequestException("Tỉnh thành không được để trống!");
//        }

        if (hasDuplicates(this.provincesCode)) {
            throw new BadRequestException("Mã tỉnh thành không được trùng lặp.");
        }

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
