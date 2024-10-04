package hcmute.kltn.vtv.service.vendor.impl;

import hcmute.kltn.vtv.model.data.guest.ResponseClass;
import hcmute.kltn.vtv.model.data.vendor.request.CategoryShopRequest;
import hcmute.kltn.vtv.model.data.vendor.response.CategoryShopResponse;
import hcmute.kltn.vtv.model.data.vendor.response.ListCategoryShopResponse;
import hcmute.kltn.vtv.model.entity.vendor.CategoryShop;
import hcmute.kltn.vtv.model.entity.vendor.Product;
import hcmute.kltn.vtv.model.entity.vendor.Shop;
import hcmute.kltn.vtv.model.extra.Status;
import hcmute.kltn.vtv.repository.vendor.CategoryShopRepository;
import hcmute.kltn.vtv.service.guest.ICategoryShopGuestService;
import hcmute.kltn.vtv.service.vendor.ICategoryShopService;
import hcmute.kltn.vtv.service.vendor.IProductShopService;
import hcmute.kltn.vtv.service.vendor.IShopService;
import hcmute.kltn.vtv.service.vtv.IImageService;
import hcmute.kltn.vtv.util.exception.BadRequestException;
import hcmute.kltn.vtv.util.exception.InternalServerErrorException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryShopServiceImpl implements ICategoryShopService {

    private final CategoryShopRepository categoryShopRepository;
    private final IImageService imageService;
    private final IProductShopService productShopService;
    private final IShopService shopService;
    private final ICategoryShopGuestService categoryShopGuestService;


    @Override
    @Transactional
    public CategoryShopResponse addNewCategoryShop(CategoryShopRequest request, String username) {
        Shop shop = shopService.getShopByUsername(username);
        checkExistsByNameAndCategoryShopIdNot(request.getName(), shop.getShopId());

        CategoryShop categoryShop = createCategoryShopByRequest(request, shop);
        try {
            categoryShopRepository.save(categoryShop);

            return CategoryShopResponse.categoryShopResponse(categoryShop, "Thêm danh mục thành công!", "Success");
        } catch (Exception e) {
            throw new InternalServerErrorException("Thêm danh mục của cửa hàng thất bại! Vui lòng thử lại sau! " + e.getMessage());
        }
    }


    @Override
    @Transactional
    public CategoryShopResponse updateCategoryShop(CategoryShopRequest request, Long categoryShopId, String username) {
        Shop shop = shopService.getShopByUsername(username);
        categoryShopGuestService.checkExistsById(categoryShopId);
        checkExistsByIdAndShopShopId(categoryShopId, shop.getShopId());
        checkExistsByCategoryShopIdNotAndNameAndShopShopId(request.getName(), categoryShopId, shop.getShopId());

        CategoryShop categoryShop = updateCategoryShopByRequest(request, categoryShopId);


        try {
            categoryShopRepository.save(categoryShop);
            return CategoryShopResponse.categoryShopResponse(categoryShop, "Cập nhật danh mục thành công!", "Success");
        } catch (Exception e) {
            throw new InternalServerErrorException("Cập nhật danh mục cửa hàng thất bại! Vui lòng thử lại sau!");
        }
    }


    @Override
    public ListCategoryShopResponse getAllByUsername(String username) {
        Shop shop = shopService.getShopByUsername(username);
        return categoryShopGuestService.getCategoryShopsByShopId(shop.getShopId());
    }


    @Override
    @Transactional
    public CategoryShopResponse addProductToCategoryShop(Long categoryShopId, List<Long> productIds, String username) {
        checkProductWithShop(username, categoryShopId, productIds);

        CategoryShop categoryShop = updateProductToCategoryShop(categoryShopId, productIds);
        try {
            categoryShopRepository.save(categoryShop);

            return CategoryShopResponse.categoryShopResponse(categoryShop, "Thêm sản phẩm vào danh mục của hàng thành công!", "Success");
        } catch (Exception e) {
            throw new InternalServerErrorException("Thêm sản phẩm vào danh mục thất bại! Vui lòng thử lại sau!");
        }
    }


    @Override
    @Transactional
    public CategoryShopResponse removeProductFromCategoryShop(Long categoryShopId, List<Long> productIds, String username) {
        checkProductWithShop(username, categoryShopId, productIds);
        CategoryShop categoryShop = updateRemoveProductToCategoryShop(categoryShopId, productIds);
        try {
            categoryShopRepository.save(categoryShop);

            return CategoryShopResponse.categoryShopResponse(categoryShop, "Xóa sản phẩm khỏi danh mục của hàng thành công!", "Success");
        } catch (Exception e) {
            throw new InternalServerErrorException("Xóa sản phẩm khỏi danh mục thất bại! Vui lòng thử lại sau!");
        }
    }


    @Override
    @Transactional
    public ResponseClass deleteCategoryShopById(Long categoryShopId, String username) {
        Shop shop = shopService.getShopByUsername(username);
        categoryShopGuestService.checkExistsById(categoryShopId);
        checkExistsByIdAndShopShopId(categoryShopId, shop.getShopId());
        CategoryShop categoryShop = categoryShopGuestService.getCategoryShopByCategoryShopId(categoryShopId);
        String message = "Xóa danh mục cửa hàng thành: " + categoryShop.getName() + " thành công!";
        String image = categoryShop.getImage();
        try {
            categoryShop.getProducts().clear();
            categoryShopRepository.delete(categoryShop);
            imageService.deleteImageInFirebase(image);
            return ResponseClass.responseClass(message, "Success");
        } catch (Exception e) {
            throw new InternalServerErrorException("Xóa danh mục cửa hàng thất bại! Vui lòng thử lại sau!");
        }
    }



    private CategoryShop updateRemoveProductToCategoryShop(Long categoryShopId, List<Long> productIds) {
        CategoryShop categoryShop = categoryShopGuestService.getCategoryShopByCategoryShopId(categoryShopId);
        List<Product> products = categoryShop.getProducts();
        if (products.isEmpty()) {
            throw new BadRequestException("Danh mục cửa hàng không có sản phẩm nào!");
        }

        categoryShop.setProducts(checkProductWithCategoryShopAndDelete(products, productIds));
        categoryShop.setUpdateAt(LocalDateTime.now());
        return categoryShop;
    }


    private CategoryShop updateProductToCategoryShop(Long categoryShopId, List<Long> productIds) {
        CategoryShop categoryShop = categoryShopGuestService.getCategoryShopByCategoryShopId(categoryShopId);
        List<Product> products = categoryShop.getProducts();
        checkExistsProductInCategoryShop(products, productIds);
        categoryShop.setProducts(productShopService.getProductsByProductIds(productIds));
        categoryShop.setUpdateAt(LocalDateTime.now());
        return categoryShop;
    }


    private CategoryShop updateCategoryShopByRequest(CategoryShopRequest request, Long categoryShopId) {
        CategoryShop categoryShop = categoryShopGuestService.getCategoryShopByCategoryShopId(categoryShopId);

        categoryShop.setName(request.getName());
        categoryShop.setUpdateAt(LocalDateTime.now());
        if (request.isChangeImage()) {
            imageService.deleteImageInFirebase(categoryShop.getImage());
            categoryShop.setImage(imageService.uploadImageToFirebase(request.getImage()));
        }
        return categoryShop;
    }


    private List<Product>  checkProductWithCategoryShopAndDelete(List<Product> products, List<Long> productIds) {
        List<Product> newProducts = new ArrayList<>();

        List<Product> deleteProducts = productShopService.getProductsByProductIds(productIds);

        for (Product product : deleteProducts) {
            if (!products.contains(product)) {
                throw new BadRequestException("Sản phẩm cấn xóa khỏi danh mục cửa hàng không tồn tại trong danh mục cửa hàng! " + product.getProductId());
            }
        }

        for (Product product : products) {
            if (!productIds.contains(product.getProductId())) {
                newProducts.add(product);
            }
        }
        return newProducts;
    }





    private void checkProductWithShop(String username, Long categoryShopId, List<Long> productIds) {
        Shop shop = shopService.getShopByUsername(username);
        categoryShopGuestService.checkExistsById(categoryShopId);
        checkExistsByIdAndShopShopId(categoryShopId, shop.getShopId());
        productShopService.checkProductIdsInShop(productIds, shop.getShopId());
    }


    private void checkExistsProductInCategoryShop(List<Product> products, List<Long> productIds) {
        if (products.isEmpty()) {
            return;
        }
        for (Product product : products) {
           if (!productIds.contains(product.getProductId())) {
               productIds.add(product.getProductId());
           }
        }

    }


    private void checkExistsByIdAndShopShopId(Long categoryShopId, Long shopId) {
        if (!categoryShopRepository.existsByCategoryShopIdAndShopShopId(categoryShopId, shopId)) {
            throw new BadRequestException("Không tồn tại danh mục cửa hàng có id: " + categoryShopId + " trong cửa hàng!");
        }
    }

    private void checkExistsByNameAndCategoryShopIdNot(String name, Long shopId) {
        if (categoryShopRepository.existsByNameAndCategoryShopIdNot(name, shopId)) {
            throw new BadRequestException("Tên danh mục đã tồn tại trong cửa hàng!");
        }
    }



    private void checkExistsByCategoryShopIdNotAndNameAndShopShopId(String name, Long categoryShopId, Long shopId) {
        if (categoryShopRepository.existsByCategoryShopIdNotAndNameAndShopShopId(categoryShopId, name, shopId)) {
            throw new BadRequestException("Tên danh mục đã tồn tại trong cửa hàng!");
        }
    }


    private CategoryShop createCategoryShopByRequest(CategoryShopRequest categoryShopRequest, Shop shop) {
        try {
            CategoryShop categoryShop = new CategoryShop();
            categoryShop.setName(categoryShopRequest.getName());
            categoryShop.setImage(imageService.uploadImageToFirebase(categoryShopRequest.getImage()));
            categoryShop.setStatus(Status.ACTIVE);
            categoryShop.setCreateAt(LocalDateTime.now());
            categoryShop.setUpdateAt(LocalDateTime.now());
            categoryShop.setShop(shop);
            categoryShop.setProducts(new ArrayList<>());

            return categoryShop;
        } catch (Exception e) {
            throw new InternalServerErrorException("Tạo danh mục cửa hàng thất bại! Vui lòng thử lại sau!");
        }
    }
}
