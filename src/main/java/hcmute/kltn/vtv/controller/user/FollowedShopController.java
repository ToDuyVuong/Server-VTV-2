package hcmute.kltn.vtv.controller.user;

import hcmute.kltn.vtv.util.exception.BadRequestException;
import hcmute.kltn.vtv.model.data.user.response.FollowedShopResponse;
import hcmute.kltn.vtv.model.data.user.response.ListFollowedShopResponse;
import hcmute.kltn.vtv.service.user.IFollowedShopService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customer/followed-shop")
@RequiredArgsConstructor
public class FollowedShopController {

    private final  IFollowedShopService followedShopService;

    @PostMapping("/add")
    public ResponseEntity<FollowedShopResponse> addNewFollowedShop(@RequestParam Long shopId,
            HttpServletRequest request) {
        if (shopId == null) {
            throw new BadRequestException("Mã cửa hàng không được để trống!");
        }
        String username = (String) request.getAttribute("username");
        FollowedShopResponse response = followedShopService.addNewFollowedShop(shopId, username);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/list")
    public ResponseEntity<ListFollowedShopResponse> getListFollowedShop(HttpServletRequest request) {
        String username = (String) request.getAttribute("username");
        return ResponseEntity.ok(followedShopService.getListFollowedShopByUsername(username));
    }

    @DeleteMapping("/delete/{followedShopId}")
    public ResponseEntity<FollowedShopResponse> deleteFollowedShopById(
            @PathVariable("followedShopId") Long followedShopId,
            HttpServletRequest request) {
        if (followedShopId == null) {
            throw new BadRequestException("Mã cửa hàng theo dõi không được để trống!");
        }
        String username = (String) request.getAttribute("username");

        FollowedShopResponse response = followedShopService.deleteFollowedShop(followedShopId, username);
        return ResponseEntity.ok(response);
    }

}
