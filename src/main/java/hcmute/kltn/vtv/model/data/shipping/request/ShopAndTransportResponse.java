package hcmute.kltn.vtv.model.data.shipping.request;


import hcmute.kltn.vtv.model.dto.location.WardDTO;
import hcmute.kltn.vtv.model.dto.shipping.ShopAndTransportsDTO;
import hcmute.kltn.vtv.model.entity.location.Ward;
import hcmute.kltn.vtv.model.entity.shipping.Transport;
import hcmute.kltn.vtv.model.entity.vendor.Shop;
import hcmute.kltn.vtv.model.extra.ResponseAbstract;
import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
public class ShopAndTransportResponse extends ResponseAbstract {

    private int countShop;
    private int countTransport;
    private int countWard;
    private List<WardDTO> wardDTOs;
    private List<ShopAndTransportsDTO> shopAndTransportsDTOs;

    public static ShopAndTransportResponse shopAndTransportResponse(List<Shop> shops, List<Ward> wards, List<Transport> transports, String message) {
        ShopAndTransportResponse response = new ShopAndTransportResponse();
        response.setCountShop(shops.size());
        response.setCountTransport(transports.size());
        response.setCountWard(wards.size());
        response.setWardDTOs(WardDTO.convertEntitiesToDTOs(wards));
        response.setShopAndTransportsDTOs(ShopAndTransportsDTO.shopAndTransportsDTOs(shops, transports));
        response.setMessage(message);
        response.setStatus("OK");
        response.setCode(200);

        return response;
    }


}
