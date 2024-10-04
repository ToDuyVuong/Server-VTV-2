package hcmute.kltn.vtv.service.user.impl;


import hcmute.kltn.vtv.model.data.vendor.response.ProductPageResponse;
import hcmute.kltn.vtv.model.entity.vendor.Product;
import hcmute.kltn.vtv.model.extra.Status;
import hcmute.kltn.vtv.repository.vendor.ProductRepository;
import hcmute.kltn.vtv.service.user.IProductSuggestionCustomerService;
import hcmute.kltn.vtv.service.user.ISearchHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductSuggestionCustomerServiceImpl implements IProductSuggestionCustomerService {

    private final ProductRepository productRepository;
    private final ISearchHistoryService searchHistoryService;

    @Override
    public ProductPageResponse getProductSuggestionBySearchHistory(String username, int page, int size) {
        String searchHistoriesToString = searchHistoryService.toStringSearchHistoryByUsername(username, 15, 1);
        Page<Product> productPage = productRepository.suggestBySearchHistoryAndRandomly(
                        searchHistoriesToString, Status.ACTIVE.toString(), PageRequest.of(page - 1, size))
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm phù hợp"));

        return ProductPageResponse.productPageResponse(productPage,  "Lấy danh sách sản phẩm theo lịch sử tìm kiếm thành công!");
    }


    @Override
    public ProductPageResponse suggestByRandomly(int page, int size) {
        Page<Product> productPage = productRepository.suggestByRandomly(Status.ACTIVE.toString(), PageRequest.of(page - 1, size))
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm phù hợp"));

        return ProductPageResponse.productPageResponse(productPage, "Lấy danh sách sản phẩm gợi ý thành công!");


    }
}
