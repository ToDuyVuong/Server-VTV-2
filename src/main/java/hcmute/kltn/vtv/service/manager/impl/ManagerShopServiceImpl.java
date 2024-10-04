package hcmute.kltn.vtv.service.manager.impl;

import hcmute.kltn.vtv.model.data.manager.response.ShopsResponse;
import hcmute.kltn.vtv.model.data.vendor.response.ShopResponse;
import hcmute.kltn.vtv.model.entity.manager.ManagerShop;
import hcmute.kltn.vtv.model.entity.user.Customer;
import hcmute.kltn.vtv.util.exception.BadRequestException;
import hcmute.kltn.vtv.model.data.manager.response.ManagerShopPageResponse;
import hcmute.kltn.vtv.model.data.manager.response.ManagerShopResponse;
import hcmute.kltn.vtv.model.entity.vendor.Shop;
import hcmute.kltn.vtv.model.extra.Status;
import hcmute.kltn.vtv.repository.vtv.CategoryRepository;
import hcmute.kltn.vtv.repository.vtv.ShopRepository;
import hcmute.kltn.vtv.repository.manager.ManagerShopRepository;
import hcmute.kltn.vtv.service.manager.IManagerProductService;
import hcmute.kltn.vtv.service.manager.IManagerShopService;
import hcmute.kltn.vtv.service.user.ICustomerService;
import hcmute.kltn.vtv.util.exception.InternalServerErrorException;
import hcmute.kltn.vtv.util.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ManagerShopServiceImpl implements IManagerShopService {

    private final ShopRepository shopRepository;
    private final ICustomerService customerService;
    private final ManagerShopRepository managerShopRepository;
    private final IManagerProductService managerProductService;
    private final CategoryRepository categoryRepository;

    @Override
    public ShopResponse getShopById(Long id) {
        Shop shop = shopRepository.findById(id).orElseThrow(() -> new NotFoundException("Không tìm thấy cửa hàng!"));

        return ShopResponse.shopResponse(shop, "Lấy thông tin quản lý cửa hàng thành công!", "OK");
    }


    @Override
    public ShopsResponse getShopsByStatus(int page, int size, Status status) {
        Page<Shop> shops = shopRepository.findAllByStatusOrderByName(status, PageRequest.of(page - 1, size))
                .orElseThrow(() -> new NotFoundException("Không tìm thấy cửa hàng!"));
        String message = "Lấy danh sách cửa hàng theo trạng thái " + messageByStatus(status) + " thành công!";

        return ShopsResponse.shopsResponse(shops, message, page, size);
    }


    @Override
    public ShopsResponse getShopsByNameAndStatus(int page, int size, String name, Status status) {
        Page<Shop> shops = shopRepository.findAllByNameContainsAndStatusOrderByName(name, status, PageRequest.of(page - 1, size))
                .orElseThrow(() -> new NotFoundException("Không tìm thấy cửa hàng!"));
        String message = "Lấy danh sách cửa hàng theo tên và trạng thái " + messageByStatus(status) + " thành công!";

        return ShopsResponse.shopsResponse(shops, message, page, size);
    }


    @Override
    @Transactional
    public ManagerShopResponse lockShopById(Long shopId, String username, String note) {
        Customer customer = customerService.getCustomerByUsername(username);
        Shop shop = checkShopLock(shopId);

        ManagerShop managerShop;
        if (managerShopRepository.existsByShopShopIdAndLock(shopId, true)) {
            throw new BadRequestException("Cửa hàng đã bị khóa!");
        } else if (managerShopRepository.existsByShopShopId(shopId)) {
            managerShop = updateManagerShop(shopId, customer, note, true, false);
        } else {
            managerShop = createManagerShop(shop, customer, note);
        }

        try {
            managerShopRepository.save(managerShop);
            managerProductService.lockProductsByShopId(shopId, username, note);
            updateStatusShopByManager(shop, true);
            return ManagerShopResponse.managerShopResponse(shop, managerShop, "Khóa cửa hàng thành công!", "OK");
        } catch (Exception e) {
            throw new InternalServerErrorException("Khóa cửa hàng thất bại! " + e.getMessage());
        }
    }


    @Override
    @Transactional
    public ManagerShopResponse unlockShopById(Long shopId, String username, String note) {
        Customer customer = customerService.getCustomerByUsername(username);
        Shop shop = checkShopLock(shopId);
        checkExistManagerShopByShopId(shopId);
        ManagerShop managerShop = updateManagerShop(shopId, customer, note, false, false);

        try {
            managerShopRepository.save(managerShop);
            managerProductService.unlockProductsByShopId(shopId, username, note);
            updateStatusShopByManager(shop, false);

            return ManagerShopResponse.managerShopResponse(shop, managerShop, "Mở khóa cửa hàng thành công!", "OK");
        } catch (Exception e) {
            throw new InternalServerErrorException("Mở khóa cửa hàng thất bại! " + e.getMessage());
        }
    }


    @Override
    public ManagerShopPageResponse getManagerShopPageByLock(int size, int page, boolean lock) {

        Page<ManagerShop> managerShops = managerShopRepository.findAllByLock(lock, PageRequest.of(page - 1, size))
                .orElseThrow(() -> new NotFoundException("Không tìm thấy danh sách quản lý cửa hàng theo trạng thái " + (lock ? "khóa" : "mở")));


        return ManagerShopPageResponse.managerShopPageResponse(managerShops, "Lấy danh sách cửa hàng theo trạng thái thành công!");
    }

    @Override
    public ManagerShopPageResponse getManagerShopPageByNameAndLock(int size, int page, String name, boolean lock) {

        Page<ManagerShop> managerShops = managerShopRepository.findAllByLockAndShopNameContains(lock, name, PageRequest.of(page - 1, size))
                .orElseThrow(() -> new NotFoundException("Không tìm thấy danh sách quản lý cửa hàng theo tên và trạng thái " + (lock ? "khóa" : "mở")));

        return ManagerShopPageResponse.managerShopPageResponse(managerShops, "Lấy danh sách cửa hàng theo tên thành công!");
    }


    private void updateStatusShopByManager(Shop shop, boolean lock) {
        shop.setStatus(lock ? Status.LOCKED : Status.ACTIVE);
        shop.setUpdateAt(LocalDateTime.now());
        try {
            shopRepository.save(shop);
        } catch (Exception e) {
            throw new InternalServerErrorException("Cập nhật trạng thái : " + (lock ? "khóa" : "mở") + " cửa hàng thất bại! " + e.getMessage());
        }
    }


    private ManagerShop createManagerShop(Shop shop, Customer customer, String note) {
        ManagerShop managerShop = new ManagerShop();
        managerShop.setShop(shop);
        managerShop.setManager(customer);
        managerShop.setLock(true);
        managerShop.setDelete(false);
        managerShop.setNote(note);
        managerShop.setCreateAt(LocalDateTime.now());
        managerShop.setUpdateAt(LocalDateTime.now());

        return managerShop;
    }


    private ManagerShop updateManagerShop(Long shopId, Customer customer, String note, boolean lock, boolean delete) {
        ManagerShop managerShop = managerShopRepository.findByShop_ShopId(shopId)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy cửa hàng!"));
        managerShop.setManager(customer);
        managerShop.setLock(lock);
        managerShop.setDelete(delete);
        managerShop.setNote(note);

        return managerShop;
    }


    private Shop checkShopLock(Long shopId) {
        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy cửa hàng!"));
        if (shop.getStatus().equals(Status.DELETED)) {
            throw new NotFoundException("Cửa hàng đã bị xóa!");
        }

        return shop;
    }


    private String messageByStatus(Status status) {
        if (status.equals(Status.ACTIVE)) {
            return "hoạt động";
        } else if (status.equals(Status.INACTIVE)) {
            return "ngưng hoạt động";
        } else if (status.equals(Status.LOCKED)) {
            return "bị khóa";
        } else {
            return "đã xóa";
        }
    }

    private void checkExistManagerShopByShopId(Long shopId) {
        if (!managerShopRepository.existsByShopShopId(shopId)) {
            throw new BadRequestException("Cửa hàng chưa được quản lý!");
        }

        if (managerShopRepository.existsByShopShopIdAndLock(shopId, false)) {
            throw new BadRequestException("Cửa hàng chưa bị khóa!");
        }
    }


}
