package hcmute.kltn.vtv.service.shipping;

import hcmute.kltn.vtv.model.data.shipping.request.ShopAndTransportResponse;
import hcmute.kltn.vtv.model.data.shipping.response.TransportResponse;
import hcmute.kltn.vtv.model.dto.shipping.ShopAndTransportsDTO;
import hcmute.kltn.vtv.model.entity.shipping.Transport;
import hcmute.kltn.vtv.model.extra.TransportStatus;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

public interface ITransportService {


    @Transactional
    Transport addNewTransport(UUID orderId);

    @Transactional
    Transport updateStatusTransport(Transport transport, String wardCode, String username,
                                    boolean handled, TransportStatus transportStatus);

    Transport updateStatusTransportByOrderId(UUID orderId, String wardCode, String username,
                                             boolean handled, TransportStatus transportStatus);

    Transport updateStatusTransportByTransportId(UUID transportId, String wardCode, String username,
                                                 boolean handled, TransportStatus transportStatus);

    Transport getTransportById(UUID transportId);

    Transport getTransportByOrderId(UUID orderId);

    @Transactional
    TransportResponse updateTransportStatusByDeliver(UUID transportId, String username, boolean handled, TransportStatus transportStatus, String wardCode);

    @Transactional
    TransportResponse updateTransportStatusWithReturnOrderByDeliver(UUID transportId, String username, boolean handled,
                                                                    TransportStatus transportStatus, String wardCode);

    @Transactional
    TransportResponse updateTransportStatusWithCancelReturnOrderByDeliver(UUID transportId, String username, String reason);

    @Transactional
    TransportResponse updateTransportStatusWithSuccessReturnOrderByDeliver(UUID transportId, String username, String reason);

    @Transactional
    TransportResponse updateReturnTransportByWarehouse(String username, UUID transportId, String reason);

    TransportResponse getTransportResponseByTransportId(UUID transportId);

    ShopAndTransportResponse getTransportsByWardWorksDeliver(String username);

    ShopAndTransportResponse getTransportsByDistrictCode(String districtCode);

    ShopAndTransportResponse  getTransportsByWard(String wardCode, String username) ;

    ShopAndTransportsDTO getShopAndTransportsDTOByShopId(Long shopId, TransportStatus status);
}
