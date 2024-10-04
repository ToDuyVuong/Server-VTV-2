package hcmute.kltn.vtv.repository.user;

import hcmute.kltn.vtv.model.entity.user.Order;
import hcmute.kltn.vtv.model.entity.vendor.Product;
import hcmute.kltn.vtv.model.extra.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {
    boolean existsByOrderId(UUID orderId);

    boolean existsByOrderIdAndShopShopId(UUID orderId, Long shopId);

    // check updateAt after date
    boolean existsByOrderIdAndUpdateAtAfterAndStatus(UUID orderId, LocalDateTime updateAt, OrderStatus status);

    boolean existsByOrderIdAndCustomerUsername(UUID orderId, String username);

    boolean existsByOrderIdAndStatus(UUID orderId, OrderStatus status);


    Optional<Order> findByOrderId(UUID orderId);

    Optional<List<Order>> findAllByStatus(OrderStatus status);

    Optional<List<Order>> findAllByPaymentMethodAndStatus(String paymentMethod, OrderStatus status);

    Optional<List<Order>> findAllByCustomerUsername(String username);

    Optional<List<Order>> findAllByCustomerUsernameAndStatus(String username, OrderStatus status);


    Optional<Order> findByOrderIdAndShopShopId(UUID orderId, Long shopId);

    Optional<Order> findByOrderIdAndCustomerUsername(UUID orderId, String username);

    Optional<List<Order>> findAllByShopShopId(Long shopId);

    Optional<List<Order>> findAllByShopShopIdAndStatus(Long shopId, OrderStatus status);

    Optional<List<Order>> findAllByShopShopIdAndOrderDateBetween(Long shopId, Date startOrderDate, Date endOrderDate);

    Optional<List<Order>> findAllByShopShopIdAndOrderDateBetweenAndStatus(Long shopId, Date startOrderDate,
                                                                          Date endOrderDate, OrderStatus status);


    Optional<List<Order>> findAllByShopShopIdAndStatusAndOrderDateBetween(Long shopId, OrderStatus status, Date startDate,
                                                                          Date endDate);

    Optional<List<Order>> findAllByShippingMethodAndOrderDateBetween(String shippingMethod, Date startDate, Date endDate);


    Optional<List<Order>> findAllByStatusAndShippingMethodAndOrderDateBetween(OrderStatus status, String shippingMethod, Date startDate, Date endDate);

    Optional<List<Order>> findAllByStatusAndOrderDateBetween(OrderStatus status, Date startDate, Date endDate);

    Optional<Page<Order>> findAllByStatusAndOrderDateBetweenOrderByUpdateAtDesc(OrderStatus status, Date startDate, Date endDate, PageRequest pageRequest);

    Optional<Page<Order>> findAllByShopShopIdOrderByCreateAtDesc(Long shopId, PageRequest pageRequest);

    Optional<Page<Order>> findAllByShopShopIdAndStatusOrderByCreateAtDesc(Long shopId, OrderStatus status,
                                                                          PageRequest pageRequest);


    Optional<List<Order>> findAllByPaymentMethodAndStatusAndUpdateAtBetween(String paymentMethod, OrderStatus status, LocalDateTime startUpdateAt, LocalDateTime endUpdateAt);


    @Query(value =
            "SELECT  o.* " +
                    "FROM product p " +
                    "JOIN product_variant pv ON p.product_id = pv.product_id " +
                    "JOIN cart c ON pv.product_variant_id = c.product_variant_id " +
                    "JOIN order_item oi ON c.cart_id = oi.cart_id " +
                    "JOIN `order` o ON oi.order_id = o.order_id " +
                    "WHERE p.product_id = :productId " +
                    "AND o.status = :status " +
                    "AND o.update_at BETWEEN :startDate AND :endDate ",
            nativeQuery = true)
    Optional<List<Order>> getRevenueProductResponseByProductIdAndDateAndOrderStatus(Long productId, Date startDate, Date endDate, String status);

}
