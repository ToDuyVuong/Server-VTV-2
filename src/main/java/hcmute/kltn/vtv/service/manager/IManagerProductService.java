package hcmute.kltn.vtv.service.manager;

import hcmute.kltn.vtv.model.data.manager.response.ManagerProductPageResponse;
import hcmute.kltn.vtv.model.data.manager.response.ManagerProductResponse;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Transactional;

public interface IManagerProductService {

    @Transactional
    ManagerProductResponse lockProductByProductId(Long productId, String username, String note);

    @Transactional
    ManagerProductResponse unLockProductByProductId(Long productId, String username, String note);

    ManagerProductPageResponse getManagerProductPage(int page, int size);

    boolean checkExistProductUseCategory(Long categoryId);

    ManagerProductPageResponse getManagerProductPageByProductName(int page, int size, String productName);

    @Async
    @Transactional
    void lockProductsByShopId(Long shopId, String username, String note);

    @Async
    @Transactional
    void unlockProductsByShopId(Long shopId, String username, String note);

//    void checkRequestPageParams(int page, int size);
}
