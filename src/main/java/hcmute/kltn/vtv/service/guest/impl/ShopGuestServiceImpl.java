package hcmute.kltn.vtv.service.guest.impl;

import hcmute.kltn.vtv.model.data.guest.ShopDetailResponse;
import hcmute.kltn.vtv.model.entity.vendor.Shop;
import hcmute.kltn.vtv.model.extra.Status;
import hcmute.kltn.vtv.repository.vendor.CategoryShopRepository;
import hcmute.kltn.vtv.repository.vtv.ShopRepository;
import hcmute.kltn.vtv.service.guest.*;
import hcmute.kltn.vtv.util.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ShopGuestServiceImpl implements IShopGuestService {

    private final ShopRepository shopRepository;
    private final IFollowedGuestService followedGuestService;
    private final IProductService productService;
    private final IReviewService reviewService;
    private final CategoryShopRepository categoryShopRepository;


    @Override
    public ShopDetailResponse getShopDetailByShopId(Long shopId) {
        Shop shop = getShopById(shopId);
        int countFollowed = followedGuestService.countFollowedByShop(shopId);
        int countProduct = productService.countProductByShopId(shopId);
        int countCategoryShop = countCategoryShopByShopId(shopId);
        float averageRating = reviewService.averageRatingByShopId(shopId);

        return ShopDetailResponse.shopDetailResponse(shop, countFollowed, countProduct, countCategoryShop, averageRating,
                "Lấy thông tin chi tiết cửa hàng thành công!", "OK");
    }


    @Override
    public ShopDetailResponse getShopDetailByUsername(String username) {
        Shop shop = shopRepository.findByCustomerUsername(username)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy cửa hàng theo tên đăng nhập!"));
        int countFollowed = followedGuestService.countFollowedByShop(shop.getShopId());
        int countProduct = productService.countProductByShopId(shop.getShopId());
        int countCategoryShop = countCategoryShopByShopId(shop.getShopId());
        float averageRating = reviewService.averageRatingByShopId(shop.getShopId());

        return ShopDetailResponse.shopDetailResponse(shop, countFollowed, countProduct, countCategoryShop, averageRating,
                "Lấy thông tin chi tiết cửa hàng thành công!", "OK");
    }


    @Override
    public Shop getShopById(Long shopId) {
        checkExistsById(shopId);

        Shop shop =  shopRepository.findById(shopId)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy cửa hàng theo mã cửa hàng!"));
        if (shop.getStatus().equals(Status.DELETED)) {
            throw new NotFoundException("Cửa hàng đã bị xóa!");
        }
        if (shop.getStatus().equals(Status.LOCKED)) {
            throw new NotFoundException("Cửa hàng đã bị khóa!");
        }

        return shop;
    }


    @Override
    public void checkExistsById(Long shopId) {
        if (!shopRepository.existsById(shopId)) {
            throw new NotFoundException("Không tìm thấy cửa hàng có mã: " + shopId);
        }
    }


    private int countCategoryShopByShopId(Long shopId) {
        return categoryShopRepository.countCategoryShopByShopShopId(shopId);
    }
}
