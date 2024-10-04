package hcmute.kltn.vtv.service.vendor.impl;

import hcmute.kltn.vtv.model.entity.vendor.Product;
import hcmute.kltn.vtv.repository.vendor.ProductVariantRepository;
import hcmute.kltn.vtv.service.vtv.IImageService;
import hcmute.kltn.vtv.util.exception.BadRequestException;
import hcmute.kltn.vtv.model.data.vendor.request.ProductVariantRequest;
import hcmute.kltn.vtv.model.entity.vendor.Attribute;
import hcmute.kltn.vtv.model.entity.vendor.ProductVariant;
import hcmute.kltn.vtv.model.extra.Status;
import hcmute.kltn.vtv.service.vendor.IAttributeShopService;
import hcmute.kltn.vtv.service.vendor.IProductVariantShopService;
import hcmute.kltn.vtv.util.exception.InternalServerErrorException;
import hcmute.kltn.vtv.util.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductVariantShopServiceImpl implements IProductVariantShopService {

    private final IAttributeShopService attributeService;
    private final ProductVariantRepository productVariantRepository;
    private final IImageService imageService;


    @Override
    @Transactional
    public List<ProductVariant> addNewProductVariants(List<ProductVariantRequest> requests, Product product) {
        try {
            List<ProductVariant> productVariants = new ArrayList<>();
            for (ProductVariantRequest request : requests) {
                productVariants.add(addNewProductVariant(request, product));
            }

            return productVariants;
        } catch (Exception e) {
            throw new BadRequestException("Thêm danh sách biến thể sản phẩm thất bại! " + e.getMessage());
        }
    }


    @Override
    @Transactional
    public ProductVariant addNewProductVariant(ProductVariantRequest request, Product product) {
        checkExistProductVariantBySku(request.getSku(), product.getProductId());

        List<Attribute> attributes = new ArrayList<>();
        if (request.getProductAttributeRequests() != null && !request.getProductAttributeRequests().isEmpty()) {
            attributes = attributeService.addNewAttributesByProductAttributeRequests(request.getProductAttributeRequests(), product.getShop());
        }
        ProductVariant productVariant = createProductVariantByProductVariantRequest(request, attributes, product);

        try {

            return productVariantRepository.save(productVariant);
        } catch (Exception e) {
            imageService.deleteImageInFirebase(productVariant.getImage());

            throw new InternalServerErrorException("Thêm biến thể sản phẩm thất bại! " + e.getMessage());
        }
    }


    @Override
    @Transactional
    public List<ProductVariant> updateProductVariants(List<ProductVariantRequest> requests, Product product) {
        List<ProductVariant> productVariantsOld = productVariantRepository
                .findAllByProductProductIdAndStatus(product.getProductId(), Status.ACTIVE)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy biến thể sản phẩm!"));
        List<ProductVariant> productVariantsCurrent = new ArrayList<>();
        for (ProductVariantRequest request : requests) {
            productVariantsCurrent.add(updateProductVariant(request, product));
        }
        List<ProductVariant> missingProductVariants = findMissingProductVariants(productVariantsOld, productVariantsCurrent);

        try {
            if (!missingProductVariants.isEmpty()) {
                updateStatusProductVariants(product.getProductId(), missingProductVariants, Status.DELETED);
            }

            return productVariantsCurrent;
        } catch (Exception e) {
            throw new InternalServerErrorException("Cập nhật danh sách biến thể sản phẩm thất bại!");
        }
    }


    @Override
    @Transactional
    public ProductVariant updateProductVariant(ProductVariantRequest request, Product product) {
        if (request.getProductVariantId() == null) {
            return addNewProductVariant(request, product);
        } else {
            checkExistProductVariantByIdAndProductId(product.getProductId(), request.getProductVariantId());
            ProductVariant productVariant = productVariantRepository.findByProductVariantId(request.getProductVariantId());
            List<Attribute> attributes = new ArrayList<>();
            if (request.getProductAttributeRequests() != null && !request.getProductAttributeRequests().isEmpty()) {
                attributes = attributeService.addNewAttributesByProductAttributeRequests(request.getProductAttributeRequests(), product.getShop());
            }
            checkSameSkuProductVariant(request.getSku(), productVariant);
            updateProductVariantByProductVariantRequest(productVariant, request, attributes);

            try {

                return productVariantRepository.save(productVariant);
            } catch (Exception e) {
                throw new InternalServerErrorException("Cập nhật biến thể sản phẩm thất bại!");
            }
        }
    }


    @Override
    @Transactional
    public List<ProductVariant> updateStatusProductVariants(Long productId, List<ProductVariant> productVariants, Status status) {
        for (ProductVariant productVariant : productVariants) {
            updateStatusProductVariant(productId, productVariant, status);
        }

        return productVariants;
    }


    @Override
    @Transactional
    public List<ProductVariant> updatePriceProductVariants(Long price, List<ProductVariant> productVariants) {
        for (ProductVariant productVariant : productVariants) {
            if (productVariant.getStatus().equals(Status.DELETED)) {
                continue;
            }
            checkPriceBeforeUpdate(productVariant.getOriginalPrice(), price, productVariant.getProduct().getName(), productVariant.getSku());
            productVariant.setPrice(price);
            productVariant.setUpdateAt(LocalDateTime.now());
        }
        try {
            return productVariantRepository.saveAll(productVariants);
        } catch (Exception e) {
            throw new InternalServerErrorException("Cập nhật giá biến thể sản phẩm thất bại!");
        }
    }


    @Override
    @Transactional
    public List<ProductVariant> updatePriceProductVariantsByPercent(int percent, List<ProductVariant> productVariants) {
        for (ProductVariant productVariant : productVariants) {
            if (productVariant.getStatus().equals(Status.DELETED)) {
                continue;
            }
            Long price = productVariant.getOriginalPrice() * percent / 100;
            productVariant.setPrice(price);
            productVariant.setUpdateAt(LocalDateTime.now());
        }
        try {
            return productVariantRepository.saveAll(productVariants);
        } catch (Exception e) {
            throw new InternalServerErrorException("Cập nhật giá biến thể sản phẩm thất bại!");
        }
    }


    @Transactional
    public ProductVariant updateStatusProductVariant(Long productId, ProductVariant productVariant, Status status) {
        checkExistProductVariantByIdAndProductId(productId, productVariant.getProductVariantId());
        productVariant.setStatus(status);
        productVariant.setUpdateAt(LocalDateTime.now());
        try {

            return productVariantRepository.save(productVariant);
        } catch (Exception e) {
            throw new InternalServerErrorException("Cập nhật biến thể sản phẩm thất bại!");
        }
    }

    private void checkPriceBeforeUpdate(Long priceOriginal, Long priceUpdate, String productName, String sku) {
        if (priceUpdate > priceOriginal) {
            throw new BadRequestException("Giá mới của sản phẩm " + productName + " có mã " + sku
                    + " không thể cao hơn giá cũ! Giá gốc: " + priceOriginal + "VNĐ - Giá mới: " + priceUpdate + "VNĐ");
        }

        if (priceOriginal*4/100 > priceUpdate) {
            throw new BadRequestException("Giá mới của sản phẩm " + productName + " có mã " + sku
                    + " không thể thấp hơn giá cũ quá 4%! Giá gốc: " + priceOriginal + "VNĐ - Giá mới: " + priceUpdate + "VNĐ và không thể thấp hơn " + priceOriginal*4/100 + "VNĐ");
        }
    }


    public List<ProductVariant> findMissingProductVariants(List<ProductVariant> productVariantsOld, List<ProductVariant> productVariantsCurrent) {
        List<ProductVariant> missingProductVariants = new ArrayList<>();
        Set<Long> currentProductVariantIds = productVariantsCurrent.stream()
                .map(ProductVariant::getProductVariantId)
                .collect(Collectors.toSet());

        for (ProductVariant productVariantOld : productVariantsOld) {
            if (!currentProductVariantIds.contains(productVariantOld.getProductVariantId())) {
                missingProductVariants.add(productVariantOld);
            }
        }
        return missingProductVariants;
    }


    private void checkExistProductVariantByIdAndProductId(Long productId, Long productVariantId) {
        if (!productVariantRepository.existsByProductProductIdAndProductVariantId(productId, productVariantId)) {
            throw new BadRequestException("Biến thể sản phẩm không tồn tại trong sản phẩm!");
        }
    }

    private ProductVariant createProductVariantByProductVariantRequest(ProductVariantRequest request, List<Attribute> attributes, Product product) {
        ProductVariant productVariant = new ProductVariant();
        productVariant.setSku(request.getSku());
        productVariant.setImage(imageService.uploadImageToFirebase(request.getImage()));
        productVariant.setOriginalPrice(request.getOriginalPrice());
        productVariant.setPrice(request.getPrice());
        productVariant.setQuantity(request.getQuantity());
        productVariant.setStatus(Status.ACTIVE);
        productVariant.setCreateAt(LocalDateTime.now());
        productVariant.setUpdateAt(LocalDateTime.now());
        productVariant.setAttributes(attributes);
        productVariant.setProduct(product);

        return productVariant;
    }


    private void updateProductVariantByProductVariantRequest(ProductVariant productVariant, ProductVariantRequest request, List<Attribute> attributes) {
        productVariant.setSku(request.getSku());
        productVariant.setImage(request.isChangeImage() ? imageService.uploadImageToFirebase(request.getImage()) : productVariant.getImage());
        productVariant.setOriginalPrice(request.getOriginalPrice());
        productVariant.setPrice(request.getPrice());
        productVariant.setQuantity(request.getQuantity());
        productVariant.setAttributes(attributes);
        productVariant.setUpdateAt(LocalDateTime.now());
    }


    private void checkExistProductVariantBySku(String sku, Long productId) {
        if (productVariantRepository.existsBySkuAndProductProductId(sku, productId)) {
            throw new BadRequestException("Mã biến thể sản phẩm đã tồn tại trong sản phẩm!");
        }
    }


    private void checkSameSkuProductVariant(String sku, ProductVariant productVariant) {


        if (productVariantRepository.existsBySkuAndProductProductIdAndProductVariantIdNot(sku, productVariant.getProduct().getProductId(), productVariant.getProductVariantId())) {
            throw new BadRequestException("Mã biến thể sản phẩm đã tồn tại trong sản phẩm!");
        }
    }


}
