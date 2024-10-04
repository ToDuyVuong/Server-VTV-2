package hcmute.kltn.vtv.service.shipping;

import hcmute.kltn.vtv.model.data.shipping.response.DeliverResponse;
import hcmute.kltn.vtv.model.entity.shipping.Deliver;
import hcmute.kltn.vtv.model.entity.shipping.TransportProvider;
import hcmute.kltn.vtv.model.extra.TransportStatus;
import hcmute.kltn.vtv.model.extra.TypeWork;

public interface IDeliverService {
    DeliverResponse getDeliverResponseByUsername(String username);

    Deliver checkTypeWorkDeliverWithTransportStatus(String username, TransportStatus transportStatus);

    Deliver getDeliverByUsername(String username);

    Deliver getDeliverByDeliverId(Long deliverId);

    Deliver getDeliverByDeliverIdAndTransportProvider(Long deliverId, TransportProvider transportProvider);

    void checkTypeWorkWithManager(Deliver deliver);

    void checkDeliverUseRole(String username, TypeWork typeWork);

    boolean checkBooleanDeliverUseRole(String username, TypeWork typeWork);

    void checkExistByTypeWorkShipperByUsername(String username);

    void checkExistByTypeWorkWarehouseByUsername(String username);
}
