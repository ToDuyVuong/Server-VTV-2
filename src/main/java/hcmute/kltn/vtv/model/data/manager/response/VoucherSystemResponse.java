package hcmute.kltn.vtv.model.data.manager.response;

import hcmute.kltn.vtv.model.dto.vtv.VoucherDTO;
import hcmute.kltn.vtv.model.entity.vendor.Voucher;
import hcmute.kltn.vtv.model.extra.ResponseAbstract;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class VoucherSystemResponse extends ResponseAbstract {


    private VoucherDTO voucherDTO;

    public static VoucherSystemResponse voucherSystemResponse(Voucher voucher, String message, String status) {
        VoucherSystemResponse response = new VoucherSystemResponse();
        response.setVoucherDTO(VoucherDTO.convertEntityToDTO(voucher));
        response.setCode(200);
        response.setMessage(message);
        response.setStatus(status);

        return response;
    }


}
