package hcmute.kltn.vtv.service.guest;

import hcmute.kltn.vtv.model.data.vendor.response.ProductPageResponse;
import hcmute.kltn.vtv.model.data.vendor.response.ListProductResponse;
import hcmute.kltn.vtv.model.data.guest.ProductResponse;
import hcmute.kltn.vtv.model.entity.vendor.Product;
import hcmute.kltn.vtv.model.extra.OrderStatus;

public interface IProductService {
    ProductResponse getProductDetail(Long productId);


    int countOrdersByProductId(Long productId, OrderStatus status);

    void updateProductSold(Long productId, int quantity);

    ListProductResponse getListProductByShopId(Long shopId);

    ProductPageResponse getProductPageByCategoryId(Long categoryId, int page, int size);

    ListProductResponse getBestSellingProducts(Long shopId, int limit, boolean isShop);

//    ListProductResponse getListNewProduct(Long shopId);

//    ListProductResponse getListProductByPriceRange(Long shopId, Long minPrice, Long maxPrice);


    int countProductByShopId(Long shopId);

    Product getProductById(Long productId);
}
