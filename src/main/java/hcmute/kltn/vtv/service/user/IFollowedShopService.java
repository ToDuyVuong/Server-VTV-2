package hcmute.kltn.vtv.service.user;

import hcmute.kltn.vtv.model.data.user.response.FollowedShopResponse;
import hcmute.kltn.vtv.model.data.user.response.ListFollowedShopResponse;
import org.springframework.transaction.annotation.Transactional;

public interface IFollowedShopService {

    @Transactional
    FollowedShopResponse addNewFollowedShop(Long shopId, String username);

    ListFollowedShopResponse getListFollowedShopByUsername(String username);

    @Transactional
    FollowedShopResponse deleteFollowedShop(Long followedShopId, String username);
}
