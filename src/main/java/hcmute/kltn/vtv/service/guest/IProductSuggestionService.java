package hcmute.kltn.vtv.service.guest;

import hcmute.kltn.vtv.model.data.vendor.response.ProductPageResponse;

public interface IProductSuggestionService {

    ProductPageResponse suggestByRandomly(int page, int size);


    ProductPageResponse suggestByRandomlyAndProductId(Long productId, int page, int size, boolean inShop);

    ProductPageResponse suggestByCategoryAndRandomly(Long categoryId, int page, int size);
}
