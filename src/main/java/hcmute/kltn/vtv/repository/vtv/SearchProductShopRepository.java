package hcmute.kltn.vtv.repository.vtv;

import hcmute.kltn.vtv.model.entity.vendor.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface SearchProductShopRepository extends JpaRepository<Product, Long> {



  /////////////////////////////////////////// OnShop ///////////////////////////////////////////
  @Query(value =
          "SELECT  p.* " +
                  "FROM product p " +
                  "JOIN shop s " +
                  "ON p.shop_id = s.shop_id " +
                  "JOIN customer c " +
                  "ON s.customer_id = c.customer_id " +
                  "WHERE MATCH(p.name) AGAINST (:searchText IN BOOLEAN MODE) " +
                  "OR p.name LIKE %:searchText% " +
                  "AND c.username = :username " +
                  "AND p.status = :status " +
                  "GROUP BY p.product_id " +
                  "ORDER BY p.create_at DESC",
          countQuery = "SELECT COUNT(*) " +
                  "FROM product p " +
                  "JOIN shop s " +
                  "ON p.shop_id = s.shop_id " +
                  "JOIN customer c " +
                  "ON s.customer_id = c.customer_id " +
                  "WHERE MATCH(p.name) AGAINST (:searchText IN BOOLEAN MODE) " +
                  "OR p.name LIKE %:searchText% " +
                  "AND c.username = :username " +
                  "AND p.status = :status",
          nativeQuery = true)
  Optional<Page<Product>> searchFullTextOnShopByUsernameAndNameAndStatusAndSortByNewest(
          @Param("searchText") String searchText,
          @Param("username") String username,
          @Param("status") String status,
          Pageable pageable);


  @Query(value =
          "SELECT  p.* " +
                  "FROM product p " +
                  "JOIN shop s " +
                  "ON p.shop_id = s.shop_id " +
                  "JOIN customer c " +
                  "ON s.customer_id = c.customer_id " +
                  "WHERE MATCH(p.name) AGAINST (:searchText IN BOOLEAN MODE) " +
                  "OR p.name LIKE %:searchText% " +
                  "AND c.username = :username " +
                  "AND p.status = :status " +
                  "GROUP BY p.product_id " +
                  "ORDER BY p.sold DESC",
          countQuery = "SELECT COUNT(*) " +
                  "FROM product p " +
                  "JOIN shop s " +
                  "ON p.shop_id = s.shop_id " +
                  "JOIN customer c " +
                  "ON s.customer_id = c.customer_id " +
                  "WHERE MATCH(p.name) AGAINST (:searchText IN BOOLEAN MODE) " +
                  "OR p.name LIKE %:searchText% " +
                  "AND c.username = :username " +
                  "AND p.status = :status",
          nativeQuery = true)
  Optional<Page<Product>> searchFullTextOnShopByUsernameAndNameAndStatusAndSortByBestSelling(
          @Param("searchText") String searchText,
          @Param("username") String username,
          @Param("status") String status,
          Pageable pageable);






  @Query(value =
          "SELECT  p.* " +
                  "FROM product p " +
                  "JOIN shop s " +
                  "ON p.shop_id = s.shop_id " +
                  "JOIN customer c " +
                  "ON s.customer_id = c.customer_id " +
                  "JOIN product_variant pv " +
                  "ON p.product_id = pv.product_id " +
                  "WHERE MATCH(p.name) AGAINST (:searchText IN BOOLEAN MODE) " +
                  "OR p.name LIKE %:searchText% " +
                  "AND c.username = :username " +
                  "AND p.status = :status " +
                  "GROUP BY p.product_id " +
                  "ORDER BY MIN(pv.price) ASC",
          countQuery = "SELECT COUNT(DISTINCT p.product_id) " +
                  "FROM product p " +
                  "JOIN shop s " +
                  "ON p.shop_id = s.shop_id " +
                  "JOIN customer c " +
                  "ON s.customer_id = c.customer_id " +
                  "JOIN product_variant pv " +
                  "ON p.product_id = pv.product_id " +
                  "WHERE MATCH(p.name) AGAINST (:searchText IN BOOLEAN MODE) " +
                  "OR p.name LIKE %:searchText% " +
                  "AND c.username = :username " +
                  "AND p.status = :status ",
          nativeQuery = true)
  Optional<Page<Product>> searchFullTextOnShopByUsernameAndNameAndStatusAndSortByPriceAsc(
          @Param("searchText") String searchText,
          @Param("username") String username,
          @Param("status") String status,
          Pageable pageable);


  @Query(value =
          "SELECT p.* " +
                  "FROM product p " +
                  "JOIN shop s " +
                  "ON p.shop_id = s.shop_id " +
                  "JOIN customer c " +
                  "ON s.customer_id = c.customer_id " +
                  "JOIN product_variant pv " +
                  "ON p.product_id = pv.product_id " +
                  "WHERE MATCH(p.name) AGAINST (:searchText IN BOOLEAN MODE) " +
                  "OR p.name LIKE %:searchText% " +
                  "AND c.username = :username " +
                  "AND p.status = :status " +
                  "GROUP BY p.product_id " +
                  "ORDER BY MAX(pv.price) DESC ",
          countQuery = "SELECT COUNT(DISTINCT p.product_id) " +
                  "FROM product p " +
                  "JOIN shop s " +
                  "ON p.shop_id = s.shop_id " +
                  "JOIN customer c " +
                  "ON s.customer_id = c.customer_id " +
                  "JOIN product_variant pv " +
                  "ON p.product_id = pv.product_id " +
                  "WHERE MATCH(p.name) AGAINST (:searchText IN BOOLEAN MODE) " +
                  "OR p.name LIKE %:searchText% " +
                  "AND c.username = :username " +
                  "AND p.status = :status ",
          nativeQuery = true)
  Optional<Page<Product>> searchFullTextOnShopByUsernameAndNameAndStatusAndSortByPriceDesc(
          @Param("searchText") String searchText,
          @Param("username") String username,
          @Param("status") String status,
          Pageable pageable);


  @Query(value =
          "SELECT p.* " +
                  "FROM product p " +
                  "JOIN shop s " +
                  "ON p.shop_id = s.shop_id " +
                  "JOIN customer c " +
                  "ON s.customer_id = c.customer_id " +
                  "WHERE MATCH(p.name) AGAINST (:searchText IN BOOLEAN MODE) " +
                  "OR p.name LIKE %:searchText% " +
                  "AND c.username = :username " +
                  "AND p.status = :status " +
                  "GROUP BY p.product_id " +
                  "ORDER BY RAND()",
          countQuery =
                  "SELECT COUNT(DISTINCT p.product_id) " +
                          "FROM product p " +
                          "JOIN shop s " +
                          "ON p.shop_id = s.shop_id " +
                          "JOIN customer c " +
                          "ON s.customer_id = c.customer_id " +
                          "WHERE MATCH(p.name) AGAINST (:searchText IN BOOLEAN MODE) " +
                          "OR p.name LIKE %:searchText% " +
                          "AND c.username = :username " +
                          "AND p.status = :status",
          nativeQuery = true)
  Optional<Page<Product>> searchFullTextOnShopByUsernameAndNameAndStatusAndSortRandomly(
          @Param("searchText") String searchText,
          @Param("username") String username,
          @Param("status") String status,
          Pageable pageable);


}