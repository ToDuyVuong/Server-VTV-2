package hcmute.kltn.vtv.service.guest;

import hcmute.kltn.vtv.model.data.vendor.response.ProductPageResponse;

public interface IProductPageService {

    ProductPageResponse getProductPage(int page, int size);

    ProductPageResponse getListProductPageByCategoryId(Long categoryId, int page, int size);

    ProductPageResponse getProductPageByShopId(Long shopId, int page, int size);

    ProductPageResponse getListBestSellingProductsPageByShopId(Long shopId, int page, int size);

    ProductPageResponse getNewProductPageByShopId(Long shopId, int page, int size);





}
