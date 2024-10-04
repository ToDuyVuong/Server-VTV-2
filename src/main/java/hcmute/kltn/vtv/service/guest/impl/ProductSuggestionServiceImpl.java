package hcmute.kltn.vtv.service.guest.impl;


import hcmute.kltn.vtv.model.data.vendor.response.ProductPageResponse;
import hcmute.kltn.vtv.model.entity.vtv.Category;
import hcmute.kltn.vtv.model.entity.vendor.Product;
import hcmute.kltn.vtv.model.extra.Status;
import hcmute.kltn.vtv.repository.vtv.CategoryRepository;
import hcmute.kltn.vtv.repository.vendor.ProductRepository;
import hcmute.kltn.vtv.repository.vtv.ProductSearchRepository;
import hcmute.kltn.vtv.service.guest.IProductSuggestionService;
import hcmute.kltn.vtv.util.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class ProductSuggestionServiceImpl implements IProductSuggestionService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductSearchRepository productSearchRepository;


    @Override
    public ProductPageResponse suggestByRandomly(int page, int size) {

        Page<Product> productPage = productRepository.suggestByRandomly(Status.ACTIVE.toString(), PageRequest.of(page - 1, size))
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm phù hợp"));

        return ProductPageResponse.productPageResponse(productPage,"Lấy danh sách sản phẩm gợi ý thành công!");
    }


    @Override
    public ProductPageResponse suggestByRandomlyAndProductId(Long productId, int page, int size, boolean inShop) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm phù hợp"));
        String content = product.getName();
        Page<Product> productPage;
        String message;

        if (inShop) {
            productPage = productSearchRepository.searchFullTextOnShopByNameAndStatusAndSortRandomly(
                            content, product.getShop().getShopId(), Status.ACTIVE.toString(),
                            PageRequest.of(page - 1, size))
                    .orElseThrow(() -> new NotFoundException("Không tìm thấy sản phẩm phù hợp"));
            message = "Lấy danh sách sản phẩm gợi ý trong cùng cửa hàng thành công!";
        } else {
            productPage = productSearchRepository.searchFullTextByNameAndStatusAndSortRandomly(
                            content, Status.ACTIVE.toString(),
                            PageRequest.of(page - 1, size))
                    .orElseThrow(() -> new NotFoundException("Không tìm thấy sản phẩm phù hợp"));
            message = "Lấy danh sách sản phẩm gợi ý thành công!";
        }

        return ProductPageResponse.productPageResponse(productPage,message);
    }


    @Override
    public ProductPageResponse suggestByCategoryAndRandomly(Long categoryId, int page, int size) {

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm phù hợp"));
        Page<Product> productPage = new PageImpl<>(new ArrayList<>());
        String message;

        if (category.getParent() == null) {
            productPage = productRepository.suggestByCategoryParentAndRandomly(
                            category.getCategoryId(), Status.ACTIVE.toString(),
                            PageRequest.of(page - 1, size))
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm phù hợp"));
            message = "Lấy danh sách sản phẩm gợi ý trong cùng cửa hàng thành công!";
        } else {
            productPage = productRepository.suggestByCategoryAndRandomly(
                            categoryId, Status.ACTIVE.toString(),
                            PageRequest.of(page - 1, size))
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm phù hợp"));
            message = "Lấy danh sách sản phẩm gợi ý thành công!";
        }

        return ProductPageResponse.productPageResponse(productPage,message);
    }


}
