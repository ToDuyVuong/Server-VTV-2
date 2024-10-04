package hcmute.kltn.vtv.controller.guest;

import hcmute.kltn.vtv.model.data.guest.ListVoucherResponse;
import hcmute.kltn.vtv.model.data.guest.VoucherResponse;
import hcmute.kltn.vtv.model.extra.VoucherType;
import hcmute.kltn.vtv.service.guest.IVoucherService;
import hcmute.kltn.vtv.util.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/voucher")
@RequiredArgsConstructor
public class VoucherController {

    private final IVoucherService voucherService;

    @GetMapping("/detail/{voucherId}")
    public ResponseEntity<VoucherResponse> getVoucherByVoucherId(@PathVariable Long voucherId) {

        return ResponseEntity.ok(voucherService.getVoucherByVoucherId(voucherId));
    }

    @GetMapping("/list-on-shop/{shopId}")
    public ResponseEntity<ListVoucherResponse> listVoucherByShopId(@PathVariable Long shopId) {

        return ResponseEntity.ok(voucherService.listVoucherByShopId(shopId));
    }

    @GetMapping("/list-on-system")
    public ResponseEntity<ListVoucherResponse> listVoucherSystem() {
        return ResponseEntity.ok(voucherService.listVoucherSystem());
    }

    @GetMapping("/list-by-type/{type}")
    public ResponseEntity<ListVoucherResponse> listVoucherByType(@PathVariable VoucherType type) {

        return ResponseEntity.ok(voucherService.listVoucherByType(type));
    }

    @GetMapping("/list-all")
    public ResponseEntity<ListVoucherResponse> listAllVoucher() {
        return ResponseEntity.ok(voucherService.allVoucher());
    }

}
