package hcmute.kltn.vtv.model.data.vendor.response;

import hcmute.kltn.vtv.model.dto.vtv.VoucherDTO;
import hcmute.kltn.vtv.model.entity.vendor.Voucher;
import hcmute.kltn.vtv.model.extra.ResponseAbstract;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class VoucherShopResponse extends ResponseAbstract {

    private Long shopId;
    private String shopName;

    private VoucherDTO voucherDTO;


    public static VoucherShopResponse voucherShopResponse(Voucher voucher, String message, String status) {
        VoucherShopResponse response = new VoucherShopResponse();
        response.setVoucherDTO(VoucherDTO.convertEntityToDTO(voucher));
        response.setCode(200);
        response.setMessage(message);
        response.setStatus(status);
        response.setShopName(voucher.getShop().getName());
        response.setShopId(voucher.getShop().getShopId());

        return response;
    }

}
