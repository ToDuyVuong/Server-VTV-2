package hcmute.kltn.vtv.service.manager;

import hcmute.kltn.vtv.model.data.manager.request.VoucherSystemRequest;
import hcmute.kltn.vtv.model.data.manager.response.ListVoucherSystemResponse;
import hcmute.kltn.vtv.model.data.manager.response.VoucherSystemResponse;
import hcmute.kltn.vtv.model.entity.vendor.Voucher;
import hcmute.kltn.vtv.model.extra.Status;
import hcmute.kltn.vtv.model.extra.VoucherType;
import org.springframework.transaction.annotation.Transactional;

public interface IManagerVoucherSystemService {
    @Transactional
    VoucherSystemResponse addNewVoucherSystem(String username, VoucherSystemRequest request);

    VoucherSystemResponse getVoucherSystemByVoucherId(Long voucherId);

    ListVoucherSystemResponse getListVoucherSystem(String username);

    ListVoucherSystemResponse getListVoucherSystemByUsername(String username);

    ListVoucherSystemResponse getListVoucherSystemByStatus(String username, Status status);

    ListVoucherSystemResponse getListVoucherSystemByType(String username, VoucherType type);

    @Transactional
    VoucherSystemResponse updateVoucherSystem(Long voucherId, VoucherSystemRequest request, String username);

    @Transactional
    VoucherSystemResponse updateStatusVoucherSystem(Long voucherId, Status status, String username);

    Voucher checkVoucherSystem(Long voucherId);
}
