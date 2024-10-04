package hcmute.kltn.vtv.repository.vendor;

import hcmute.kltn.vtv.model.entity.vtv.Category;
import hcmute.kltn.vtv.model.entity.vendor.Product;
import hcmute.kltn.vtv.model.extra.OrderStatus;
import hcmute.kltn.vtv.model.extra.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {


    Optional<Page<Product>> findAllByShopShopIdAndStatusOrderByName(Long shopId, Status status,
                                                                           Pageable pageable);

    Optional<Page<Product>> findAllByCategoryCategoryIdAndStatus(Long categoryId, Status status,
                                                                               Pageable pageable);
    boolean existsByCategoryCategoryId(Long categoryId);

    boolean existsByBrandBrandId(Long brandId);

    @Query(value =
            "SELECT COUNT(DISTINCT o.order_id) " +
                    "FROM `order` o " +
                    "JOIN order_item oi ON o.order_id = oi.order_id " +
                    "JOIN cart c ON oi.cart_id = c.cart_id " +
                    "JOIN product_variant pv ON c.product_variant_id = pv.product_variant_id " +
                    "JOIN product p ON pv.product_id = p.product_id " +
                    "WHERE p.product_id = :productId " +
                    "AND o.status = :status",
            nativeQuery = true)
    int countOrdersByProductId(Long productId, String status);

    int countByShopShopIdAndStatus(Long shopId, Status status);



    boolean existsByName(String name);

    boolean existsByProductIdAndShopShopId(Long productId, Long shopId);

    boolean existsByNameAndProductIdNot(String name, Long productId);

    boolean existsByProductIdAndShopCustomerUsername(Long productId, String username);

    boolean existsByProductIdInAndShopShopId(List<Long> productIds, Long shopId);




    Optional<List<Product>> findAllByProductIdIn(List<Long> productIds);

    Optional<List<Product>> findByShopShopIdAndStatusOrderBySoldDescNameAsc(Long shopId, Status status);

    Optional<List<Product>> findByStatusOrderBySoldDescNameAsc(Status status);


    @Query("SELECT p FROM Product p JOIN p.productVariants v " +
            "WHERE p.shop.shopId = :shopId " +
            "AND p.status = :status " +
            "AND v.status = :status " +
            "AND v.price >= :minPrice " +
            "AND v.price <= :maxPrice")
    Optional<List<Product>> findByPriceRange(@Param("shopId") Long shopId,
                                             @Param("status") Status status,
                                             @Param("minPrice") Long minPrice,
                                             @Param("maxPrice") Long maxPrice);


    Optional<List<Product>> findByShopShopIdAndStatus(Long shopId, Status status);

    @Query("SELECT p FROM Product p JOIN p.productVariants v " +
            "WHERE p.status = :status " +
            "AND v.status = :status " +
            "AND v.price >= :minPrice " +
            "AND v.price <= :maxPrice")
    Optional<List<Product>> findByPriceRange(@Param("status") Status status,
                                             @Param("minPrice") Long minPrice,
                                             @Param("maxPrice") Long maxPrice);

    @Query("SELECT p FROM Product p " +
            "WHERE p.status = :status " +
            "ORDER BY p.createAt DESC")
    Optional<Page<Product>> findNewestProducts(@Param("status") Status status, Pageable pageable);



    Optional<Page<Product>> findAllByCategoryCategoryIdAndStatusOrderByCreateAt(Long categoryId, Status status,
                                                                                PageRequest pageable);


    Optional<List<Product>> findAllByCategoryCategoryIdAndStatus(Long categoryId, Status status);


    Optional<Page<Product>> findAllByCategoryInAndStatusOrderByCreateAt(List<Category> categories, Status status,
                                                                        PageRequest pageRequest);


    Optional<Page<Product>> findAllByShopShopIdAndStatusOrderByCreateAt(Long shopId, Status status,
                                                                                Pageable pageable);

    Optional<Page<Product>> findAllByShopShopIdAndStatusOrderBySoldDescNameAsc(Long shopId, Status status,
                                                                                       Pageable pageable);

    Optional<Page<Product>> findAllByShopShopIdAndStatusOrderByCreateAtDesc(Long shopId, Status status,
                                                                                    Pageable pageable);


    Optional<Page<Product>> findAllByStatusOrderByName(Status status, Pageable pageable);



    /// FIX
    /////////////////////////////////////////// Suggestion ///////////////////////////////////////////

    @Query(value =
            "SELECT DISTINCT p.* " +
                    "FROM product p " +
                    "WHERE p.status = :status " +
                    "ORDER BY RAND()",
            countQuery = "SELECT COUNT( DISTINCT p.product_id) FROM product p WHERE  p.status = :status",
            nativeQuery = true)
    Optional<Page<Product>> suggestByRandomly(
            @Param("status") String status,
            Pageable pageable);


    @Query(value =
            "SELECT DISTINCT p.* " +
                    "FROM product p " +
                    "WHERE MATCH(p.name) AGAINST (:searchText IN BOOLEAN MODE) " +
                    "AND p.status = :status " +
                    "ORDER BY RAND()",
            countQuery = "SELECT COUNT(DISTINCT p.product_id) FROM product p WHERE MATCH(p.name) AGAINST (:searchText IN BOOLEAN MODE) AND p.status = :status",
            nativeQuery = true)
    Optional<Page<Product>> suggestBySearchHistoryAndRandomly(
            @Param("searchText") String searchText,
            @Param("status") String status,
            Pageable pageable);


    @Query(value =
            "SELECT DISTINCT p.* " +
                    "FROM product p " +
                    "WHERE p.category_id = :categoryId " +
                    "AND p.status = :status " +
                    "ORDER BY RAND()",
            countQuery = "" +
                    "SELECT COUNT( DISTINCT p.product_id) " +
                    "FROM product p " +
                    "WHERE p.category_id = :categoryId " +
                    "AND p.status = :status ",
            nativeQuery = true)
    Optional<Page<Product>> suggestByCategoryAndRandomly(
            @Param("categoryId") Long categoryId,
            @Param("status") String status,
            Pageable pageable);


    @Query(value =
            "SELECT DISTINCT p.* " +
                    "FROM product p " +
                    "JOIN  category c " +
                    "ON p.category_id = c.category_id " +
                    "WHERE c.parent_id = :categoryParentId " +
                    "AND p.status = :status " +
                    "ORDER BY RAND()",
            countQuery = "" +
                    "SELECT COUNT( DISTINCT p.product_id) " +
                    "FROM product p " +
                    "JOIN  category c " +
                    "ON p.category_id = c.category_id " +
                    "WHERE c.parent_id = :categoryParentId " +
                    "AND p.status = :status ",
            nativeQuery = true)
    Optional<Page<Product>> suggestByCategoryParentAndRandomly(
            @Param("categoryParentId") Long categoryParentId,
            @Param("status") String status,
            Pageable pageable);



    /////////////////////////////////////////// Search On VTV ///////////////////////////////////////////



    /////////////////////////////////////////// Top 10 On Shop By Date ///////////////////////////////////////////

    @Query(value =
            "SELECT DISTINCT p.* " +
                    "FROM product p " +
                    "JOIN product_variant pv ON p.product_id = pv.product_id " +
                    "JOIN cart c ON pv.product_variant_id = c.product_variant_id " +
                    "JOIN order_item oi ON c.cart_id = oi.cart_id " +
                    "JOIN `order` o ON oi.order_id = o.order_id " +
                    "WHERE p.shop_id = :shopId " +
                    "AND o.status = :orderStatus " +
                    "AND o.update_at BETWEEN :startDate AND :endDate " +
                    "LIMIT :limit OFFSET 0",  // Assuming the offset is always 0 for the first page
            countQuery =
                    "SELECT COUNT(DISTINCT p.product_id) " +
                            "FROM product p " +
                            "JOIN product_variant pv ON p.product_id = pv.product_id " +
                            "JOIN cart c ON pv.product_variant_id = c.product_variant_id " +
                            "JOIN order_item oi ON c.cart_id = oi.cart_id " +
                            "JOIN `order` o ON oi.order_id = o.order_id " +
                            "WHERE p.shop_id = :shopId " +
                            "AND o.status = :orderStatus " +
                            "AND o.update_at BETWEEN :startDate AND :endDate",
            nativeQuery = true)
    Optional<List<Product>> getBestProductsByLimitAndShopIdAndOrderStatusAndOrderDateBetween(
            int limit, Long shopId, String orderStatus, Date startDate, Date endDate);






    @Query(value =
            "SELECT DISTINCT p.* " +
                    "FROM product p " +
                    "JOIN product_variant pv ON p.product_id = pv.product_id " +
                    "JOIN cart c ON pv.product_variant_id = c.product_variant_id " +
                    "JOIN order_item oi ON c.cart_id = oi.cart_id " +
                    "JOIN `order` o ON oi.order_id = o.order_id " +
                    "WHERE  o.status = :orderStatus " +
                    "AND o.update_at BETWEEN :startDate AND :endDate " +
                    "LIMIT :limit OFFSET 0",  // Assuming the offset is always 0 for the first page
            countQuery =
                    "SELECT COUNT(DISTINCT p.product_id) " +
                            "FROM product p " +
                            "JOIN product_variant pv ON p.product_id = pv.product_id " +
                            "JOIN cart c ON pv.product_variant_id = c.product_variant_id " +
                            "JOIN order_item oi ON c.cart_id = oi.cart_id " +
                            "JOIN `order` o ON oi.order_id = o.order_id " +
                            "WHERE  o.status = :orderStatus " +
                            "AND o.update_at BETWEEN :startDate AND :endDate",
            nativeQuery = true)
    Optional<List<Product>> getBestProductsByLimitAndOrderStatusAndOrderDateBetween(
            int limit,  String orderStatus, Date startDate, Date endDate);




    @Query(value =
            "SELECT DISTINCT p.* " +
                    "FROM product p " +
                    "JOIN product_variant pv ON p.product_id = pv.product_id " +
                    "JOIN cart c ON pv.product_variant_id = c.product_variant_id " +
                    "JOIN order_item oi ON c.cart_id = oi.cart_id " +
                    "JOIN `order` o ON oi.order_id = o.order_id " +
                    "WHERE p.shop_id = :shopId " +
                    "AND o.update_at BETWEEN :startDate AND :endDate " +
                    "LIMIT :limit OFFSET 0",  // Assuming the offset is always 0 for the first page
            countQuery =
                    "SELECT COUNT(DISTINCT p.product_id) " +
                            "FROM product p " +
                            "JOIN product_variant pv ON p.product_id = pv.product_id " +
                            "JOIN cart c ON pv.product_variant_id = c.product_variant_id " +
                            "JOIN order_item oi ON c.cart_id = oi.cart_id " +
                            "JOIN `order` o ON oi.order_id = o.order_id " +
                            "WHERE p.shop_id = :shopId " +
                            "AND o.update_at BETWEEN :startDate AND :endDate",
            nativeQuery = true)
    Optional<List<Product>> getBestProductsByLimitAndShopIdAndOrderDateBetween(
            int limit, Long shopId, Date startDate, Date endDate);













}
