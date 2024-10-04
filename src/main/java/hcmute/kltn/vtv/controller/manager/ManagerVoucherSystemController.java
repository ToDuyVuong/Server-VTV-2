package hcmute.kltn.vtv.controller.manager;

import hcmute.kltn.vtv.model.data.manager.request.VoucherSystemRequest;
import hcmute.kltn.vtv.model.data.manager.response.ListVoucherSystemResponse;
import hcmute.kltn.vtv.model.data.manager.response.VoucherSystemResponse;
import hcmute.kltn.vtv.model.extra.Status;
import hcmute.kltn.vtv.util.exception.BadRequestException;
import hcmute.kltn.vtv.model.extra.VoucherType;
import hcmute.kltn.vtv.service.manager.IManagerVoucherSystemService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/manager/voucher/system")
@RequiredArgsConstructor
public class ManagerVoucherSystemController {

    private final IManagerVoucherSystemService managerVoucherSystemService;

    @PostMapping("/add")
    public ResponseEntity<VoucherSystemResponse> addNewVoucherSystem(@RequestBody VoucherSystemRequest request,
                                                                     HttpServletRequest servletRequest) {
        String username = (String) servletRequest.getAttribute("username");
        request.validateCreate();

        return ResponseEntity.ok(managerVoucherSystemService.addNewVoucherSystem(username, request));
    }

    @GetMapping("/detail/{voucherId}")
    public ResponseEntity<VoucherSystemResponse> getVoucherSystemByVoucherId(@PathVariable Long voucherId) {

        return ResponseEntity.ok(managerVoucherSystemService.getVoucherSystemByVoucherId(voucherId));
    }

    @GetMapping("/get-all/by-username")
    public ResponseEntity<ListVoucherSystemResponse> getAllVoucherSystemByUsername(HttpServletRequest servletRequest) {
        String username = (String) servletRequest.getAttribute("username");

        return ResponseEntity.ok(managerVoucherSystemService.getListVoucherSystemByUsername(username));
    }

    @GetMapping("/get-all")
    public ResponseEntity<ListVoucherSystemResponse> getAllVoucherSystem(HttpServletRequest servletRequest) {
        String username = (String) servletRequest.getAttribute("username");
        return ResponseEntity.ok(managerVoucherSystemService.getListVoucherSystem(username));
    }

    @GetMapping("/get-all/by-status")
    public ResponseEntity<ListVoucherSystemResponse> getAllVoucherSystemByStatus(@RequestParam Status status,
                                                                                 HttpServletRequest servletRequest) {
        String username = (String) servletRequest.getAttribute("username");
        return ResponseEntity.ok(managerVoucherSystemService.getListVoucherSystemByStatus(username, status));
    }

    @GetMapping("/get-all/system/by-type")
    public ResponseEntity<ListVoucherSystemResponse> getListVoucherSystemByType(@RequestParam VoucherType type,
                                                                              HttpServletRequest servletRequest) {
        String username = (String) servletRequest.getAttribute("username");
        return ResponseEntity.ok(managerVoucherSystemService.getListVoucherSystemByType(username, type));
    }

    @PutMapping("/update/{voucherId}")
    public ResponseEntity<VoucherSystemResponse> updateVoucherSystem(@PathVariable Long voucherId,
                                                                     @RequestBody VoucherSystemRequest request,
                                                                     HttpServletRequest servletRequest) {
        String username = (String) servletRequest.getAttribute("username");


        return ResponseEntity.ok(managerVoucherSystemService.updateVoucherSystem(voucherId, request, username));
    }

    @PatchMapping("/update-status/{voucherId}")
    public ResponseEntity<VoucherSystemResponse> updateStatusVoucherSystem(@PathVariable Long voucherId,
                                                                           @RequestParam Status status,
                                                                           HttpServletRequest servletRequest) {
        String username = (String) servletRequest.getAttribute("username");

        return ResponseEntity.ok(managerVoucherSystemService.updateStatusVoucherSystem(voucherId, status, username));
    }

}
