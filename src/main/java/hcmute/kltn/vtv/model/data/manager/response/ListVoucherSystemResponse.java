package hcmute.kltn.vtv.model.data.manager.response;

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
public class ListVoucherSystemResponse extends ResponseAbstract {

    private int count;
    private List<VoucherDTO> voucherDTOs;



    public static ListVoucherSystemResponse listVoucherSystemResponse(List<Voucher> vouchers, String message) {
        ListVoucherSystemResponse response = new ListVoucherSystemResponse();
        response.setVoucherDTOs(VoucherDTO.convertEntitiesToDTOs(vouchers));
        response.setCode(200);
        response.setMessage(message);
        response.setStatus("OK");
        response.setCount(vouchers.size());

        return response;
    }
}
