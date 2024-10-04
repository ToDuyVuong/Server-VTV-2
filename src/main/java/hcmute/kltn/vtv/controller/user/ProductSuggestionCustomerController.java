package hcmute.kltn.vtv.controller.user;

import hcmute.kltn.vtv.model.data.vendor.response.ProductPageResponse;
import hcmute.kltn.vtv.service.vtv.IPageService;
import hcmute.kltn.vtv.service.user.IProductSuggestionCustomerService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/customer/product-suggestion")
@RequiredArgsConstructor
public class ProductSuggestionCustomerController {

    private final IProductSuggestionCustomerService productSuggestionService;
    private final IPageService pageService;

    @GetMapping("/get-page/search-history")
    public ResponseEntity<ProductPageResponse> getProductSuggestionBySearchHistory(@Param("page") int page,
                                                                                   @Param("size") int size,
                                                                                   HttpServletRequest request) {
        String username = (String) request.getAttribute("username");
        pageService.checkRequestProductPageParams(page, size);

        return ResponseEntity.ok(productSuggestionService.getProductSuggestionBySearchHistory(username, page, size));
    }


}
