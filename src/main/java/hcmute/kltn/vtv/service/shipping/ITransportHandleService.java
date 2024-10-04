package hcmute.kltn.vtv.service.shipping;

import hcmute.kltn.vtv.model.data.shipping.request.TransportHandleRequest;
import hcmute.kltn.vtv.model.entity.shipping.TransportHandle;
import hcmute.kltn.vtv.model.extra.TransportStatus;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

public interface ITransportHandleService {
    @Transactional
    void addNewTransportHandle(TransportHandleRequest transportHandleRequest);

    @Transactional
    void addNewTransportHandleByOrderId(UUID orderId, String wardCode, String username, boolean handled, TransportStatus transportStatus);

    List<TransportHandle> getAllTransportHandleByTransportId(UUID transportId);
}
