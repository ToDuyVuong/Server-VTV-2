package hcmute.kltn.vtv.controller.manager;

import hcmute.kltn.vtv.service.vtv.IPageService;
import hcmute.kltn.vtv.util.exception.BadRequestException;
import hcmute.kltn.vtv.model.data.manager.response.ManagerProductPageResponse;
import hcmute.kltn.vtv.model.data.manager.response.ManagerProductResponse;
import hcmute.kltn.vtv.service.manager.IManagerProductService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/manager/product")
@RequiredArgsConstructor
public class ManagerProductController {

    private final IManagerProductService managerProductService;
    private final IPageService pageService;

    @PostMapping("/lock/{productId}")
        public ResponseEntity<ManagerProductResponse> lockProductByProductId(@PathVariable Long productId,
                                                                         @RequestBody String note,
                                                                         HttpServletRequest request) {
        String username = (String) request.getAttribute("username");
        if (note == null || note.trim().isEmpty()) {
            throw new BadRequestException("Ghi chú không hợp lệ! Vui lòng nhập ghi chú");
        }

        return ResponseEntity.ok(managerProductService.lockProductByProductId(productId, username, note.trim()));
    }

    @PostMapping("/unlock/{productId}")
    public ResponseEntity<ManagerProductResponse> unLockProductByProductId(@PathVariable Long productId,
                                                                           @RequestBody String note,
                                                                           HttpServletRequest request) {
        String username = (String) request.getAttribute("username");
        if (note == null || note.trim().isEmpty()) {
            throw new BadRequestException("Ghi chú không hợp lệ! Vui lòng nhập ghi chú");
        }

        return ResponseEntity.ok(managerProductService.unLockProductByProductId(productId, username, note.trim()));
    }

    @GetMapping("/page")
    public ResponseEntity<ManagerProductPageResponse> getManagerProductPage(@RequestParam int page,
                                                                            @RequestParam int size) {
        pageService.checkRequestProductPageParams(page, size);

        return ResponseEntity.ok(managerProductService.getManagerProductPage(page, size));
    }

    @GetMapping("/page/search/{productName}")
    public ResponseEntity<ManagerProductPageResponse> getManagerProductPageByProductName(@RequestParam int page,
                                                                                         @RequestParam int size,
                                                                                         @PathVariable String productName) {
        pageService.checkRequestProductPageParams(page, size);

        return ResponseEntity.ok(managerProductService.getManagerProductPageByProductName(page, size, productName.trim()));
    }

}
