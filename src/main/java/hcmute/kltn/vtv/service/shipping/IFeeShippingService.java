package hcmute.kltn.vtv.service.shipping;

import hcmute.kltn.vtv.model.data.manager.request.FeeShippingRequest;
import hcmute.kltn.vtv.model.entity.shipping.FeeShipping;
import hcmute.kltn.vtv.model.entity.shipping.TransportProvider;
import org.springframework.transaction.annotation.Transactional;

public interface IFeeShippingService {
    Long calculateShippingCost(String shippingMethod, String customerWardCode, String shopWardCode);

    @Transactional
    FeeShipping addFeeShippingByRequest(FeeShippingRequest request);

    @Transactional
    FeeShipping updateFeeShippingWithTransportProvider(FeeShipping feeShipping, TransportProvider transportProvider);

    FeeShipping getFeeShippingByTransportProviderShortName(String shortName);

    @Transactional
    void updateFeeShippingByRequest(FeeShippingRequest request, Long transportProviderId);
}
