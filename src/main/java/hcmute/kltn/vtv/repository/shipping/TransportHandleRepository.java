package hcmute.kltn.vtv.repository.shipping;

import hcmute.kltn.vtv.model.entity.shipping.TransportHandle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TransportHandleRepository extends JpaRepository<TransportHandle, UUID> {
    Optional<List<TransportHandle>> findAllByTransportTransportId(UUID transportId);
}
