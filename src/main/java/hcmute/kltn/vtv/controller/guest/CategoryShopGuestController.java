package hcmute.kltn.vtv.controller.guest;


import hcmute.kltn.vtv.model.data.vendor.response.CategoryShopResponse;
import hcmute.kltn.vtv.model.data.vendor.response.ListCategoryShopResponse;
import hcmute.kltn.vtv.service.guest.ICategoryShopGuestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/category-shop")
@RequiredArgsConstructor
public class CategoryShopGuestController {

    private final ICategoryShopGuestService categoryShopGuestService;


    @GetMapping("/category-shop-id/{categoryShopId}")
    public ResponseEntity<CategoryShopResponse> getCategoryShopById(@PathVariable Long categoryShopId) {
        return ResponseEntity.ok(categoryShopGuestService.getCategoryShopById(categoryShopId));
    }



    @GetMapping("/get-list/shop-id/{shopId}")
    public ResponseEntity<ListCategoryShopResponse> getCategoryShopsByShopId(@PathVariable Long shopId) {
        return ResponseEntity.ok(categoryShopGuestService.getCategoryShopsByShopId(shopId));
    }

}
