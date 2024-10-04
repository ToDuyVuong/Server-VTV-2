package hcmute.kltn.vtv.model.data.guest;

import hcmute.kltn.vtv.model.dto.vtv.VoucherDTO;
import hcmute.kltn.vtv.model.entity.vendor.Voucher;
import hcmute.kltn.vtv.model.extra.ResponseAbstract;
import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ListVoucherResponse extends ResponseAbstract {
    private int count;
    private List<VoucherDTO> voucherDTOs;


    public static ListVoucherResponse listVoucherResponse(List<Voucher> vouchers, String message) {
        ListVoucherResponse response = new ListVoucherResponse();
        response.setVoucherDTOs(VoucherDTO.convertEntitiesToDTOs(vouchers));
        response.setMessage(message);
        response.setStatus("OK");
        response.setCount(vouchers.size());
        response.setCode(200);

        return response;
    }
}
