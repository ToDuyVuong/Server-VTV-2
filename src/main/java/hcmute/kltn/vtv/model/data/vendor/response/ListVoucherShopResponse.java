package hcmute.kltn.vtv.model.data.vendor.response;

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
public class ListVoucherShopResponse extends ResponseAbstract {
    private Long shopId;
    private String shopName;
    private int count;
    private List<VoucherDTO> voucherDTOs;

    public static ListVoucherShopResponse listVoucherShopResponse(List<Voucher> vouchers, String message, Long shopId,
                                                            String shopName) {
        ListVoucherShopResponse response = new ListVoucherShopResponse();
        response.setVoucherDTOs(VoucherDTO.convertEntitiesToDTOs(vouchers));
        response.setCode(200);
        response.setMessage(message);
        response.setStatus("OK");
        response.setCount(vouchers.size());
        response.setShopId(shopId);
        response.setShopName(shopName);

        return response;
    }
}
