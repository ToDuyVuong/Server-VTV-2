package hcmute.kltn.vtv.service.guest;

import hcmute.kltn.vtv.model.data.guest.ListVoucherResponse;
import hcmute.kltn.vtv.model.data.guest.VoucherResponse;
import hcmute.kltn.vtv.model.entity.vendor.Voucher;
import hcmute.kltn.vtv.model.extra.VoucherType;

import java.util.List;

public interface IVoucherService {
    VoucherResponse getVoucherByVoucherId(Long voucherId);

    ListVoucherResponse listVoucherByShopId(Long shopId);

    ListVoucherResponse listVoucherByType(VoucherType type);

    ListVoucherResponse listVoucherSystem();

    ListVoucherResponse allVoucher();


    void checkQuantityVoucher(String voucherCode, Integer quantity);

    Voucher getVoucherById(Long voucherId);
}
