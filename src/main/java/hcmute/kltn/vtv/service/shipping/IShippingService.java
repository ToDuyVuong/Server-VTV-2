package hcmute.kltn.vtv.service.shipping;

import hcmute.kltn.vtv.model.data.shipping.response.ListShippingResponse;
import hcmute.kltn.vtv.model.data.shipping.response.ShippingResponse;
import hcmute.kltn.vtv.model.dto.shipping.ShippingDTO;

import java.time.LocalDateTime;

public interface IShippingService {
    ShippingResponse getCalculateShippingByWardAndTransportProvider(String wardCodeCustomer, String wardCodeShop, String shippingProvider);

    ListShippingResponse getShippingProvidersByWard(String wardCodeCustomer, String wardCodeShop);

    void checkShippingProvider(String shippingProvider);

    ShippingDTO getAndCalculateShippingDTO(String wardCodeCustomer, String wardCodeShop, String shippingProvider, LocalDateTime dateOrder);
}
