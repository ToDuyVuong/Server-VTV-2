package hcmute.kltn.vtv.service.user.impl;


import hcmute.kltn.vtv.model.data.vendor.response.ProductPageResponse;
import hcmute.kltn.vtv.model.entity.vendor.Product;
import hcmute.kltn.vtv.model.extra.Status;
import hcmute.kltn.vtv.repository.vendor.ProductRepository;
import hcmute.kltn.vtv.repository.vtv.ProductSearchRepository;
import hcmute.kltn.vtv.repository.vtv.SearchProductShopRepository;
import hcmute.kltn.vtv.repository.vtv.ShopRepository;
import hcmute.kltn.vtv.service.user.ISearchProductService;
import hcmute.kltn.vtv.util.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SearchProductServiceImpl implements ISearchProductService {


    private final ProductRepository productRepository;
    private final ProductSearchRepository productSearchRepository;
    private final ShopRepository shopRepository;
    private final SearchProductShopRepository searchProductRepository;

    @Override
    public ProductPageResponse getProductPageBySearchAndSort(String search, int page, int size, String sort) {
        String message = checkSortMessage(sort);
        Page<Product> productPage = getProductPage(search, page, size, sort);

        return ProductPageResponse.productPageResponse(productPage, message);
    }

    @Override
    public ProductPageResponse getProductsPageBySearchAndPriceRangeAndSort(String search, int page, int size, String sort,
                                                                           Long minPrice, Long maxPrice) {
        String message = checkSortMessage(sort) + " Trong khoảng giá từ " + minPrice + " VNĐ đến " + maxPrice + " VNĐ!";
        Page<Product> productPage = getProductPageAndPriceRange(search, page, size, sort, minPrice, maxPrice);

        return ProductPageResponse.productPageResponse(productPage, message);
    }


    @Override
    public ProductPageResponse getProductsPageBySearchAndSortOnShop(String search, Long shopId,
                                                                    int page, int size, String sort) {
        if (!shopRepository.existsById(shopId)) {
            throw new NotFoundException("Không tìm thấy cửa hàng nào có mã: " + shopId);
        }

        String message = checkSortMessage(sort) + " Của cửa hàng!";
        Page<Product> productPage = getProductPageOnShop(search, shopId, page, size, sort);

        return ProductPageResponse.productPageResponse(productPage, message);
    }




    @Override
    public ProductPageResponse getProductPageByUsernameAndSearchSortOnShop(String search, String username,
                                                                    int page, int size, String sort) {
        if (!shopRepository.existsByCustomerUsername(username)) {
            throw new NotFoundException("Không tìm thấy cửa hàng nào có tên đăng nhập: " + username);
        }

        String message = checkSortMessage(sort) + " Của cửa hàng!";
        Page<Product> productPage = getProductPageOnShopByUsername(search, username, page, size, sort);

        return ProductPageResponse.productPageResponse(productPage, message);
    }



    @Override
    public ProductPageResponse getProductsPageBySearchAndPriceRangeAndSortOnShop(String search, Long shopId,
                                                                                 int page, int size, String sort,
                                                                                 Long minPrice, Long maxPrice) {
        if (!shopRepository.existsById(shopId)) {
            throw new NotFoundException("Không tìm thấy cửa hàng nào có mã: " + shopId);
        }

        String message = checkSortMessage(sort) + " Trong khoảng giá từ " + minPrice + " VNĐ đến " + maxPrice + " VNĐ!";
        Page<Product> productPage = getProductPagePriceRangeOnShop(search, shopId, page, size, sort, minPrice, maxPrice);

        return ProductPageResponse.productPageResponse(productPage, message);
    }


    private String checkSortMessage(String sort) {
        return switch (sort) {
            case "newest" -> "Lọc sản phẩm tìm kiếm theo thứ tự mới nhất thành công!";
            case "best-selling" -> "Lọc sản phẩm tìm kiếm theo thứ tự bán chạy nhất thành công!";
            case "price-asc" -> "Lọc sản phẩm tìm kiếm theo thứ tự giá tăng dần thành công!";
            case "price-desc" -> "Lọc sản phẩm tìm kiếm theo thứ tự giá giảm dần thành công!";
            case "random" -> "Lấy sản phẩm tìm kiếm ngẫu nhiên thành công!";
            default -> "Tìm kiếm sản phẩm thành công!";
        };
    }


    private Page<Product> getProductPageAndPriceRange(String search, int page, int size, String sort, Long minPrice, Long maxPrice) {
        return switch (sort) {
            case "newest" -> productSearchRepository
                    .searchFullTextAndPriceRangeByStatusAndSortCreateAtAsc(
                            search, Status.ACTIVE.toString(), minPrice, maxPrice,
                            PageRequest.of(page - 1, size))
                    .orElseThrow(() -> new NotFoundException("Không tìm thấy sản phẩm nào! Lọc sản phẩm theo thứ tự mới nhất!"));

            case "best-selling" -> productSearchRepository
                    .searchFullTextAndPriceRangeByStatusAndSortBestSelling(
                            search, Status.ACTIVE.toString(), minPrice, maxPrice,
                            PageRequest.of(page - 1, size))
                    .orElseThrow(() -> new NotFoundException("Không tìm thấy sản phẩm nào! Lọc sản phẩm theo thứ tự bán chạy nhất!"));

            case "price-asc" -> productSearchRepository
                    .searchFullTextAndPriceRangeByStatusAndSortPriceAsc(
                            search, Status.ACTIVE.toString(), minPrice, maxPrice,
                            PageRequest.of(page - 1, size))
                    .orElseThrow(() -> new NotFoundException("Không tìm thấy sản phẩm nào!" +
                            " Lọc sản phẩm theo thứ tự giá tăng dần!"));

            case "price-desc" -> productSearchRepository
                    .searchFullTextAndPriceRangeByStatusAndSortPriceDesc(
                            search, Status.ACTIVE.toString(), minPrice, maxPrice,
                            PageRequest.of(page - 1, size))
                    .orElseThrow(() -> new NotFoundException("Không tìm thấy sản phẩm nào! Lọc sản phẩm theo thứ tự giá giảm dần!"));

            default -> productSearchRepository
                    .searchFullTextAndPriceRangeByStatusAndSortRandomly(
                            search, Status.ACTIVE.toString(), minPrice, maxPrice,
                            PageRequest.of(page - 1, size))
                    .orElseThrow(() -> new NotFoundException("Không tìm thấy sản phẩm nào! Lấy sản phẩm ngẫu nhiên!"));
        };
    }


    private Page<Product> getProductPage(String search, int page, int size, String sort) {
        return switch (sort) {
            case "newest" -> productSearchRepository
                    .searchFullTextByNameAndStatusAndSortByNewest(search, Status.ACTIVE.toString(),
                            PageRequest.of(page - 1, size))
                    .orElseThrow(() -> new NotFoundException("Không tìm thấy sản phẩm nào! Lọc sản phẩm theo thứ tự mới nhất!"));

            case "best-selling" -> productSearchRepository
                    .searchFullTextByNameAndStatusAndSortByBestSelling(search, Status.ACTIVE.toString(),
                            PageRequest.of(page - 1, size))
                    .orElseThrow(() -> new NotFoundException("Không tìm thấy sản phẩm nào! Lọc sản phẩm theo thứ tự bán chạy nhất!"));

            case "price-asc" -> productSearchRepository
                    .searchFullTextByNameAndStatusAndSortByPriceAsc(search, Status.ACTIVE.toString(),
                            PageRequest.of(page - 1, size))
                    .orElseThrow(() -> new NotFoundException("Không tìm thấy sản phẩm nào! Lọc sản phẩm theo thứ tự giá tăng dần!"));

            case "price-desc" -> productSearchRepository
                    .searchFullTextByNameAndStatusAndSortByPriceDesc(search, Status.ACTIVE.toString(),
                            PageRequest.of(page - 1, size))
                    .orElseThrow(() -> new NotFoundException("Không tìm thấy sản phẩm nào! Lọc sản phẩm theo thứ tự giá giảm dần!"));

            default -> productSearchRepository
                    .searchFullTextByNameAndStatusAndSortRandomly(search, Status.ACTIVE.toString(), PageRequest.of(page - 1, size))
                    .orElseThrow(() -> new NotFoundException("Không tìm thấy sản phẩm nào! Lấy sản phẩm ngẫu nhiên!"));
        };
    }





    private Page<Product> getProductPageOnShop(String search, Long shopId, int page, int size, String sort) {
        return switch (sort) {
            case "newest" -> productSearchRepository
                    .searchFullTextOnShopByNameAndStatusAndSortByNewest(search, shopId, Status.ACTIVE.toString(),
                            PageRequest.of(page - 1, size))
                    .orElseThrow(() -> new NotFoundException("Không tìm thấy sản phẩm nào trong cửa hàng! Lọc sản phẩm theo thứ tự mới nhất!"));

            case "best-selling" -> productSearchRepository
                    .searchFullTextOnShopByNameAndStatusAndSortByBestSelling(search, shopId, Status.ACTIVE.toString(),
                            PageRequest.of(page - 1, size))
                    .orElseThrow(() -> new NotFoundException("Không tìm thấy sản phẩm nào trong cửa hàng! Lọc sản phẩm theo thứ tự bán chạy nhất!"));

            case "price-asc" -> productSearchRepository
                    .searchFullTextOnShopByNameAndStatusAndSortByPriceAsc(search, shopId, Status.ACTIVE.toString(),
                            PageRequest.of(page - 1, size))
                    .orElseThrow(() -> new NotFoundException("Không tìm thấy sản phẩm nào trong cửa hàng! Lọc sản phẩm theo thứ tự giá tăng dần!"));

            case "price-desc" -> productSearchRepository
                    .searchFullTextOnShopByNameAndStatusAndSortByPriceDesc(search, shopId, Status.ACTIVE.toString(),
                            PageRequest.of(page - 1, size))
                    .orElseThrow(() -> new NotFoundException("Không tìm thấy sản phẩm nào trong cửa hàng! Lọc sản phẩm theo thứ tự giá giảm dần!"));

            default -> productSearchRepository
                    .searchFullTextOnShopByNameAndStatusAndSortRandomly(search, shopId, Status.ACTIVE.toString(), PageRequest.of(page - 1, size))
                    .orElseThrow(() -> new NotFoundException("Không tìm thấy sản phẩm nào trong cửa hàng! Lấy sản phẩm ngẫu nhiên!"));
        };
    }





    private Page<Product> getProductPageOnShopByUsername(String search, String username, int page, int size, String sort) {
        return switch (sort) {
            case "newest" -> searchProductRepository
                    .searchFullTextOnShopByUsernameAndNameAndStatusAndSortByNewest(search, username, Status.ACTIVE.toString(),
                            PageRequest.of(page - 1, size))
                    .orElseThrow(() -> new NotFoundException("Không tìm thấy sản phẩm nào trong cửa hàng! Lọc sản phẩm theo thứ tự mới nhất!"));

            case "best-selling" -> searchProductRepository
                    .searchFullTextOnShopByUsernameAndNameAndStatusAndSortByBestSelling(search, username, Status.ACTIVE.toString(),
                            PageRequest.of(page - 1, size))
                    .orElseThrow(() -> new NotFoundException("Không tìm thấy sản phẩm nào trong cửa hàng! Lọc sản phẩm theo thứ tự bán chạy nhất!"));

            case "price-asc" -> searchProductRepository
                    .searchFullTextOnShopByUsernameAndNameAndStatusAndSortByPriceAsc(search, username, Status.ACTIVE.toString(),
                            PageRequest.of(page - 1, size))
                    .orElseThrow(() -> new NotFoundException("Không tìm thấy sản phẩm nào trong cửa hàng! Lọc sản phẩm theo thứ tự giá tăng dần!"));

            case "price-desc" -> searchProductRepository
                    .searchFullTextOnShopByUsernameAndNameAndStatusAndSortByPriceDesc(search, username, Status.ACTIVE.toString(),
                            PageRequest.of(page - 1, size))
                    .orElseThrow(() -> new NotFoundException("Không tìm thấy sản phẩm nào trong cửa hàng! Lọc sản phẩm theo thứ tự giá giảm dần!"));

            default -> searchProductRepository
                    .searchFullTextOnShopByUsernameAndNameAndStatusAndSortRandomly(search, username, Status.ACTIVE.toString(), PageRequest.of(page - 1, size))
                    .orElseThrow(() -> new NotFoundException("Không tìm thấy sản phẩm nào trong cửa hàng! Lấy sản phẩm ngẫu nhiên!"));
        };
    }


    private Page<Product> getProductPagePriceRangeOnShop(String search, Long shopId, int page, int size, String sort, Long minPrice, Long maxPrice) {
        return switch (sort) {
            case "newest" -> productSearchRepository
                    .searchFullTextPriceRangeOnShopAndSortByNewest(
                            search, shopId, Status.ACTIVE.toString(), minPrice, maxPrice,
                            PageRequest.of(page - 1, size))
                    .orElseThrow(() -> new NotFoundException("Không tìm thấy sản phẩm nào trong cửa hàng! Lọc sản phẩm theo thứ tự mới nhất!"));

            case "best-selling" -> productSearchRepository
                    .searchFullTextPriceRangeOnShopAndSortByBestSelling(
                            search, shopId, Status.ACTIVE.toString(), minPrice, maxPrice,
                            PageRequest.of(page - 1, size))
                    .orElseThrow(() -> new NotFoundException("Không tìm thấy sản phẩm nào trong cửa hàng! Lọc sản phẩm theo thứ tự bán chạy nhất!"));

            case "price-asc" -> productSearchRepository
                    .searchFullTextPriceRangeOnShopAndSortByPriceAse(
                            search, shopId, Status.ACTIVE.toString(), minPrice, maxPrice,
                            PageRequest.of(page - 1, size))
                    .orElseThrow(() -> new NotFoundException("Không tìm thấy sản phẩm nào trong cửa hàng! Lọc sản phẩm theo thứ tự giá tăng dần!"));

            case "price-desc" -> productSearchRepository
                    .searchFullTextPriceRangeOnShopAndSortByPriceDesc(
                            search, shopId, Status.ACTIVE.toString(), minPrice, maxPrice,
                            PageRequest.of(page - 1, size))
                    .orElseThrow(() -> new NotFoundException("Không tìm thấy sản phẩm nào trong cửa hàng! Lọc sản phẩm theo thứ tự giá giảm dần!"));

            default -> productSearchRepository
                    .searchFullTextPriceRangeOnShopAndSortByRandomly(
                            search, shopId, Status.ACTIVE.toString(), minPrice, maxPrice,
                            PageRequest.of(page - 1, size))
                    .orElseThrow(() -> new NotFoundException("Không tìm thấy sản phẩm nào trong cửa hàng! Lấy sản phẩm ngẫu nhiên!"));
        };
    }





}
