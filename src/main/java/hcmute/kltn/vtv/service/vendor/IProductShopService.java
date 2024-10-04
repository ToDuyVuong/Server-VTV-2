package hcmute.kltn.vtv.service.vendor;


import hcmute.kltn.vtv.model.data.vendor.request.ChangePriceProductsByPercentRequest;
import hcmute.kltn.vtv.model.data.vendor.request.ChangePriceProductsRequest;
import hcmute.kltn.vtv.model.data.vendor.response.ListProductResponse;
import hcmute.kltn.vtv.model.data.vendor.response.ProductPageResponse;
import hcmute.kltn.vtv.model.data.vendor.request.ProductRequest;
import hcmute.kltn.vtv.model.data.guest.ProductResponse;
import hcmute.kltn.vtv.model.entity.vendor.Product;
import hcmute.kltn.vtv.model.extra.Status;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface IProductShopService {
    @Transactional
    ProductResponse addNewProduct(ProductRequest request, String username);

    @Transactional
    ProductResponse updateProduct(ProductRequest request, String username);

    @Transactional
    ProductResponse updateStatusProduct(Long productId, String username, Status status);

    @Transactional
    ProductResponse restoreProductById(Long productId, String username);

    ProductPageResponse getListProductPageByUsernameAndStatus(String username, int page, int size, Status status);

    @Transactional
    ListProductResponse updateProductsByIdAndPrice(ChangePriceProductsRequest request, String username);

    @Transactional
    ListProductResponse updateProductsByIdAndPriceAndPercent(ChangePriceProductsByPercentRequest request, String username);

    void checkProductIdsInShop(List<Long> productIds, Long shopId);

    List<Product> getProductsByProductIds(List<Long> productIds);

    void checkExistProductByProductIdAndUsername(Long productId, String username);
//    ProductResponse addNewProduct(ProductRequest request);
//
//    ProductResponse getProductDetail(Long productId, String username);
//
//    ListProductResponse getListProductByUsername(String username);
//
//    ListProductPageResponse getListProductByUsernamePage(String username, int page, int size);
//
//
//    ListProductResponse getListProductShopByCategoryId(Long categoryId, String username);
//
//    ListProductResponse searchProductsByName(String productName, String username);
//
//    ListProductResponse getBestSellingProducts(int limit, String username);
//
//    ListProductResponse getListProductByPriceRange(String username, Long minPrice, Long maxPrice);
//
//    ListProductResponse getListNewProduct(String username);
//
//    ProductResponse updateProduct(ProductRequest productRequest);
//
//    @Transactional
//    ProductResponse updateStatusProduct(Long productId, String username, Status status);
//
//    @Transactional
//    ProductResponse restoreProductById(Long productId, String username);
//
//    ListProductResponse getAllDeletedProduct(String username);
//
//    ListProductResponse getListProductResponseSort(List<Product> products, String message, boolean isSort);
//
//    ProductDTO getProductToDTO(Product product);
}
