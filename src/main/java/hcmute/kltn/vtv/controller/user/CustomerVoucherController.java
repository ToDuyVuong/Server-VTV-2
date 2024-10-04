package hcmute.kltn.vtv.controller.user;

import hcmute.kltn.vtv.model.data.user.response.CustomerVoucherResponse;
import hcmute.kltn.vtv.model.data.user.response.ListCustomerVoucherResponse;
import hcmute.kltn.vtv.service.user.ICustomerVoucherService;
import hcmute.kltn.vtv.util.exception.NotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customer/voucher")
@RequiredArgsConstructor
public class CustomerVoucherController {

    private final ICustomerVoucherService customerVoucherService;

    @PostMapping("/save/{voucherId}")
    public ResponseEntity<CustomerVoucherResponse> saveVoucher(@PathVariable Long voucherId,
            HttpServletRequest request) {
        String username = (String) request.getAttribute("username");

        return ResponseEntity.ok(customerVoucherService.saveVoucher(voucherId, username));
    }

    @GetMapping("/list")
    public ResponseEntity<ListCustomerVoucherResponse> listCustomerVoucher(HttpServletRequest request) {
        String username = (String) request.getAttribute("username");
        return ResponseEntity.ok(customerVoucherService.listCustomerVoucherByUsername(username));
    }

    @DeleteMapping("/delete/{voucherId}")
    public ResponseEntity<CustomerVoucherResponse> deleteCustomerVoucher(@PathVariable("voucherId") Long voucherId,
            HttpServletRequest request) {
        if (voucherId == null) {
            throw new NotFoundException("Mã giảm giá không được để trống.");
        }
        String username = (String) request.getAttribute("username");
        return ResponseEntity.ok(customerVoucherService.deleteCustomerVoucher(voucherId, username));
    }

}
