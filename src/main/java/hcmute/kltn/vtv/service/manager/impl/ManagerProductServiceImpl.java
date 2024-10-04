package hcmute.kltn.vtv.service.manager.impl;

import hcmute.kltn.vtv.util.exception.BadRequestException;
import hcmute.kltn.vtv.model.data.manager.response.ManagerProductPageResponse;
import hcmute.kltn.vtv.model.data.manager.response.ManagerProductResponse;
import hcmute.kltn.vtv.model.entity.user.Customer;
import hcmute.kltn.vtv.model.entity.vendor.Product;
import hcmute.kltn.vtv.model.entity.vendor.ProductVariant;
import hcmute.kltn.vtv.model.entity.manager.ManagerProduct;
import hcmute.kltn.vtv.model.extra.Status;
import hcmute.kltn.vtv.repository.vendor.ProductRepository;
import hcmute.kltn.vtv.repository.vendor.ProductVariantRepository;
import hcmute.kltn.vtv.repository.manager.ManagerProductRepository;
import hcmute.kltn.vtv.service.manager.IManagerProductService;
import hcmute.kltn.vtv.service.user.ICustomerService;
import hcmute.kltn.vtv.util.exception.InternalServerErrorException;
import hcmute.kltn.vtv.util.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ManagerProductServiceImpl implements IManagerProductService {

    private final ManagerProductRepository managerProductRepository;
    private final ProductRepository productRepository;
    private final ICustomerService customerService;
    private final ProductVariantRepository productVariantRepository;


    @Override
    @Transactional
    public ManagerProductResponse lockProductByProductId(Long productId, String username, String note) {
        Customer manager = customerService.getCustomerByUsername(username);
        Product product = checkProductByProductId(productId);

        ManagerProduct managerProduct;
        if (managerProductRepository.existsByProductProductIdAndLock(productId, true)) {
            throw new BadRequestException("Sản phẩm đã bị khóa bởi bạn");
        } else if (managerProductRepository.existsByProduct_ProductId(productId)) {
            managerProduct = updateManagerProduct(productId, manager, note, true, false);
        } else {
            managerProduct = createManagerProduct(product, manager, note);
        }
        try {
            updateStatusProductByStatusAndStatusEqual(product, Status.LOCKED, Status.ACTIVE);
            managerProductRepository.save(managerProduct);

            String successMessage = String.format("Khóa sản phẩm thành công: %s của cửa háng: %s bàng mã: %s với nguyên nhân: %s thành công",
                    product.getName(), product.getShop().getName(), product.getProductId(), note);
            return ManagerProductResponse.managerProductResponse(managerProduct, successMessage, "Success");
        } catch (Exception e) {
            throw new InternalServerErrorException("Khóa sản phẩm thất bại! " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public ManagerProductResponse unLockProductByProductId(Long productId, String username, String note) {
        Product product = checkProductByProductId(productId);
        Customer manager = customerService.getCustomerByUsername(username);

        ManagerProduct managerProduct;
        if (!managerProductRepository.existsByProduct_ProductId(productId) && !managerProductRepository.existsByProductProductIdAndLock(productId, true)) {
            throw new BadRequestException("Sản phẩm chưa bị khóa");
        }
        managerProduct = updateManagerProduct(productId, manager, note, false, true);

        try {
            updateStatusProductByStatusAndStatusEqual(product, Status.ACTIVE, Status.LOCKED);
            managerProductRepository.save(managerProduct);

            String message = "Mở khóa sản phẩm thành công: " + product.getName() + " của cửa háng: " + product.getShop().getName() + " bàng mã: " + product.getProductId() + " với nguyên nhân: " + note + " thành công";

            return ManagerProductResponse.managerProductResponse(managerProduct, message, "Success");
        } catch (Exception e) {
            throw new BadRequestException("Mở khóa sản phẩm thất bại");
        }
    }


    @Override
    public ManagerProductPageResponse getManagerProductPage(int page, int size) {
        Page<ManagerProduct> managerProducts = managerProductRepository
                .findAllByLock(true, PageRequest.of(page - 1, size))
                .orElseThrow(() -> new BadRequestException("Không tìm thấy danh sách sản phẩm đã khóa"));
        String message = "Lấy danh sách sản phẩm đã khóa thành công";

        return ManagerProductPageResponse.managerProductPageResponse(managerProducts, message, "OK");
    }


    @Override
    public boolean checkExistProductUseCategory(Long categoryId) {
        return productRepository.existsByCategoryCategoryId(categoryId);
    }


    @Override
    public ManagerProductPageResponse getManagerProductPageByProductName(int page, int size, String productName) {

        Page<ManagerProduct> managerProducts = managerProductRepository
                .findAllByLockAndProductNameContains(true, productName, PageRequest.of(page - 1, size))
                .orElseThrow(() -> new BadRequestException("Không tìm thấy danh sách sản phẩm đã khóa"));
        String message = "Lấy danh sách sản phẩm đã khóa thành công";

        return ManagerProductPageResponse.managerProductPageResponse(managerProducts, message, "OK");
    }


    @Async
    @Override
    @Transactional
    public void lockProductsByShopId(Long shopId, String username, String note) {
        List<Product> products = productRepository.findByShopShopIdAndStatus(shopId, Status.ACTIVE)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy sản phẩm của cửa hàng có mã: " + shopId));
        Customer manager = customerService.getCustomerByUsername(username);

        for (Product product : products) {
            ManagerProduct managerProduct;
            if (managerProductRepository.existsByProductProductIdAndLock(product.getProductId(), true)) {
                throw new BadRequestException("Sản phẩm đã bị khóa bởi bạn");
            } else if (managerProductRepository.existsByProduct_ProductId(product.getProductId())) {
                managerProduct = updateManagerProduct(product.getProductId(), manager, note, true, false);
            } else {
                managerProduct = createManagerProduct(product, manager, note);
            }
            try {
                updateStatusProductByStatusAndStatusEqual(product, Status.LOCKED, Status.ACTIVE);
                managerProductRepository.save(managerProduct);
            } catch (Exception e) {
                throw new InternalServerErrorException("Khóa sản phẩm: " + product.getName() + " thất bại! " + e.getMessage());
            }
        }
    }


    @Async
    @Override
    @Transactional
    public void unlockProductsByShopId(Long shopId, String username, String note) {
        List<Product> products = productRepository.findByShopShopIdAndStatus(shopId, Status.LOCKED)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy sản phẩm của cửa hàng có mã: " + shopId));
        Customer manager = customerService.getCustomerByUsername(username);

        for (Product product : products) {
            ManagerProduct managerProduct;
            if (!managerProductRepository.existsByProduct_ProductId(product.getProductId()) && !managerProductRepository.existsByProductProductIdAndLock(product.getProductId(), true)) {
                throw new BadRequestException("Sản phẩm chưa bị khóa");
            }
            managerProduct = updateManagerProduct(product.getProductId(), manager, note, false, true);

            try {
                updateStatusProductByStatusAndStatusEqual(product, Status.ACTIVE, Status.LOCKED);
                managerProductRepository.save(managerProduct);
            } catch (Exception e) {
                throw new InternalServerErrorException("Mở khóa sản phẩm: " + product.getName() + " thất bại! " + e.getMessage());
            }
        }
    }


//    @Override
//    public void checkRequestPageParams(int page, int size) {
//        if (page < 0) {
//            throw new NotFoundException("Trang không được nhỏ hơn 0!");
//        }
//        if (size < 0) {
//            throw new NotFoundException("Kích thước trang không được nhỏ hơn 0!");
//        }
//        if (size > 500) {
//            throw new NotFoundException("Kích thước trang không được lớn hơn 200!");
//        }
//    }


    private ManagerProduct updateManagerProduct(Long productId, Customer manager, String note, boolean lock, boolean delete) {
        ManagerProduct managerProduct = managerProductRepository.findByProduct_ProductId(productId)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy sản phẩm có mã: " + productId));
        managerProduct.setManager(manager);
        managerProduct.setLock(lock);
        managerProduct.setDelete(delete);
        managerProduct.setNote(note);
        managerProduct.setUpdateAt(LocalDateTime.now());

        return managerProduct;
    }


    private ManagerProduct createManagerProduct(Product product, Customer manager, String note) {
        ManagerProduct managerProduct = new ManagerProduct();
        managerProduct.setProduct(product);
        managerProduct.setManager(manager);
        managerProduct.setCreateAt(LocalDateTime.now());
        managerProduct.setUpdateAt(LocalDateTime.now());
        managerProduct.setLock(true);
        managerProduct.setDelete(false);
        managerProduct.setNote(note);

        return managerProduct;
    }


    public ManagerProduct checkManagerProduct(Long productId, String username) {
        ManagerProduct managerProduct = managerProductRepository.findByProduct_ProductId(productId)
                .orElseThrow(() -> new BadRequestException("Không tìm thấy sản phẩm"));
        if (!managerProduct.getManager().getUsername().equals(username)) {
            throw new BadRequestException("Bạn không có quyền thực hiện hành động này");
        }
        if (!managerProduct.isLock()) {
            throw new BadRequestException("Sản phẩm chưa bị khóa");
        }

        return managerProduct;
    }


    @Transactional
    public void updateStatusProductByStatusAndStatusEqual(Product product, Status status, Status statusEqual) {
        if (product.getStatus().equals(statusEqual)) {
            product.setStatus(status);
            product.setUpdateAt(LocalDateTime.now());
            try {
                productRepository.save(product);
                updateStatusProductVariantsByStatusAndStatusEqual(product.getProductVariants(), status, statusEqual);
            } catch (Exception e) {
                throw new BadRequestException("Cập nhật trạng thái sản phẩm thất bại");
            }
        }
    }


    @Transactional
    public void updateStatusProductVariantsByStatusAndStatusEqual(List<ProductVariant> productVariants, Status status, Status statusEqual) {
        for (ProductVariant productVariant : productVariants) {
            if (productVariant.getStatus().equals(statusEqual)) {
                productVariant.setStatus(status);
                productVariant.setUpdateAt(LocalDateTime.now());
                try {
                    productVariantRepository.save(productVariant);
                } catch (Exception e) {
                    throw new IllegalArgumentException("Cập nhật trạng thái biến thể sản phẩm thất bại ở biến thể: " + productVariant.getProductVariantId());
                }
            }
        }
    }


    private Product checkProductByProductId(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy sản phẩm có mã: " + productId));

        if (product.getStatus().equals(Status.DELETED)) {
            throw new BadRequestException("Sản phẩm đã bị xóa");
        }

        return product;
    }


}
