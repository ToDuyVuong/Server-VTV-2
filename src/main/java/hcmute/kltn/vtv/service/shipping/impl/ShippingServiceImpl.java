package hcmute.kltn.vtv.service.shipping.impl;

import hcmute.kltn.vtv.model.data.shipping.response.ListShippingResponse;
import hcmute.kltn.vtv.model.data.shipping.response.ShippingResponse;
import hcmute.kltn.vtv.model.dto.shipping.ShippingDTO;
import hcmute.kltn.vtv.model.entity.location.Province;
import hcmute.kltn.vtv.model.entity.shipping.TransportProvider;
import hcmute.kltn.vtv.repository.shipping.TransportProviderRepository;
import hcmute.kltn.vtv.service.location.IDistanceLocationService;
import hcmute.kltn.vtv.service.location.IProvinceService;
import hcmute.kltn.vtv.service.shipping.IFeeShippingService;
import hcmute.kltn.vtv.service.shipping.IShippingService;
import hcmute.kltn.vtv.service.shipping.IShippingTimeEstimationService;
import hcmute.kltn.vtv.service.shipping.ITransportProviderService;

import hcmute.kltn.vtv.util.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ShippingServiceImpl implements IShippingService {

    private final IDistanceLocationService distanceLocationService;
    private final IShippingTimeEstimationService deliveryTimeEstimationService;
    private final ITransportProviderService transportProviderService;
    private final IProvinceService provinceService;
    private final IFeeShippingService feeShippingService;
    private final TransportProviderRepository transportProviderRepository;


    @Override
    public ShippingResponse getCalculateShippingByWardAndTransportProvider(String wardCodeCustomer, String wardCodeShop, String shippingProvider) {


        ShippingDTO shippingDTO = calculateShippingDTO(wardCodeCustomer, wardCodeShop, shippingProvider);

        return ShippingResponse.shippingResponse(shippingDTO, "Tính phí vận chuyển của " + shippingProvider + " thành công.", "OK");
    }

    public ShippingDTO getShippingByWardAndTransportProviderAndOrderDate(String wardCodeCustomer, String wardCodeShop, String shippingProvider, LocalDateTime dateOrder) {
        ShippingDTO shippingDTO = calculateShippingDTO(wardCodeCustomer, wardCodeShop, shippingProvider);
        return shippingDTO;
    }


    @Override
    public ListShippingResponse getShippingProvidersByWard(String wardCodeCustomer, String wardCodeShop) {

        Province provinceCustomer = provinceService.getProvinceByWardCode(wardCodeCustomer);
        Province provinceShop = provinceService.getProvinceByWardCode(wardCodeShop);
        List<TransportProvider> transportProviders = transportProviderService
                .getTransportProvidersByProvince(provinceShop.getProvinceCode(), provinceCustomer.getProvinceCode());
        List<ShippingDTO> shippingDTOs = calculateShippingDTOs(transportProviders, wardCodeCustomer, wardCodeShop);
        return ListShippingResponse.listShippingResponse(shippingDTOs, "Lấy danh sách nhà vận chuyển và tính phí vận chuyển thành công.", "OK");
    }


    @Override
    public void checkShippingProvider(String shippingProvider) {

        List<TransportProvider> transportProviders = transportProviderRepository.findAll();
        for (TransportProvider transportProvider : transportProviders) {
            if (transportProvider.getShortName().equals(shippingProvider)) {
                return;
            }
        }
        String shortNames =  transportProviders.stream().map(TransportProvider::getShortName).reduce("", (a, b) -> a + ", " + b);
        String message = "Nhà vận chuyển không tồn tại. Các nhà vận chuyển hiện có: " + shortNames;
        throw new BadRequestException(message);

    }


    private List<ShippingDTO> calculateShippingDTOs(List<TransportProvider> transportProviders, String wardCodeCustomer, String wardCodeShop) {
        List<ShippingDTO> shippingDTOs = new ArrayList<>();
        for (TransportProvider transportProvider : transportProviders) {
            ShippingDTO shippingDTO = calculateShippingDTO(wardCodeCustomer, wardCodeShop, transportProvider.getShortName());
            shippingDTOs.add(shippingDTO);
        }

        return shippingDTOs;
    }


    private ShippingDTO calculateShippingDTO(String wardCodeCustomer, String wardCodeShop, String shippingProvider) {

        int distance = distanceLocationService.calculateDistance(wardCodeCustomer, wardCodeShop);
        Date estimatedDeliveryTime = deliveryTimeEstimationService.estimateDeliveryTime(distance, shippingProvider);
        Long shippingCost = feeShippingService.calculateShippingCost(shippingProvider, wardCodeCustomer, wardCodeShop);
        TransportProvider transportProvider = transportProviderService.getTransportProviderByShortName(shippingProvider);

        return ShippingDTO.createShippingDTO(transportProvider, shippingCost,  estimatedDeliveryTime, new Date());
    }


    @Override
    public ShippingDTO getAndCalculateShippingDTO(String wardCodeCustomer, String wardCodeShop, String shippingProvider, LocalDateTime dateOrder) {
        int distance = distanceLocationService.calculateDistance(wardCodeCustomer, wardCodeShop);
        Long shippingCost = feeShippingService.calculateShippingCost(shippingProvider, wardCodeCustomer, wardCodeShop);
        TransportProvider transportProvider = transportProviderService.getTransportProviderByShortName(shippingProvider);
        Date estimatedDeliveryTime = deliveryTimeEstimationService.estimateDeliveryTimeByOrderDate(distance, shippingProvider, dateOrder);
        Date orderDateDate = Date.from(dateOrder.atZone(ZoneId.systemDefault()).toInstant());

        return ShippingDTO.createShippingDTO(transportProvider, shippingCost,  estimatedDeliveryTime, orderDateDate);
    }




}
