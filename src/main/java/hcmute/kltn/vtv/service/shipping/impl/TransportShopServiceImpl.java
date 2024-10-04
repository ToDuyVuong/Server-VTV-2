package hcmute.kltn.vtv.service.shipping.impl;

import hcmute.kltn.vtv.model.entity.location.Ward;
import hcmute.kltn.vtv.model.entity.vendor.Shop;
import hcmute.kltn.vtv.model.extra.Status;
import hcmute.kltn.vtv.repository.vtv.ShopRepository;
import hcmute.kltn.vtv.service.shipping.ITransportShopService;
import hcmute.kltn.vtv.util.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class TransportShopServiceImpl implements ITransportShopService {

    private final ShopRepository shopRepository;


    @Override
    public List<Shop> getShopsByWardCode(String wardCode) {

        return shopRepository.findAllByWardWardCodeAndStatus(wardCode, Status.ACTIVE)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy cửa hàng nào tại phường: " + wardCode));
    }

    @Override
    public List<Shop> getShopsByWards(List<Ward> wards) {

        return shopRepository.findAllByWardInAndStatus(wards, Status.ACTIVE)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy cửa hàng nào tại các phường này!"));
    }

}
