package hcmute.kltn.vtv.service.vendor.impl;

import hcmute.kltn.vtv.model.data.vendor.request.ChangePriceProductsByPercentRequest;
import hcmute.kltn.vtv.model.data.vendor.request.ChangePriceProductsRequest;
import hcmute.kltn.vtv.model.data.vendor.response.ListProductResponse;
import hcmute.kltn.vtv.model.data.vendor.response.ProductPageResponse;
import hcmute.kltn.vtv.model.data.vendor.request.ProductRequest;
import hcmute.kltn.vtv.model.entity.vendor.Product;
import hcmute.kltn.vtv.model.entity.vendor.ProductVariant;
import hcmute.kltn.vtv.model.entity.vendor.Shop;
import hcmute.kltn.vtv.model.extra.OrderStatus;
import hcmute.kltn.vtv.repository.vtv.BrandRepository;
import hcmute.kltn.vtv.repository.vendor.ProductRepository;
import hcmute.kltn.vtv.service.guest.ICategoryService;
import hcmute.kltn.vtv.service.vtv.IImageService;
import hcmute.kltn.vtv.util.exception.BadRequestException;
import hcmute.kltn.vtv.model.data.guest.ProductResponse;
import hcmute.kltn.vtv.model.entity.vtv.*;
import hcmute.kltn.vtv.model.extra.Status;
import hcmute.kltn.vtv.service.vendor.IProductShopService;
import hcmute.kltn.vtv.service.vendor.IProductVariantShopService;
import hcmute.kltn.vtv.service.vendor.IShopService;
import hcmute.kltn.vtv.util.exception.InternalServerErrorException;
import hcmute.kltn.vtv.util.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductShopServiceImpl implements IProductShopService {


    private final IShopService shopService;
    private final BrandRepository brandRepository;
    private final ICategoryService categoryService;
    private final ProductRepository productRepository;
    private final IProductVariantShopService productVariantShopService;
    private final IImageService imageService;


    @Override
    @Transactional
    public ProductResponse addNewProduct(ProductRequest request, String username) {
        checkExistProductByName(request.getName());
        Category category = categoryService.getCategoryById(request.getCategoryId());
        categoryService.checkExistCategoryHasChild(category.getCategoryId());
        Brand brand = checkBrand(request.getBrandId());
        Shop shop = shopService.getShopByUsername(username);
        Product product = createProductByRequest(request, category, brand, shop);

        try {
            productRepository.save(product);
            List<ProductVariant> productVariants = productVariantShopService.addNewProductVariants(request.getProductVariantRequests(), product);
            product.setProductVariants(productVariants);
//            int countOrder = productRepository.countOrdersByProductId(product.getProductId(), OrderStatus.COMPLETED.toString());

            return ProductResponse.productResponse(product, 0, "Thêm sản phẩm mới trong cửa hàng thành công!", "Success");
        } catch (Exception e) {
            imageService.deleteImageInFirebase(product.getImage());
            throw new InternalServerErrorException("Thêm sản phẩm mới trong cửa hàng thất bại! " + e.getMessage());
        }
    }


    @Override
    @Transactional
    public ProductResponse updateProduct(ProductRequest request, String username) {
        checkExistProductByProductIdAndUsername(request.getProductId(), username);
        checkUpdateProductExistByName(request.getName(), request.getProductId());
        Product product = getProductByProductId(request.getProductId());
        Category category = categoryService.getCategoryById(request.getCategoryId());
        updateProductByRequest(product, request, category, checkBrand(request.getBrandId()));

        try {
            productRepository.save(product);
            List<ProductVariant> productVariants = productVariantShopService.updateProductVariants(request.getProductVariantRequests(), product);
            product.setProductVariants(productVariants);

            int countOrder = productRepository.countOrdersByProductId(product.getProductId(), OrderStatus.COMPLETED.toString());

            return ProductResponse.productResponse(product, countOrder, "Cập nhật sản phẩm trong cửa hàng thành công!", "Success");
        } catch (Exception e) {
            throw new InternalServerErrorException("Cập nhật sản phẩm trong cửa hàng thất bại! " + e.getMessage());
        }
    }


    @Override
    @Transactional
    public ProductResponse updateStatusProduct(Long productId, String username, Status status) {
        checkExistProductByProductIdAndUsername(productId, username);
        Product product = getProductByProductId(productId);
        updateStatusProductByRequest(product, status);

        try {
            productRepository.save(product);
            productVariantShopService.updateStatusProductVariants(productId, product.getProductVariants(), status);
            Product productUpdate = getProductByProductId(productId);

            int countOrder = productRepository.countOrdersByProductId(product.getProductId(), OrderStatus.COMPLETED.toString());

            return ProductResponse.productResponse(productUpdate, countOrder, "Cập nhật trạng thái sản phẩm trong cửa hàng thành công!", "Success");
        } catch (Exception e) {
            throw new InternalServerErrorException("Cập nhật trạng thái sản phẩm trong cửa hàng thất bại!");
        }
    }


    @Override
    @Transactional
    public ProductResponse restoreProductById(Long productId, String username) {
        checkExistProductByProductIdAndUsername(productId, username);
        Product product = getProductByProductId(productId);
        restoreProduct(product);

        try {
            productRepository.save(product);
            productVariantShopService.updateStatusProductVariants(productId, product.getProductVariants(), Status.ACTIVE);
            Product productUpdate = getProductByProductId(productId);

            int countOrder = productRepository.countOrdersByProductId(product.getProductId(), OrderStatus.COMPLETED.toString());

            return ProductResponse.productResponse(productUpdate, countOrder, "Khôi phục sản phẩm trong cửa hàng thành công!", "Success");
        } catch (Exception e) {
            throw new InternalServerErrorException("Khôi phục sản phẩm trong cửa hàng thất bại!");
        }
    }


    @Override
    public ProductPageResponse getListProductPageByUsernameAndStatus(String username, int page, int size, Status status) {
        Shop shop = shopService.getShopByUsername(username);
        Page<Product> products = productRepository
                .findAllByShopShopIdAndStatusOrderByName(shop.getShopId(), status, PageRequest.of(page - 1, size))
                .orElseThrow(() -> new NotFoundException("Cửa hàng không có sản phẩm đang bán!"));
        String message = "Lấy danh sách sản phẩm theo trạng thái trong cửa hàng thành công.";

        return ProductPageResponse.productPageResponse(products, message);
    }


    @Override
    @Transactional
    public ListProductResponse updateProductsByIdAndPrice(ChangePriceProductsRequest request, String username) {
        Long shopId = shopService.getShopByUsername(username).getShopId();
        checkProductIdsInShop(request.getProductIds(), shopId);
        List<Product> products = getProductsByProductIds(request.getProductIds());

        for (Product product : products) {
            productVariantShopService.updatePriceProductVariants(request.getPrice(), product.getProductVariants());
            product.setUpdateAt(LocalDateTime.now());
        }

        try {
            productRepository.saveAll(products);
            return ListProductResponse.listProductResponse(products, "Cập nhật giá danh sách sản phẩm trong cửa hàng thành công!", "Success");
        } catch (Exception e) {
            throw new InternalServerErrorException("Cập nhật giá danh sách sản phẩm trong cửa hàng thất bại!");
        }
    }


    @Override
    @Transactional
    public ListProductResponse updateProductsByIdAndPriceAndPercent(ChangePriceProductsByPercentRequest request, String username) {
        Long shopId = shopService.getShopByUsername(username).getShopId();
        checkProductIdsInShop(request.getProductIds(), shopId);
        List<Product> products = getProductsByProductIds(request.getProductIds());

        for (Product product : products) {
            productVariantShopService.updatePriceProductVariantsByPercent(request.getPercent(), product.getProductVariants());
            product.setUpdateAt(LocalDateTime.now());
        }

        try {
            productRepository.saveAll(products);
            return ListProductResponse.listProductResponse(products, "Cập nhật giá danh sách sản phẩm theo phầm trăm trong cửa hàng thành công!", "Success");
        } catch (Exception e) {
            throw new InternalServerErrorException("Cập nhật giá danh sách sản phẩm theo phần trăm trong cửa hàng thất bại!");
        }
    }



    @Override
    public void checkProductIdsInShop(List<Long> productIds, Long shopId) {
        if (productIds.isEmpty()) {
            throw new BadRequestException("Danh sách sản phẩm không được để trống!");
        }
        if (!productRepository.existsByProductIdInAndShopShopId(productIds, shopId)) {
            throw new BadRequestException("Có sản phẩm không tồn tại trong cửa hàng!");
        }
    }


    @Override
    public List<Product> getProductsByProductIds(List<Long> productIds) {
        return productRepository.findAllByProductIdIn(productIds)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy sản phẩm nào trong danh sách mã sản phẩm!"));
    }


    @Override
    public void checkExistProductByProductIdAndUsername(Long productId, String username) {
        if (!productRepository.existsByProductIdAndShopCustomerUsername(productId, username)) {
            throw new NotFoundException("Sản phẩm không tồn tại trong cửa hàng!");
        }
    }

    private void restoreProduct(Product product) {
        if (product.getStatus() != Status.DELETED) {
            throw new BadRequestException("Sản phẩm chưa bị xóa trong cửa hàng!");
        }
        List<ProductVariant> activeProductVariants = product.getProductVariants().stream()
                .filter(productVariant -> productVariant.getStatus() == Status.DELETED)
                .toList();

        product.setProductVariants(activeProductVariants);
        product.setStatus(Status.ACTIVE);
        product.setUpdateAt(LocalDateTime.now());
    }


    private Product getProductByProductId(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("Sản phẩm không tồn tại!"));
    }


    private void checkUpdateProductExistByName(String name, Long productId) {
        if (productRepository.existsByNameAndProductIdNot(name, productId)) {
            throw new BadRequestException("Sản phẩm cập nhật đã có tên đã tồn tại trong cửa hàng!");
        }
    }







    private void updateStatusProductByRequest(Product product, Status status) {
        if (product.getStatus() == Status.DELETED) {
            throw new BadRequestException("Sản phẩm đã bị xóa trong cửa hàng!");
        }
        product.setStatus(status);
        product.setUpdateAt(LocalDateTime.now());
        List<ProductVariant> productVariants = product.getProductVariants().stream()
                .filter(productVariant -> productVariant.getStatus() != Status.DELETED && productVariant.getStatus() != Status.LOCKED)
                .collect(Collectors.toList());
        product.setProductVariants(productVariants);
    }


    private void updateProductByRequest(Product product, ProductRequest request, Category category, Brand brand) {
        product.setName(request.getName());
        product.setImage(request.isChangeImage() ? imageService.uploadImageToFirebase(request.getImage()) : product.getImage());
        product.setDescription(request.getDescription());
        product.setInformation(request.getInformation());
        product.setCategory(category);
        product.setBrand(brand);
        product.setUpdateAt(LocalDateTime.now());
    }


    private Product createProductByRequest(ProductRequest request, Category category, Brand brand, Shop shop) {
        Product product = new Product();
        product.setName(request.getName());
        product.setImage(imageService.uploadImageToFirebase(request.getImage()));
        product.setDescription(request.getDescription());
        product.setInformation(request.getInformation());
        product.setSold(0L);
        product.setShop(shop);
        product.setCategory(category);
        product.setBrand(brand);
        product.setStatus(Status.ACTIVE);
        product.setCreateAt(LocalDateTime.now());
        product.setUpdateAt(LocalDateTime.now());

        return product;
    }


    private Brand checkBrand(Long brandId) {
        if (brandId == null) {
            return null;
        }

        return brandRepository.findById(brandId)
                .orElseThrow(() -> new BadRequestException("Thương hiệu không tồn tại!"));
    }


    private void checkExistProductByName(String name) {
        if (productRepository.existsByName(name)) {
            throw new BadRequestException("Sản phẩm có tên đã tồn tại trong cửa hàng!");
        }
    }



}
