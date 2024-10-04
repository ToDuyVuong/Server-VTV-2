package hcmute.kltn.vtv.service.vendor;

import hcmute.kltn.vtv.model.data.guest.ResponseClass;
import hcmute.kltn.vtv.model.data.vendor.request.CategoryShopRequest;
import hcmute.kltn.vtv.model.data.vendor.response.CategoryShopResponse;
import hcmute.kltn.vtv.model.data.vendor.response.ListCategoryShopResponse;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ICategoryShopService {
    @Transactional
    CategoryShopResponse addNewCategoryShop(CategoryShopRequest request, String username);

    @Transactional
    CategoryShopResponse updateCategoryShop(CategoryShopRequest request, Long categoryShopId, String username);

    ListCategoryShopResponse getAllByUsername(String username);

    @Transactional

    CategoryShopResponse addProductToCategoryShop(Long categoryShopId, List<Long> productIds, String username);

    @Transactional
    CategoryShopResponse removeProductFromCategoryShop(Long categoryShopId, List<Long> productIds, String username);

    @Transactional
    ResponseClass deleteCategoryShopById(Long categoryShopId, String username);
}
