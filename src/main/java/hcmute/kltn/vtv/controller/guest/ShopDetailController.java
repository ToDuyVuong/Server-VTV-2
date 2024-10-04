package hcmute.kltn.vtv.controller.guest;

import hcmute.kltn.vtv.model.data.guest.ShopDetailResponse;
import hcmute.kltn.vtv.util.exception.BadRequestException;
import hcmute.kltn.vtv.service.guest.IFollowedGuestService;
import hcmute.kltn.vtv.service.guest.IShopGuestService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/shop")
@RequiredArgsConstructor
public class ShopDetailController {

    private final IShopGuestService shopDetailService;
    private final IFollowedGuestService followedGuestService;

    @GetMapping("/{shopId}")
    public ResponseEntity<ShopDetailResponse> getShopDetailByShopId(@PathVariable Long shopId) {

        return ResponseEntity.ok(shopDetailService.getShopDetailByShopId(shopId));
    }


    @GetMapping("/username/{username}")
    public ResponseEntity<ShopDetailResponse> getShopDetailByUsername(@PathVariable String username) {

        return ResponseEntity.ok(shopDetailService.getShopDetailByUsername(username));
    }


    @GetMapping("/count-followed/{shopId}")
    public ResponseEntity<Integer> countFollowedShop(@PathVariable Long shopId) {

        return ResponseEntity.ok(followedGuestService.countFollowedByShop(shopId));
    }
}
