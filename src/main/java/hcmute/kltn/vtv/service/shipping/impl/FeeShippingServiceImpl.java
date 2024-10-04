package hcmute.kltn.vtv.service.shipping.impl;

import hcmute.kltn.vtv.model.data.manager.request.FeeShippingRequest;
import hcmute.kltn.vtv.model.entity.shipping.FeeShipping;
import hcmute.kltn.vtv.model.entity.shipping.TransportProvider;
import hcmute.kltn.vtv.repository.shipping.FeeShippingRepository;
import hcmute.kltn.vtv.service.location.IDistanceLocationService;
import hcmute.kltn.vtv.service.shipping.IFeeShippingService;
import hcmute.kltn.vtv.util.exception.InternalServerErrorException;
import hcmute.kltn.vtv.util.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FeeShippingServiceImpl implements IFeeShippingService {

    private final FeeShippingRepository feeShippingRepository;
    private final TransportProviderServiceImpl transportProviderServiceImpl;
    private final IDistanceLocationService distanceLocationService;


    @Override
    public Long calculateShippingCost(String shippingMethod, String customerWardCode, String shopWardCode){
        int distanceLocation = distanceLocationService.calculateDistance(customerWardCode, shopWardCode);

        TransportProvider transportProvider = transportProviderServiceImpl.getTransportProviderByShortName(shippingMethod);
        FeeShipping feeShipping = feeShippingRepository.findByTransportProvider(transportProvider)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy phí vận chuyển."));

        return switch (distanceLocation) {
            case 0 -> feeShipping.getZeroArea();
            case 1 -> feeShipping.getOneArea();
            case 2 -> feeShipping.getTwoArea();
            case 3 -> feeShipping.getThreeArea();
            default -> feeShipping.getFourArea();
        };
    }

    @Override
    @Transactional
    public  FeeShipping addFeeShippingByRequest(FeeShippingRequest request) {
        FeeShipping feeShipping = createFeeShippingByFeeShippingRequest(request);

        try {
            return feeShippingRepository.save(feeShipping);
        } catch (Exception e) {
            throw new InternalServerErrorException("Thêm phí vận chuyển thất bại. " + e.getMessage());
        }
    }


    @Override
    @Transactional
    public  FeeShipping updateFeeShippingWithTransportProvider(FeeShipping feeShipping, TransportProvider transportProvider) {
        feeShipping.setTransportProvider(transportProvider);
        try {
            return feeShippingRepository.save(feeShipping);
        } catch (Exception e) {
            throw new InternalServerErrorException("Cập nhật phí vận chuyển thất bại. " + e.getMessage());
        }
    }


    @Override
    public  FeeShipping getFeeShippingByTransportProviderShortName(String shortName) {
        TransportProvider transportProvider = transportProviderServiceImpl.getTransportProviderByShortName(shortName);
        return feeShippingRepository.findByTransportProvider(transportProvider)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy nhà vận chuyển."));
    }


    private FeeShipping createFeeShippingByFeeShippingRequest(FeeShippingRequest request) {
        FeeShipping feeShipping = new FeeShipping();
        feeShipping.setZeroArea(request.getZeroArea());
        feeShipping.setOneArea(request.getOneArea());
        feeShipping.setTwoArea(request.getTwoArea());
        feeShipping.setThreeArea(request.getThreeArea());
        feeShipping.setFourArea(request.getFourArea());

        return feeShipping;
    }


    @Override
    @Transactional
    public void updateFeeShippingByRequest(FeeShippingRequest request, Long transportProviderId) {
        TransportProvider transportProvider = transportProviderServiceImpl
                .getTransportProviderByTransportProviderId(transportProviderId);
        FeeShipping feeShipping = feeShippingRepository.findByTransportProvider(transportProvider)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy phí vận chuyển."));

        feeShipping.setZeroArea(request.getZeroArea());
        feeShipping.setOneArea(request.getOneArea());
        feeShipping.setTwoArea(request.getTwoArea());
        feeShipping.setThreeArea(request.getThreeArea());
        feeShipping.setFourArea(request.getFourArea());

       try {
            feeShippingRepository.save(feeShipping);
        } catch (Exception e) {
            throw new InternalServerErrorException("Cập nhật phí vận chuyển thất bại. " + e.getMessage());
       }
    }



}

