package hcmute.kltn.vtv.service.guest.impl;

import hcmute.kltn.vtv.repository.user.FollowedShopRepository;
import hcmute.kltn.vtv.service.guest.IFollowedGuestService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FollowedGuestServiceImpl implements IFollowedGuestService {

    private final FollowedShopRepository followedShopRepository;

    @Override
    public int countFollowedByShop(Long shopId) {
        return followedShopRepository.countByShopShopId(shopId);
    }

}
