package hcmute.kltn.vtv.repository.vtv;

import hcmute.kltn.vtv.model.entity.vendor.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductSearchRepository extends JpaRepository<Product, Long> {




    /////////////////////////////////////////// Search On VTV ///////////////////////////////////////////


    @Query(value =
            "SELECT DISTINCT p.* " +
                    "FROM product p " +
                    "WHERE (MATCH(p.name) AGAINST (:searchText IN BOOLEAN MODE) OR p.name LIKE %:searchText%) " +
                    "AND p.status = :status " +
                    "ORDER BY p.create_at DESC",
            countQuery = "SELECT COUNT(*) FROM product p WHERE (MATCH(p.name) AGAINST (:searchText IN BOOLEAN MODE) OR p.name LIKE %:searchText%) " +
                    "AND p.status = :status",
            nativeQuery = true)
    Optional<Page<Product>> searchFullTextByNameAndStatusAndSortByNewest(
            @Param("searchText") String searchText,
            @Param("status") String status,
            Pageable pageable);


    @Query(value =
            "SELECT DISTINCT p.* " +
                    "FROM product p " +
                    "WHERE (MATCH(p.name) AGAINST (:searchText IN BOOLEAN MODE) OR p.name LIKE %:searchText%) " +
                    "AND p.status = :status " +
                    "ORDER BY p.sold DESC",
            countQuery = "SELECT COUNT(*) FROM product p WHERE (MATCH(p.name) AGAINST (:searchText IN BOOLEAN MODE) OR p.name LIKE %:searchText%) " +
                    "AND p.status = :status",
            nativeQuery = true)
    Optional<Page<Product>> searchFullTextByNameAndStatusAndSortByBestSelling(
            @Param("searchText") String searchText,
            @Param("status") String status,
            Pageable pageable);

    @Query(value =
            "SELECT p.* " +
                    "FROM product p " +
                    "JOIN product_variant pv " +
                    "ON p.product_id = pv.product_id " +
                    "WHERE (MATCH(p.name) AGAINST (:searchText IN BOOLEAN MODE) OR p.name LIKE %:searchText%) " +
                    "AND p.status = :status " +
                    "GROUP BY p.product_id " +
                    "ORDER BY MIN(pv.price) ASC",
            countQuery = "SELECT COUNT(DISTINCT p.product_id) " +
                    "FROM product p " +
                    "JOIN product_variant pv " +
                    "ON p.product_id = pv.product_id " +
                    "WHERE (MATCH(p.name) AGAINST (:searchText IN BOOLEAN MODE) OR p.name LIKE %:searchText%) " +
                    "AND p.status = :status ",
            nativeQuery = true)
    Optional<Page<Product>> searchFullTextByNameAndStatusAndSortByPriceAsc(
            @Param("searchText") String searchText,
            @Param("status") String status,
            Pageable pageable);


    @Query(value =
            "SELECT p.* " +
                    "FROM product p " +
                    "JOIN product_variant pv " +
                    "ON p.product_id = pv.product_id " +
                    "WHERE (MATCH(p.name) AGAINST (:searchText IN BOOLEAN MODE) OR p.name LIKE %:searchText%) " +
                    "AND p.status = :status " +
                    "GROUP BY p.product_id " +
                    "ORDER BY MAX(pv.price) DESC ",
            countQuery = "SELECT COUNT(DISTINCT p.product_id) " +
                    "FROM product p " +
                    "JOIN product_variant pv " +
                    "ON p.product_id = pv.product_id " +
                    "WHERE (MATCH(p.name) AGAINST (:searchText IN BOOLEAN MODE) OR p.name LIKE %:searchText%) " +
                    "AND p.status = :status ",
            nativeQuery = true)
    Optional<Page<Product>> searchFullTextByNameAndStatusAndSortByPriceDesc(
            @Param("searchText") String searchText,
            @Param("status") String status,
            Pageable pageable);


    @Query(value =
            "SELECT  p.* " +
                    "FROM product p " +
                    "WHERE (MATCH(p.name) AGAINST (:searchText IN BOOLEAN MODE) OR p.name LIKE %:searchText%) " +
                    "AND p.status = :status " +
                    "GROUP BY p.product_id " +
                    "ORDER BY RAND()",
            countQuery = "SELECT COUNT(DISTINCT p.product_id) FROM product p WHERE (MATCH(p.name) AGAINST (:searchText IN BOOLEAN MODE) OR p.name LIKE %:searchText%) " +
                    "AND p.status = :status",
            nativeQuery = true)
    Optional<Page<Product>> searchFullTextByNameAndStatusAndSortRandomly(
            @Param("searchText") String searchText,
            @Param("status") String status,
            Pageable pageable);


    /////////////////////////////////////////// Price Range On VTV ///////////////////////////////////////////


    @Query(value =
            "SELECT  p.*  " +
                    "FROM product p " +
                    "JOIN product_variant pv ON p.product_id = pv.product_id " +
                    "WHERE (MATCH(p.name) AGAINST (:searchText IN BOOLEAN MODE) OR p.name LIKE %:searchText%) " +
                    "AND p.status = :status " +
                    "AND pv.price >= :minPrice " +
                    "AND pv.price <= :maxPrice " +
                    "GROUP BY p.product_id " +
                    "ORDER BY p.create_at ASC",
            countQuery = "SELECT COUNT(DISTINCT p.product_id) " +
                    "FROM product p " +
                    "JOIN product_variant pv " +
                    "ON p.product_id = pv.product_id " +
                    "WHERE (MATCH(p.name) AGAINST (:searchText IN BOOLEAN MODE) OR p.name LIKE %:searchText%) " +
                    "AND p.status = :status " +
                    "AND pv.price >= :minPrice " +
                    "AND pv.price <= :maxPrice",
            nativeQuery = true)
    Optional<Page<Product>> searchFullTextAndPriceRangeByStatusAndSortCreateAtAsc(
            @Param("searchText") String searchText,
            @Param("status") String status,
            @Param("minPrice") Long minPrice,
            @Param("maxPrice") Long maxPrice,
            Pageable pageable);

    @Query(value =
            "SELECT  p.*   " +
                    "FROM product p " +
                    "JOIN product_variant pv ON p.product_id = pv.product_id " +
                    "WHERE (MATCH(p.name) AGAINST (:searchText IN BOOLEAN MODE) OR p.name LIKE %:searchText%) " +
                    "AND p.status = :status " +
                    "AND pv.price >= :minPrice " +
                    "AND pv.price <= :maxPrice " +
                    "GROUP BY p.product_id " +
                    "ORDER BY p.sold DESC",
            countQuery = "SELECT COUNT( DISTINCT p.product_id) " +
                    "FROM product p " +
                    "JOIN product_variant pv " +
                    "ON p.product_id = pv.product_id " +
                    "WHERE (MATCH(p.name) AGAINST (:searchText IN BOOLEAN MODE) OR p.name LIKE %:searchText%) " +
                    "AND p.status = :status " +
                    "AND pv.price >= :minPrice " +
                    "AND pv.price <= :maxPrice",
            nativeQuery = true)
    Optional<Page<Product>> searchFullTextAndPriceRangeByStatusAndSortBestSelling(
            @Param("searchText") String searchText,
            @Param("status") String status,
            @Param("minPrice") Long minPrice,
            @Param("maxPrice") Long maxPrice,
            Pageable pageable);


    @Query(value =
            "SELECT  p.* " +
                    "FROM product p " +
                    "JOIN product_variant pv ON p.product_id = pv.product_id " +
                    "WHERE (MATCH(p.name) AGAINST (:searchText IN BOOLEAN MODE) OR p.name LIKE %:searchText%) " +
                    "AND p.status = :status " +
                    "AND pv.price >= :minPrice " +
                    "AND pv.price <= :maxPrice " +
                    "GROUP BY p.product_id " +
                    "ORDER BY MIN(pv.price) ASC",
            countQuery = "SELECT COUNT( DISTINCT p.product_id) " +
                    "FROM product p " +
                    "JOIN product_variant pv " +
                    "ON p.product_id = pv.product_id " +
                    "WHERE (MATCH(p.name) AGAINST (:searchText IN BOOLEAN MODE) OR p.name LIKE %:searchText%) " +
                    "AND p.status = :status " +
                    "AND pv.price >= :minPrice " +
                    "AND pv.price <= :maxPrice",
            nativeQuery = true)
    Optional<Page<Product>> searchFullTextAndPriceRangeByStatusAndSortPriceAsc(
            @Param("searchText") String searchText,
            @Param("status") String status,
            @Param("minPrice") Long minPrice,
            @Param("maxPrice") Long maxPrice,
            Pageable pageable);


    @Query(value =
            "SELECT  p.* " +
                    "FROM product p " +
                    "JOIN product_variant pv ON p.product_id = pv.product_id " +
                    "WHERE (MATCH(p.name) AGAINST (:searchText IN BOOLEAN MODE) OR p.name LIKE %:searchText%) " +
                    "AND p.status = :status " +
                    "AND pv.price >= :minPrice " +
                    "AND pv.price <= :maxPrice " +
                    "GROUP BY p.product_id " +
                    "ORDER BY MAX(pv.price) DESC ",
            countQuery = "SELECT COUNT( DISTINCT p.product_id) " +
                    "FROM product p " +
                    "JOIN product_variant pv " +
                    "ON p.product_id = pv.product_id " +
                    "WHERE (MATCH(p.name) AGAINST (:searchText IN BOOLEAN MODE) OR p.name LIKE %:searchText%) " +
                    "AND p.status = :status " +
                    "AND pv.price >= :minPrice " +
                    "AND pv.price <= :maxPrice",
            nativeQuery = true)
    Optional<Page<Product>> searchFullTextAndPriceRangeByStatusAndSortPriceDesc(
            @Param("searchText") String searchText,
            @Param("status") String status,
            @Param("minPrice") Long minPrice,
            @Param("maxPrice") Long maxPrice,
            Pageable pageable);


    @Query(value =
            "SELECT  p.*  " +
                    "FROM product p " +
                    "JOIN product_variant pv ON p.product_id = pv.product_id " +
                    "WHERE (MATCH(p.name) AGAINST (:searchText IN BOOLEAN MODE) OR p.name LIKE %:searchText%) " +
                    "AND p.status = :status " +
                    "AND pv.price >= :minPrice " +
                    "AND pv.price <= :maxPrice " +
                    "GROUP BY p.product_id " +
                    "ORDER BY RAND()",
            countQuery = "SELECT COUNT( DISTINCT p.product_id) " +
                    "FROM product p " +
                    "JOIN product_variant pv " +
                    "ON p.product_id = pv.product_id " +
                    "WHERE (MATCH(p.name) AGAINST (:searchText IN BOOLEAN MODE) OR p.name LIKE %:searchText%) " +
                    "AND p.status = :status " +
                    "AND pv.price >= :minPrice " +
                    "AND pv.price <= :maxPrice",
            nativeQuery = true)
    Optional<Page<Product>> searchFullTextAndPriceRangeByStatusAndSortRandomly(
            @Param("searchText") String searchText,
            @Param("status") String status,
            @Param("minPrice") Long minPrice,
            @Param("maxPrice") Long maxPrice,
            Pageable pageable);



    /////////////////////////////////////////// Price Range On Shop ///////////////////////////////////////////

    @Query(value =
            "SELECT  p.*  " +
                    "FROM product p " +
                    "JOIN shop s " +
                    "ON p.shop_id = s.shop_id " +
                    "JOIN product_variant pv " +
                    "ON p.product_id = pv.product_id " +
                    "WHERE (MATCH(p.name) AGAINST (:searchText IN BOOLEAN MODE) OR p.name LIKE %:searchText%) " +
                    "AND s.shop_id = :shopId " +
                    "AND p.status = :status " +
                    "AND pv.price >= :minPrice " +
                    "AND pv.price <= :maxPrice " +
                    "GROUP BY p.product_id " +
                    "ORDER BY p.create_at ASC",
            countQuery = "SELECT COUNT(DISTINCT p.product_id) " +
                    "FROM product p " +
                    "JOIN shop s " +
                    "ON p.shop_id = s.shop_id " +
                    "JOIN product_variant pv " +
                    "ON p.product_id = pv.product_id " +
                    "WHERE (MATCH(p.name) AGAINST (:searchText IN BOOLEAN MODE) OR p.name LIKE %:searchText%) " +
                    "AND s.shop_id = :shopId " +
                    "AND p.status = :status " +
                    "AND pv.price >= :minPrice " +
                    "AND pv.price <= :maxPrice",
            nativeQuery = true)
    Optional<Page<Product>> searchFullTextPriceRangeOnShopAndSortByNewest(
            @Param("searchText") String searchText,
            @Param("shopId") Long shopId,
            @Param("status") String status,
            @Param("minPrice") Long minPrice,
            @Param("maxPrice") Long maxPrice,
            Pageable pageable);


    @Query(value =
            "SELECT  p.*  " +
                    "FROM product p " +
                    "JOIN shop s " +
                    "ON p.shop_id = s.shop_id " +
                    "JOIN product_variant pv " +
                    "ON p.product_id = pv.product_id " +
                    "WHERE (MATCH(p.name) AGAINST (:searchText IN BOOLEAN MODE) OR p.name LIKE %:searchText%) " +
                    "AND s.shop_id = :shopId " +
                    "AND p.status = :status " +
                    "AND pv.price >= :minPrice " +
                    "AND pv.price <= :maxPrice " +
                    "GROUP BY p.product_id " +
                    "ORDER BY p.sold DESC",
            countQuery = "SELECT COUNT(DISTINCT p.product_id) " +
                    "FROM product p " +
                    "JOIN shop s " +
                    "ON p.shop_id = s.shop_id " +
                    "JOIN product_variant pv " +
                    "ON p.product_id = pv.product_id " +
                    "WHERE (MATCH(p.name) AGAINST (:searchText IN BOOLEAN MODE) OR p.name LIKE %:searchText%) " +
                    "AND s.shop_id = :shopId " +
                    "AND p.status = :status " +
                    "AND pv.price >= :minPrice " +
                    "AND pv.price <= :maxPrice",
            nativeQuery = true)
    Optional<Page<Product>> searchFullTextPriceRangeOnShopAndSortByBestSelling(
            @Param("searchText") String searchText,
            @Param("shopId") Long shopId,
            @Param("status") String status,
            @Param("minPrice") Long minPrice,
            @Param("maxPrice") Long maxPrice,
            Pageable pageable);


    @Query(value =
            "SELECT  p.* " +
                    "FROM product p " +
                    "JOIN shop s " +
                    "ON p.shop_id = s.shop_id " +
                    "JOIN product_variant pv " +
                    "ON p.product_id = pv.product_id " +
                    "WHERE (MATCH(p.name) AGAINST (:searchText IN BOOLEAN MODE) OR p.name LIKE %:searchText%) " +
                    "AND s.shop_id = :shopId " +
                    "AND p.status = :status " +
                    "AND pv.price >= :minPrice " +
                    "AND pv.price <= :maxPrice " +
                    "GROUP BY p.product_id " +
                    "ORDER BY pv.price ASC",
            countQuery = "SELECT COUNT(DISTINCT p.product_id) " +
                    "FROM product p " +
                    "JOIN shop s " +
                    "ON p.shop_id = s.shop_id " +
                    "JOIN product_variant pv " +
                    "ON p.product_id = pv.product_id " +
                    "WHERE (MATCH(p.name) AGAINST (:searchText IN BOOLEAN MODE) OR p.name LIKE %:searchText%) " +
                    "AND s.shop_id = :shopId " +
                    "AND p.status = :status " +
                    "AND pv.price >= :minPrice " +
                    "AND pv.price <= :maxPrice",
            nativeQuery = true)
    Optional<Page<Product>> searchFullTextPriceRangeOnShopAndSortByPriceAse(
            @Param("searchText") String searchText,
            @Param("shopId") Long shopId,
            @Param("status") String status,
            @Param("minPrice") Long minPrice,
            @Param("maxPrice") Long maxPrice,
            Pageable pageable);


    @Query(value =
            "SELECT  p.* " +
                    "FROM product p " +
                    "JOIN shop s " +
                    "ON p.shop_id = s.shop_id " +
                    "JOIN product_variant pv " +
                    "ON p.product_id = pv.product_id " +
                    "WHERE (MATCH(p.name) AGAINST (:searchText IN BOOLEAN MODE) OR p.name LIKE %:searchText%) " +
                    "AND s.shop_id = :shopId " +
                    "AND p.status = :status " +
                    "AND pv.price >= :minPrice " +
                    "AND pv.price <= :maxPrice " +
                    "GROUP BY p.product_id " +
                    "ORDER BY MAX(pv.price) DESC ",
            countQuery = "SELECT COUNT(DISTINCT p.product_id) " +
                    "FROM product p " +
                    "JOIN shop s " +
                    "ON p.shop_id = s.shop_id " +
                    "JOIN product_variant pv " +
                    "ON p.product_id = pv.product_id " +
                    "WHERE (MATCH(p.name) AGAINST (:searchText IN BOOLEAN MODE) OR p.name LIKE %:searchText%) " +
                    "AND s.shop_id = :shopId " +
                    "AND p.status = :status " +
                    "AND pv.price >= :minPrice " +
                    "AND pv.price <= :maxPrice",
            nativeQuery = true)
    Optional<Page<Product>> searchFullTextPriceRangeOnShopAndSortByPriceDesc(
            @Param("searchText") String searchText,
            @Param("shopId") Long shopId,
            @Param("status") String status,
            @Param("minPrice") Long minPrice,
            @Param("maxPrice") Long maxPrice,
            Pageable pageable);


    @Query(value =
            "SELECT  p.*  " +
                    "FROM product p " +
                    "JOIN shop s " +
                    "ON p.shop_id = s.shop_id " +
                    "JOIN product_variant pv " +
                    "ON p.product_id = pv.product_id " +
                    "WHERE (MATCH(p.name) AGAINST (:searchText IN BOOLEAN MODE) OR p.name LIKE %:searchText%) " +
                    "AND s.shop_id = :shopId " +
                    "AND p.status = :status " +
                    "AND pv.price >= :minPrice " +
                    "AND pv.price <= :maxPrice " +
                    "GROUP BY p.product_id " +
                    "ORDER BY RAND()",
            countQuery = "SELECT COUNT(DISTINCT p.product_id) " +
                    "FROM product p " +
                    "JOIN shop s " +
                    "ON p.shop_id = s.shop_id " +
                    "JOIN product_variant pv " +
                    "ON p.product_id = pv.product_id " +
                    "WHERE (MATCH(p.name) AGAINST (:searchText IN BOOLEAN MODE) OR p.name LIKE %:searchText%) " +
                    "AND s.shop_id = :shopId " +
                    "AND p.status = :status " +
                    "AND pv.price >= :minPrice " +
                    "AND pv.price <= :maxPrice",
            nativeQuery = true)
    Optional<Page<Product>> searchFullTextPriceRangeOnShopAndSortByRandomly(
            @Param("searchText") String searchText,
            @Param("shopId") Long shopId,
            @Param("status") String status,
            @Param("minPrice") Long minPrice,
            @Param("maxPrice") Long maxPrice,
            Pageable pageable);


    /////////////////////////////////////////// OnShop ///////////////////////////////////////////
    @Query(value =
            "SELECT p.* " +
                    "FROM product p " +
                    "JOIN shop s " +
                    "ON p.shop_id = s.shop_id " +
                    "WHERE (MATCH(p.name) AGAINST (:searchText IN BOOLEAN MODE) OR p.name LIKE %:searchText%) " +
                    "AND s.shop_id = :shopId " +
                    "AND p.status = :status " +
                    "ORDER BY p.create_at DESC",
            countQuery = "SELECT COUNT(*) " +
                    "FROM product p " +
                    "JOIN shop s " +
                    "ON p.shop_id = s.shop_id " +
                    "WHERE (MATCH(p.name) AGAINST (:searchText IN BOOLEAN MODE) OR p.name LIKE %:searchText%) " +
                    "AND s.shop_id = :shopId " +
                    "AND p.status = :status",
            nativeQuery = true)
    Optional<Page<Product>> searchFullTextOnShopByNameAndStatusAndSortByNewest(
            @Param("searchText") String searchText,
            @Param("shopId") Long shopId,
            @Param("status") String status,
            Pageable pageable);


    @Query(value =
            "SELECT p.* " +
                    "FROM product p " +
                    "JOIN shop s " +
                    "ON p.shop_id = s.shop_id " +
                    "WHERE (MATCH(p.name) AGAINST (:searchText IN BOOLEAN MODE) OR p.name LIKE %:searchText%) " +
                    "AND s.shop_id = :shopId " +
                    "AND p.status = :status " +
                    "ORDER BY p.sold DESC",
            countQuery = "SELECT COUNT(*) " +
                    "FROM product p " +
                    "JOIN shop s " +
                    "ON p.shop_id = s.shop_id " +
                    "WHERE (MATCH(p.name) AGAINST (:searchText IN BOOLEAN MODE) OR p.name LIKE %:searchText%) " +
                    "AND s.shop_id = :shopId " +
                    "AND p.status = :status",
            nativeQuery = true)
    Optional<Page<Product>> searchFullTextOnShopByNameAndStatusAndSortByBestSelling(
            @Param("searchText") String searchText,
            @Param("shopId") Long shopId,
            @Param("status") String status,
            Pageable pageable);

    @Query(value =
            "SELECT p.* " +
                    "FROM product p " +
                    "JOIN shop s " +
                    "ON p.shop_id = s.shop_id " +
                    "JOIN product_variant pv " +
                    "ON p.product_id = pv.product_id " +
                    "WHERE (MATCH(p.name) AGAINST (:searchText IN BOOLEAN MODE) OR p.name LIKE %:searchText%) " +
                    "AND s.shop_id = :shopId " +
                    "AND p.status = :status " +
                    "GROUP BY p.product_id " +
                    "ORDER BY MIN(pv.price) ASC",
            countQuery = "SELECT COUNT(DISTINCT p.product_id) " +
                    "FROM product p " +
                    "JOIN shop s " +
                    "ON p.shop_id = s.shop_id " +
                    "JOIN product_variant pv " +
                    "ON p.product_id = pv.product_id " +
                    "WHERE (MATCH(p.name) AGAINST (:searchText IN BOOLEAN MODE) OR p.name LIKE %:searchText%) " +
                    "AND s.shop_id = :shopId " +
                    "AND p.status = :status ",
            nativeQuery = true)
    Optional<Page<Product>> searchFullTextOnShopByNameAndStatusAndSortByPriceAsc(
            @Param("searchText") String searchText,
            @Param("shopId") Long shopId,
            @Param("status") String status,
            Pageable pageable);


    @Query(value =
            "SELECT  p.* " +
                    "FROM product p " +
                    "JOIN shop s " +
                    "ON p.shop_id = s.shop_id " +
                    "JOIN product_variant pv " +
                    "ON p.product_id = pv.product_id " +
                    "WHERE (MATCH(p.name) AGAINST (:searchText IN BOOLEAN MODE) OR p.name LIKE %:searchText%) " +
                    "AND s.shop_id = :shopId " +
                    "AND p.status = :status " +
                    "GROUP BY p.product_id " +
                    "ORDER BY MAX(pv.price) DESC ",
            countQuery = "SELECT COUNT(DISTINCT p.product_id) " +
                    "FROM product p " +
                    "JOIN shop s " +
                    "ON p.shop_id = s.shop_id " +
                    "JOIN product_variant pv " +
                    "ON p.product_id = pv.product_id " +
                    "WHERE (MATCH(p.name) AGAINST (:searchText IN BOOLEAN MODE) OR p.name LIKE %:searchText%) " +
                    "AND s.shop_id = :shopId " +
                    "AND p.status = :status ",
            nativeQuery = true)
    Optional<Page<Product>> searchFullTextOnShopByNameAndStatusAndSortByPriceDesc(
            @Param("searchText") String searchText,
            @Param("shopId") Long shopId,
            @Param("status") String status,
            Pageable pageable);


    @Query(value =
            "SELECT  p.* " +
                    "FROM product p " +
                    "JOIN shop s " +
                    "ON p.shop_id = s.shop_id " +
                    "WHERE (MATCH(p.name) AGAINST (:searchText IN BOOLEAN MODE) OR p.name LIKE %:searchText%) " +
                    "AND s.shop_id = :shopId " +
                    "AND p.status = :status " +
                    "GROUP BY p.product_id " +
                    "ORDER BY RAND()",
            countQuery =
                    "SELECT COUNT(DISTINCT p.product_id) " +
                            "FROM product p " +
                            "JOIN shop s " +
                            "ON p.shop_id = s.shop_id " +
                            "WHERE MATCH(p.name) AGAINST (:searchText IN BOOLEAN MODE) " +
                            "OR p.name LIKE %:searchText% " +
                            "AND s.shop_id = :shopId " +
                            "AND p.status = :status",
            nativeQuery = true)
    Optional<Page<Product>> searchFullTextOnShopByNameAndStatusAndSortRandomly(
            @Param("searchText") String searchText,
            @Param("shopId") Long shopId,
            @Param("status") String status,
            Pageable pageable);

}
