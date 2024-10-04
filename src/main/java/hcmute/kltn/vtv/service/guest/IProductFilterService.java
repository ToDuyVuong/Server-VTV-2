package hcmute.kltn.vtv.service.guest;

import hcmute.kltn.vtv.model.data.vendor.response.ProductPageResponse;

public interface IProductFilterService {
    ProductPageResponse getFilterProductPage(int page, int size, String filter);

    ProductPageResponse getFilterProductPageByPriceRange(int page, int size, String filter,
                                                         Long minPrice, Long maxPrice);
}
