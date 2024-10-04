package hcmute.kltn.vtv.service.shipping;

import hcmute.kltn.vtv.model.entity.location.Ward;
import hcmute.kltn.vtv.model.entity.vendor.Shop;

import java.util.List;

public interface ITransportShopService {
    List<Shop> getShopsByWardCode(String wardCode);


    List<Shop> getShopsByWards(List<Ward> wards);
}
