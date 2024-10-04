package hcmute.kltn.vtv.controller.vendor;

import hcmute.kltn.vtv.util.exception.BadRequestException;
import hcmute.kltn.vtv.model.data.vendor.request.VoucherShopRequest;
import hcmute.kltn.vtv.model.data.vendor.response.ListVoucherShopResponse;
import hcmute.kltn.vtv.model.data.vendor.response.VoucherShopResponse;
import hcmute.kltn.vtv.model.extra.Status;
import hcmute.kltn.vtv.model.extra.VoucherType;
import hcmute.kltn.vtv.service.vendor.IVoucherShopService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/vendor/shop/voucher")
@RequiredArgsConstructor
public class VoucherShopController {

    private final IVoucherShopService voucherShopService;

    @PostMapping("/add")
    public ResponseEntity<VoucherShopResponse> addNewVoucher(@RequestBody VoucherShopRequest request,
                                                             HttpServletRequest httpServletRequest) {
        String username = (String) httpServletRequest.getAttribute("username");

        request.setUsername(username);
        request.validate();

        return ResponseEntity.ok(voucherShopService.addNewVoucher(request, username));
    }

    @GetMapping("/detail/{voucherId}")
    public ResponseEntity<VoucherShopResponse> getVoucherByVoucherId(@PathVariable Long voucherId,
                                                                     HttpServletRequest httpServletRequest) {
        if (voucherId == null) {
            throw new BadRequestException("Mã giảm giá không được để trống!");
        }
        String username = (String) httpServletRequest.getAttribute("username");

        return ResponseEntity.ok(voucherShopService.getVoucherByVoucherId(voucherId, username));
    }

    @GetMapping("/get-all-shop")
    public ResponseEntity<ListVoucherShopResponse> getAllVoucher(HttpServletRequest httpServletRequest) {
        String username = (String) httpServletRequest.getAttribute("username");

        return ResponseEntity.ok(voucherShopService.getListVoucherByUsername(username));
    }

    @GetMapping("/get-all-shop-status")
    public ResponseEntity<ListVoucherShopResponse> getAllVoucherByStatus(@RequestParam Status status,
                                                                         HttpServletRequest httpServletRequest) {
        String username = (String) httpServletRequest.getAttribute("username");

        return ResponseEntity.ok(voucherShopService.getListVoucherByUsernameAndStatus(username, status));
    }

    @GetMapping("/get-all-shop-type")
    public ResponseEntity<ListVoucherShopResponse> getAllVoucherByType(@RequestParam VoucherType type,
                                                                       HttpServletRequest httpServletRequest) {
        String username = (String) httpServletRequest.getAttribute("username");

        return ResponseEntity.ok(voucherShopService.getListVoucherByUsernameAndVoucherType(username, type));
    }

    @PutMapping("/update/{voucherId}")
    public ResponseEntity<VoucherShopResponse> updateVoucher(@PathVariable Long voucherId,
                                                             @RequestBody VoucherShopRequest request,
                                                             HttpServletRequest httpServletRequest) {
        String username = (String) httpServletRequest.getAttribute("username");

        request.setUsername(username);
        request.validate();

        return ResponseEntity.ok(voucherShopService.updateVoucherShopByVoucherShopRequest(voucherId, request, username));
    }

    @PatchMapping("/update-status/{voucherId}")
    public ResponseEntity<VoucherShopResponse> updateStatusVoucher(@PathVariable Long voucherId,
                                                                   @RequestParam Status status,
                                                                   HttpServletRequest httpServletRequest) {
        if (voucherId == null) {
            throw new BadRequestException("Mã giảm giá không được để trống!");
        }

        String username = (String) httpServletRequest.getAttribute("username");

        return ResponseEntity.ok(voucherShopService.updateStatusVoucher(voucherId, status, username));
    }
}
