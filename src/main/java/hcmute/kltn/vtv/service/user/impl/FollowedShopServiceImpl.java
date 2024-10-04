package hcmute.kltn.vtv.service.user.impl;

import hcmute.kltn.vtv.util.exception.BadRequestException;
import hcmute.kltn.vtv.model.data.user.response.FollowedShopResponse;
import hcmute.kltn.vtv.model.data.user.response.ListFollowedShopResponse;
import hcmute.kltn.vtv.model.dto.user.FollowedShopDTO;
import hcmute.kltn.vtv.model.entity.user.Customer;
import hcmute.kltn.vtv.model.entity.user.FollowedShop;
import hcmute.kltn.vtv.model.entity.vendor.Shop;
import hcmute.kltn.vtv.repository.user.FollowedShopRepository;
import hcmute.kltn.vtv.repository.vtv.ShopRepository;
import hcmute.kltn.vtv.service.user.ICustomerService;
import hcmute.kltn.vtv.service.user.IFollowedShopService;
import hcmute.kltn.vtv.util.exception.InternalServerErrorException;
import hcmute.kltn.vtv.util.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FollowedShopServiceImpl implements IFollowedShopService {

    private final ICustomerService customerService;
    private final ShopRepository shopRepository;
    private final FollowedShopRepository followedShopRepository;

    @Override
    @Transactional
    public FollowedShopResponse addNewFollowedShop(Long shopId, String username) {
        if (followedShopRepository.existsByCustomerUsernameAndShopShopId(username, shopId)) {
            throw new BadRequestException("Bạn đã theo dõi cửa hàng này!");
        }
        FollowedShop followedShop = createFollowedShopByShopIdAndUsername(shopId, username);

        try {
            followedShopRepository.save(followedShop);

            return FollowedShopResponse.followedShopResponse(followedShop, "Theo dõi cửa hàng thành công!", "Success");
        } catch (Exception e) {
            throw new InternalServerErrorException("Lỗi theo dõi cửa hàng!");
        }
    }



    @Override
    public ListFollowedShopResponse getListFollowedShopByUsername(String username) {
        List<FollowedShop> followedShops = followedShopRepository.findAllByCustomerUsername(username)
                .orElseThrow(() -> new NotFoundException("Không có cửa hàng nào được theo dõi!"));

        return ListFollowedShopResponse.listFollowedShopResponse(followedShops,
                "Lấy danh sách cửa hàng theo dõi thành công!", "OK");
    }


    @Override
    @Transactional
    public FollowedShopResponse deleteFollowedShop(Long followedShopId, String username) {
        FollowedShop followedShop = followedShopRepository.findById(followedShopId)
                .orElseThrow(() -> new NotFoundException("Theo dõi cửa hàng không tồn tại!"));
        if (!followedShop.getCustomer().getUsername().equals(username)) {
            throw new BadRequestException("Bạn không có quyền xóa theo dõi cửa hàng này!");
        }

        try {
            followedShopRepository.delete(followedShop);

            return FollowedShopResponse.followedShopResponse(followedShop, "Xóa theo dõi cửa hàng thành công!", "Success");
        } catch (Exception e) {
            throw new InternalServerErrorException("Lỗi xóa theo dõi cửa hàng!");
        }
    }


    private FollowedShop createFollowedShopByShopIdAndUsername(Long shopId, String username) {
        FollowedShop followedShop = new FollowedShop();
        followedShop.setCustomer(customerService.getCustomerByUsername(username));
        followedShop.setShop(shopRepository.findById(shopId)
                .orElseThrow(() -> new NotFoundException("Cửa hàng không tồn tại!")));
        followedShop.setCreateAt(LocalDateTime.now());

        return followedShop;
    }

}
