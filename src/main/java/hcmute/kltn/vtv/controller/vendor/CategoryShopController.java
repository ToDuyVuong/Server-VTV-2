package hcmute.kltn.vtv.controller.vendor;


import hcmute.kltn.vtv.model.data.guest.ResponseClass;
import hcmute.kltn.vtv.model.data.vendor.request.CategoryShopRequest;
import hcmute.kltn.vtv.model.data.vendor.response.CategoryShopResponse;
import hcmute.kltn.vtv.model.data.vendor.response.ListCategoryShopResponse;
import hcmute.kltn.vtv.service.vendor.ICategoryShopService;
import hcmute.kltn.vtv.util.exception.BadRequestException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vendor/category-shop")
@RequiredArgsConstructor
public class CategoryShopController {

    private final ICategoryShopService categoryShopService;


    @PostMapping("/add")
    public ResponseEntity<CategoryShopResponse> addNewCategoryShop(@ModelAttribute CategoryShopRequest categoryShopRequest,
                                                                   HttpServletRequest request) {
        String username = (String) request.getAttribute("username");
        categoryShopRequest.validateCreate();

        return ResponseEntity.ok(categoryShopService.addNewCategoryShop(categoryShopRequest, username));
    }

    @PutMapping("/update/{categoryShopId}")
    public ResponseEntity<CategoryShopResponse> updateCategoryShop(@PathVariable Long categoryShopId,
                                                                   @ModelAttribute CategoryShopRequest categoryShopRequest,
                                                                   HttpServletRequest request) {
        String username = (String) request.getAttribute("username");
        categoryShopRequest.validate();

        return ResponseEntity.ok(categoryShopService.updateCategoryShop(categoryShopRequest, categoryShopId, username));
    }


    @GetMapping("/get-all")
    public ResponseEntity<ListCategoryShopResponse> getAllCategoryShop(HttpServletRequest request) {
        String username = (String) request.getAttribute("username");
        return ResponseEntity.ok(categoryShopService.getAllByUsername(username));
    }


    @PutMapping("/id/{categoryShopId}/add-product")
    public ResponseEntity<CategoryShopResponse> addProductToCategoryShop(@PathVariable Long categoryShopId,
                                                                         @RequestBody List<Long> productIds,
                                                                         HttpServletRequest request) {
        if (productIds.isEmpty()) {
            throw new BadRequestException("Danh sách sản phẩm không được để trống!");
        }
        String username = (String) request.getAttribute("username");
        return ResponseEntity.ok(categoryShopService.addProductToCategoryShop(categoryShopId, productIds, username));
    }



    @DeleteMapping("/id/{categoryShopId}/delete-product")
    public ResponseEntity<CategoryShopResponse> deleteProductToCategoryShop(@PathVariable Long categoryShopId,
                                                                         @RequestBody List<Long> productIds,
                                                                         HttpServletRequest request) {
        if (productIds.isEmpty()) {
            throw new BadRequestException("Danh sách sản phẩm không được để trống!");
        }
        String username = (String) request.getAttribute("username");
        return ResponseEntity.ok(categoryShopService.removeProductFromCategoryShop(categoryShopId, productIds, username));
    }

    @DeleteMapping("/delete/{categoryShopId}")
    public ResponseEntity<ResponseClass> deleteCategoryShopById(@PathVariable Long categoryShopId, HttpServletRequest request) {
        String username = (String) request.getAttribute("username");
        return ResponseEntity.ok(categoryShopService.deleteCategoryShopById(categoryShopId, username));
    }

}
