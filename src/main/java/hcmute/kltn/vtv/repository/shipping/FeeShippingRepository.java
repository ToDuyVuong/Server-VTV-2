package hcmute.kltn.vtv.repository.shipping;

import hcmute.kltn.vtv.model.entity.shipping.FeeShipping;
import hcmute.kltn.vtv.model.entity.shipping.TransportProvider;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FeeShippingRepository extends JpaRepository<FeeShipping, Long> {



    Optional<FeeShipping> findByTransportProvider(TransportProvider transportProvider);

}