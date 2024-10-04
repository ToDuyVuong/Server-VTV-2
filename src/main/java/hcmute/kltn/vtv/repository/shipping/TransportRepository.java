package hcmute.kltn.vtv.repository.shipping;

import hcmute.kltn.vtv.model.entity.shipping.Transport;
import hcmute.kltn.vtv.model.extra.TransportStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TransportRepository extends JpaRepository<Transport, UUID> {

    Optional<Transport> findByTransportId(UUID transportId);

    Optional<List<Transport>> findAllByShopIdAndStatus(Long shopId, TransportStatus status);

    Optional<Transport> findByOrderId(UUID orderId);

    Optional<List<Transport>> findAllByShopIdInAndStatus(List<Long> shopIds, TransportStatus status);


    Optional<List<Transport>> findAllByShippingMethodAndUpdateAtBetween(String shippingMethod, LocalDateTime startDate, LocalDateTime endDate);


    boolean existsByTransportIdAndStatus(UUID transportId, TransportStatus transportStatus);

    Optional<Page<Transport>> findAllByShippingMethodAndStatusAndCreateAtBetween(String shippingMethod, TransportStatus status, LocalDateTime createAtStart, LocalDateTime createAtEnd, Pageable pageable);

}
