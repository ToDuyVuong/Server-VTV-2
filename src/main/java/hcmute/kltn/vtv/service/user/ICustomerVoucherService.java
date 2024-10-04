package hcmute.kltn.vtv.service.user;

import hcmute.kltn.vtv.model.data.user.response.CustomerVoucherResponse;
import hcmute.kltn.vtv.model.data.user.response.ListCustomerVoucherResponse;

public interface ICustomerVoucherService {
    CustomerVoucherResponse saveVoucher(Long voucherId, String username);

    ListCustomerVoucherResponse listCustomerVoucherByUsername(String username);

    CustomerVoucherResponse deleteCustomerVoucher(Long voucherId, String username);
}
