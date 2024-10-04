package hcmute.kltn.vtv.controller.vendor;

import hcmute.kltn.vtv.model.data.vendor.request.ChangePriceProductsByPercentRequest;
import hcmute.kltn.vtv.model.data.vendor.request.ChangePriceProductsRequest;
import hcmute.kltn.vtv.model.data.vendor.request.ProductVariantRequest;
import hcmute.kltn.vtv.model.data.vendor.response.ListProductResponse;
import hcmute.kltn.vtv.model.data.vendor.response.ProductPageResponse;
import hcmute.kltn.vtv.model.data.vendor.request.ProductRequest;
import hcmute.kltn.vtv.model.data.guest.ProductResponse;
import hcmute.kltn.vtv.model.extra.Status;
import hcmute.kltn.vtv.service.vtv.IPageService;

import hcmute.kltn.vtv.service.vendor.IProductShopService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/vendor/product")
@RequiredArgsConstructor
public class ProductShopController {

    private final IProductShopService productShopService;
    private final IPageService pageService;


    @PostMapping("/add")
    public ResponseEntity<ProductResponse> addNewProduct(@ModelAttribute ProductRequest request,
                                                         HttpServletRequest httpServletRequest) {
        String username = (String) httpServletRequest.getAttribute("username");
        ProductVariantRequest.validateAddProductVariantWithImage(request.getProductVariantRequests());
        request.validate();


        return ResponseEntity.ok(productShopService.addNewProduct(request, username));
    }


    @PostMapping("/update/{productId}")
    public ResponseEntity<ProductResponse> updateProduct(@PathVariable Long productId,
                                                         @ModelAttribute ProductRequest request,
                                                         HttpServletRequest httpServletRequest) {
        String username = (String) httpServletRequest.getAttribute("username");
        request.setProductId(productId);
        request.validate();
        return ResponseEntity.ok(productShopService.updateProduct(request, username));
    }


    @PatchMapping("/update/{productId}/status/{status}")
    public ResponseEntity<ProductResponse> updateStatusProduct(@PathVariable Long productId,
                                                               @PathVariable Status status,
                                                               HttpServletRequest httpServletRequest) {
        String username = (String) httpServletRequest.getAttribute("username");
        return ResponseEntity.ok(productShopService.updateStatusProduct(productId, username, status));
    }


    @PatchMapping("/restore/{productId}")
    public ResponseEntity<ProductResponse> restoreProductById(@PathVariable Long productId,
                                                              HttpServletRequest httpServletRequest) {
        String username = (String) httpServletRequest.getAttribute("username");
        return ResponseEntity.ok(productShopService.restoreProductById(productId, username));
    }


    @GetMapping("/page/status/{status}")
    public ResponseEntity<ProductPageResponse> getPageProductByStatus(@RequestParam int page,
                                                                      @RequestParam int size,
                                                                      @PathVariable Status status,
                                                                      HttpServletRequest httpServletRequest) {
        pageService.checkRequestProductPageParams(page, size);
        String username = (String) httpServletRequest.getAttribute("username");
        return ResponseEntity.ok(productShopService.getListProductPageByUsernameAndStatus(username, page, size, status));
    }



    @PostMapping("/update/changes/price")
    public ResponseEntity<ListProductResponse> updateProductsByIdAndPrice(@RequestBody ChangePriceProductsRequest request,
                                                                          HttpServletRequest httpServletRequest) {
        String username = (String) httpServletRequest.getAttribute("username");
        request.validate();
        return ResponseEntity.ok(productShopService.updateProductsByIdAndPrice(request, username));
    }


    @PostMapping("/update/changes/price/percent")
    public ResponseEntity<ListProductResponse> updateProductsByIdAndPriceAndPercent(@RequestBody ChangePriceProductsByPercentRequest request,
                                                                          HttpServletRequest httpServletRequest) {
        String username = (String) httpServletRequest.getAttribute("username");
        request.validate();
        return ResponseEntity.ok(productShopService.updateProductsByIdAndPriceAndPercent(request, username));
    }



}
