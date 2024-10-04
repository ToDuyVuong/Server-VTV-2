package hcmute.kltn.vtv.repository.shipping;

import hcmute.kltn.vtv.model.entity.shipping.CashOrder;
import hcmute.kltn.vtv.model.extra.Status;
import hcmute.kltn.vtv.model.extra.TransportStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CashOrderRepository extends JpaRepository<CashOrder, UUID> {

    boolean existsByTransportId(UUID transportId);
    boolean existsByCashOrderIdAndShipperUsername(UUID cashOrderId, String shipperUsername);

    boolean existsByCashOrderIdAndWaveHouseUsername(UUID cashOrderId, String waveHouseUsername);

    boolean existsByWaveHouseUsernameNotNull();

    boolean existsByTransportIdAndWaveHouseUsernameNotNull(UUID transportId);

    boolean existsAllByCashOrderIdInAndShipperUsernameAndStatus(List<UUID> transportIds, String shipperUsername, Status status);

    boolean existsAllByCashOrderIdInAndWaveHouseUsernameAndStatus(List<UUID> transportIds, String waveHouseUsername, Status status);


    boolean existsByTransportIdAndShipperHoldAndWaveHouseHoldAndHandlePayment(UUID transportId, boolean shipperHold, boolean waveHouseHold, boolean handlePayment);

    boolean existsByOrderIdAndShipperHoldAndWaveHouseHoldAndHandlePaymentAndStatus(UUID orderId, boolean shipperHold, boolean waveHouseHold, boolean handlePayment, Status status);

    boolean existsAllByCashOrderIdInAndShipperUsernameAndWaveHouseHoldAndShipperHoldAndHandlePaymentAndStatus(List<UUID> transportIds, String shipperUsername, boolean waveHouseHold, boolean shipperHold, boolean handlePayment, Status status);


    boolean existsAllByCashOrderIdInAndWaveHouseUsernameAndWaveHouseHoldAndShipperHoldAndHandlePaymentAndStatus(List<UUID> transportIds, String waveHouseUsername, boolean waveHouseHold, boolean shipperHold, boolean handlePayment, Status status);


    Optional<CashOrder> findByTransportId(UUID transportId);

    Optional<CashOrder> findByOrderId(UUID orderId);

    Optional<List<CashOrder>> findAllByCashOrderIdInAndStatus(List<UUID> transportIds, Status status);

    Optional<List<CashOrder>> findAllByShipperUsernameAndStatus(String shipperUsername, Status status);

    Optional<List<CashOrder>> findAllByWaveHouseUsernameAndStatus(String waveHouseUsername,  Status status);

    Optional<List<CashOrder>> findAllByShipperUsername(String shipperUsername);

    Optional<List<CashOrder>> findAllByShipperUsernameAndShipperHoldAndWaveHouseUsernameNull(String shipperUsername, boolean shipperHold);


    @Query(value =
            "SELECT c.* " +
                    "FROM cash_order c " +
                    "JOIN transport t ON c.transport_id = t.transport_id " +
                    "WHERE c.shipper_username = :shipperUsername " +
                    "AND c.shipper_hold = :shipperHold " +
                    "AND t.status = :status " +
                    "AND c.wave_house_username IS NULL",
            nativeQuery = true)
    Optional<List<CashOrder>> getAllByShipperUsernameAndShipperHoldAndWaveHouseUsernameNullAndTransportStatus(
            @Param("shipperUsername") String shipperUsername,
            @Param("shipperHold") boolean shipperHold,
            @Param("status") String status
    );


    Optional<List<CashOrder>> findAllByShipperUsernameAndShipperHoldAndWaveHouseUsernameNullAndStatus(String shipperUsername, boolean shipperHold, Status status);

    Optional<List<CashOrder>> findAllByShipperUsernameAndShipperHoldAndWaveHouseUsernameNotNull(String shipperUsername, boolean shipperHold);

    Optional<List<CashOrder>> findAllByWaveHouseUsernameAndWaveHouseHoldAndShipperHoldAndHandlePaymentAndStatus(String waveHouseUsername, boolean waveHouseHold, boolean shipperHold, boolean handlePayment, Status status);


    boolean existsByOrderIdAndShipperUsernameAndStatus(UUID orderId, String shipperUsername, Status status);

    boolean existsByOrderIdAndShipperUsernameAndShipperHoldAndStatus(UUID orderId, String shipperUsername, boolean shipperHold, Status status);
}