package hcmute.kltn.vtv.controller.guest;

import hcmute.kltn.vtv.model.data.vendor.response.ProductPageResponse;
import hcmute.kltn.vtv.service.vtv.IPageService;
import hcmute.kltn.vtv.service.guest.IProductPageService;
import hcmute.kltn.vtv.service.guest.IProductService;
import hcmute.kltn.vtv.util.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/product/page")
@RequiredArgsConstructor
public class ProductPageController {

    private final IProductPageService productPageService;
    private final IPageService pageService;
    private final IProductService productService;

    @GetMapping("/list")
    public ResponseEntity<ProductPageResponse> getProductPage(@RequestParam int page,
                                                              @RequestParam int size) {
        pageService.checkRequestProductPageParams(page, size);

        return ResponseEntity.ok(productPageService.getProductPage(page, size));
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<ProductPageResponse> getProductPageByCategoryId(@RequestParam int page,
                                                                          @RequestParam int size,
                                                                          @PathVariable Long categoryId) {
        pageService.checkRequestProductPageParams(page, size);

        return ResponseEntity.ok(productService.getProductPageByCategoryId(categoryId, page, size));
    }

    @GetMapping("/shop/{shopId}")
    public ResponseEntity<ProductPageResponse> getProductPageByShopId(@RequestParam int page,
                                                                          @RequestParam int size,
                                                                          @PathVariable Long shopId) {
        if (shopId == null) {
            throw new NotFoundException("Mã cửa hàng không được để trống!");
        }
        pageService.checkRequestProductPageParams(page, size);

        return ResponseEntity.ok(productPageService.getProductPageByShopId(shopId, page, size));
    }



    @GetMapping("/shop/{shopId}/new")
    public ResponseEntity<ProductPageResponse> getNewProductPageByShopId(@RequestParam int page,
                                                                              @RequestParam int size,
                                                                              @PathVariable Long shopId) {
        if (shopId == null) {
            throw new NotFoundException("Mã cửa hàng không được để trống!");
        }
        pageService.checkRequestProductPageParams(page, size);

        return ResponseEntity.ok(productPageService.getNewProductPageByShopId(shopId, page, size));
    }



}
