package hcmute.kltn.vtv.service.user;

import hcmute.kltn.vtv.model.data.vendor.response.ProductPageResponse;

public interface ISearchProductService {
    ProductPageResponse getProductPageBySearchAndSort(String search, int page, int size, String sort);

    ProductPageResponse getProductsPageBySearchAndPriceRangeAndSort(String search, int page, int size, String sort,
                                                                    Long minPrice, Long maxPrice);

    ProductPageResponse getProductPageByUsernameAndSearchSortOnShop(String search, String username,
                                                                    int page, int size, String sort);

    ProductPageResponse getProductsPageBySearchAndPriceRangeAndSortOnShop(String search, Long shopId, int page, int size, String sort,
                                                                          Long minPrice, Long maxPrice);

    ProductPageResponse getProductsPageBySearchAndSortOnShop(String search, Long shopId, int page, int size, String sort);
}
