package hcmute.kltn.vtv.service.user;

import hcmute.kltn.vtv.model.data.vendor.response.ProductPageResponse;

public interface IProductSuggestionCustomerService {
    ProductPageResponse getProductSuggestionBySearchHistory(String username, int page, int size);

    ProductPageResponse suggestByRandomly(int page, int size);
}
