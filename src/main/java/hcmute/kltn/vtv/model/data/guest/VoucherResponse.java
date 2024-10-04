package hcmute.kltn.vtv.model.data.guest;

import hcmute.kltn.vtv.model.dto.vtv.VoucherDTO;
import hcmute.kltn.vtv.model.entity.vendor.Voucher;
import hcmute.kltn.vtv.model.extra.ResponseAbstract;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class VoucherResponse extends ResponseAbstract {
    private VoucherDTO voucherDTO;


    public static VoucherResponse voucherResponse(Voucher voucher, String message, String status) {
        VoucherResponse response = new VoucherResponse();
        response.setVoucherDTO(VoucherDTO.convertEntityToDTO(voucher));
        response.setMessage(message);
        response.setStatus(status);
        response.setCode(200);

        return response;
    }
}
