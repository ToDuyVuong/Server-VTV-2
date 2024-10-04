package hcmute.kltn.vtv.service.guest.impl;

import hcmute.kltn.vtv.model.data.vendor.response.CategoryShopResponse;
import hcmute.kltn.vtv.model.data.vendor.response.ListCategoryShopResponse;
import hcmute.kltn.vtv.model.entity.vendor.CategoryShop;
import hcmute.kltn.vtv.repository.vendor.CategoryShopRepository;
import hcmute.kltn.vtv.service.guest.ICategoryShopGuestService;
import hcmute.kltn.vtv.service.guest.IShopGuestService;
import hcmute.kltn.vtv.util.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryShopGuestServiceImpl implements ICategoryShopGuestService {

    private final CategoryShopRepository categoryShopRepository;
    private final IShopGuestService shopGuestService;


    @Override
    public CategoryShopResponse getCategoryShopById(Long categoryShopId) {

        checkExistsById(categoryShopId);
        CategoryShop categoryShop = getCategoryShopByCategoryShopId(categoryShopId);
        return CategoryShopResponse.categoryShopResponse(categoryShop, "Lấy thông tin danh mục cửa hàng thành công!", "OK");
    }


    @Override
    public ListCategoryShopResponse getCategoryShopsByShopId(Long shopId) {
        shopGuestService.checkExistsById(shopId);
        List<CategoryShop> categoryShops = getCategoryShopByShopId(shopId);
        return ListCategoryShopResponse.listCategoryShopResponse(categoryShops, "Lấy danh sách danh mục cửa hàng thành công!", "OK");
    }


    @Override
    public void checkExistsById(Long categoryShopId) {
        if (!categoryShopRepository.existsByCategoryShopId(categoryShopId)) {
            throw new NotFoundException("Không tồn tại danh mục cửa hàng có id: " + categoryShopId);
        }
    }


    @Override
    public CategoryShop getCategoryShopByCategoryShopId(Long categoryShopId) {
        return categoryShopRepository.findByCategoryShopId(categoryShopId)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy danh mục cửa hàng có id: " + categoryShopId));
    }


    @Override
    public int countCategoryShopByShopId(Long shopId) {
        return categoryShopRepository.countCategoryShopByShopShopId(shopId);
    }





    private List<CategoryShop> getCategoryShopByShopId(Long shopId) {
        return categoryShopRepository.findAllByShopShopId(shopId)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy danh mục cửa hàng nào!"));
    }
}
