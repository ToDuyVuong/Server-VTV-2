package hcmute.kltn.vtv.controller.manager;

import hcmute.kltn.vtv.model.data.manager.response.ManagerShopPageResponse;
import hcmute.kltn.vtv.model.data.manager.response.ManagerShopResponse;
import hcmute.kltn.vtv.model.data.manager.response.ShopsResponse;
import hcmute.kltn.vtv.model.data.vendor.response.ShopResponse;
import hcmute.kltn.vtv.model.extra.Status;
import hcmute.kltn.vtv.service.manager.IManagerShopService;
import hcmute.kltn.vtv.service.vtv.IPageService;
import hcmute.kltn.vtv.util.exception.BadRequestException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/manager/shop")
@RequiredArgsConstructor
public class ManagerShopController {

    private final IManagerShopService managerShopService;
    private final IPageService pageService;

    @GetMapping("/{shopId}")
    public ResponseEntity<ShopResponse> getShopById(@PathVariable Long shopId) {
        return ResponseEntity.ok(managerShopService.getShopById(shopId));
    }

    @GetMapping("/manager-shop/page/lock/{lock}")
    public ResponseEntity<ManagerShopPageResponse> getManagerShopPageByLock(@RequestParam int page,
                                                                            @RequestParam int size,
                                                                            @PathVariable boolean lock) {
        pageService.validatePageNumberAndSize(page, size);

        return ResponseEntity.ok(managerShopService.getManagerShopPageByLock(size, page, lock));
    }

    @GetMapping("/manager-shop/page/search/{name}/lock/{lock}")
    public ResponseEntity<ManagerShopPageResponse> getManagerShopPageByNameAndLock(@RequestParam int page,
                                                                                   @RequestParam int size,
                                                                                   @PathVariable String name,
                                                                                   @PathVariable boolean lock) {
        pageService.validatePageNumberAndSize(page, size);

        return ResponseEntity.ok(managerShopService.getManagerShopPageByNameAndLock(size, page, name, lock));
    }


    @GetMapping("/page/status/{status}")
    public ResponseEntity<ShopsResponse> getShopsByStatus(@RequestParam int page,
                                                          @RequestParam int size,
                                                          @PathVariable Status status) {
        pageService.validatePageNumberAndSize(page, size);

        return ResponseEntity.ok(managerShopService.getShopsByStatus(page, size, status));
    }


    @GetMapping("/page/search/{name}/status/{status}")
    public ResponseEntity<ShopsResponse> getShopsByNameAndStatus(@RequestParam int page,
                                                                 @RequestParam int size,
                                                                 @PathVariable String name,
                                                                 @PathVariable Status status) {
        pageService.validatePageNumberAndSize(page, size);

        return ResponseEntity.ok(managerShopService.getShopsByNameAndStatus(page, size, name, status));
    }


    @PostMapping("/lock/{shopId}")
    public ResponseEntity<ManagerShopResponse> lockShopById(@PathVariable Long shopId,
                                                            @RequestBody String note,
                                                            HttpServletRequest request) {
        if (note == null || note.isEmpty()) {
            throw new BadRequestException("Vui lòng nhập lý do khóa cửa hàng!");
        }
        String username = (String) request.getAttribute("username");

        return ResponseEntity.ok(managerShopService.lockShopById(shopId, username, note));
    }


    @PostMapping("/unlock/{shopId}")
    public ResponseEntity<ManagerShopResponse> unlockShopById(@PathVariable Long shopId,
                                                              @RequestBody String note,
                                                              HttpServletRequest request) {
        if (note == null || note.isEmpty()) {
            throw new BadRequestException("Vui lòng nhập lý do mở khóa cửa hàng!");
        }
        String username = (String) request.getAttribute("username");

        return ResponseEntity.ok(managerShopService.unlockShopById(shopId, username, note));
    }


}
