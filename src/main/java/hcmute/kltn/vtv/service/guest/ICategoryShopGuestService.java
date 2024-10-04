package hcmute.kltn.vtv.service.guest;

import hcmute.kltn.vtv.model.data.vendor.response.CategoryShopResponse;
import hcmute.kltn.vtv.model.data.vendor.response.ListCategoryShopResponse;
import hcmute.kltn.vtv.model.entity.vendor.CategoryShop;

public interface ICategoryShopGuestService {
    CategoryShopResponse getCategoryShopById(Long categoryShopId);

    ListCategoryShopResponse getCategoryShopsByShopId(Long shopId);

    void checkExistsById(Long categoryShopId);

    CategoryShop getCategoryShopByCategoryShopId(Long categoryShopId);

    int countCategoryShopByShopId(Long shopId);
}
