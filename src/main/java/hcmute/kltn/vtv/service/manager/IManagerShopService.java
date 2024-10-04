package hcmute.kltn.vtv.service.manager;

import hcmute.kltn.vtv.model.data.manager.response.ManagerShopPageResponse;
import hcmute.kltn.vtv.model.data.manager.response.ManagerShopResponse;
import hcmute.kltn.vtv.model.data.manager.response.ShopsResponse;
import hcmute.kltn.vtv.model.data.vendor.response.ShopResponse;
import hcmute.kltn.vtv.model.extra.Status;
import org.springframework.transaction.annotation.Transactional;

public interface IManagerShopService {
    ShopResponse getShopById(Long id);

    ShopsResponse getShopsByStatus(int page, int size, Status status);

    ShopsResponse getShopsByNameAndStatus(int page, int size, String name, Status status);

    @Transactional
    ManagerShopResponse lockShopById(Long shopId, String username, String note);

    @Transactional
    ManagerShopResponse unlockShopById(Long shopId, String username, String note);

    ManagerShopPageResponse getManagerShopPageByLock(int size, int page, boolean lock);

    ManagerShopPageResponse getManagerShopPageByNameAndLock(int size, int page, String name, boolean lock);

}
