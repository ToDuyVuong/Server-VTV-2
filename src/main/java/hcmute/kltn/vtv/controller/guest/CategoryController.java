package hcmute.kltn.vtv.controller.guest;


import hcmute.kltn.vtv.model.data.guest.CategoriesResponse;
import hcmute.kltn.vtv.model.data.guest.CategoryResponse;
import hcmute.kltn.vtv.service.guest.ICategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/category")
@RequiredArgsConstructor
public class CategoryController {


    private final ICategoryService categoryService;

    @GetMapping("/all-parent")
    public ResponseEntity<CategoriesResponse> getParentCategory() {
        return ResponseEntity.ok(categoryService.getAllCategoryParent());
    }


    @GetMapping("/all-category/by-parent/{categoryId}")
    public ResponseEntity<CategoriesResponse> getCategoryByParentId(@PathVariable Long categoryId) {
        return ResponseEntity.ok(categoryService.getAllCategoryByParentId(categoryId));
    }


    @GetMapping("/all")
    public ResponseEntity<CategoriesResponse> getAllCategory() {
        return ResponseEntity.ok(categoryService.getAllCategory());
    }


    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryResponse> getCategoryByCategoryId(@PathVariable Long categoryId) {
        return ResponseEntity.ok(categoryService.getCategoryByCategoryId(categoryId));
    }





}
