package hcmute.kltn.vtv.repository.shipping;

import hcmute.kltn.vtv.model.entity.shipping.Deliver;
import hcmute.kltn.vtv.model.entity.shipping.TransportProvider;
import hcmute.kltn.vtv.model.extra.Status;
import hcmute.kltn.vtv.model.extra.TypeWork;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DeliverRepository extends JpaRepository<Deliver, Long> {

    boolean existsByCustomerCustomerId(Long customerId);

    boolean existsByPhoneAndDeliverIdNot(String phone, Long deliverId);

    boolean existsByDeliverId(Long deliverId);

    boolean existsByPhone(String phone);


    boolean existsByCustomerUsername(String username);

    Optional<Deliver> findByCustomerUsernameAndStatus(String username, Status status);

    Optional<List<Deliver>> findAllByStatusAndTransportProvider(Status status, TransportProvider transportProvider);

    Optional<List<Deliver>> findAllByStatusAndTypeWorkAndTransportProvider(Status status, TypeWork typeWork, TransportProvider transportProvider);

    Optional<Deliver> findByDeliverId(Long deliverId);

    Optional<Deliver> findByDeliverIdAndTransportProvider(Long deliverId, TransportProvider transportProvider);

    Optional<Deliver> findByCustomerUsername(String username);



    Optional<Deliver> findByPhone(String phone);



}
