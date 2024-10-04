package hcmute.kltn.vtv.service.shipping.impl;

import hcmute.kltn.vtv.model.entity.shipping.FeeShipping;
import hcmute.kltn.vtv.service.shipping.IFeeShippingService;
import hcmute.kltn.vtv.service.shipping.IShippingTimeEstimationService;
import hcmute.kltn.vtv.util.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class ShippingTimeEstimationServiceImpl implements IShippingTimeEstimationService {

    private final IFeeShippingService feeShippingService;


    @Override
    public Date estimateDeliveryTime(int distance, String shippingProvider) {
        if (distance < 0 || distance > 4) {
            throw new BadRequestException("Khoảng cách không hợp lệ");
        }

        FeeShipping feeShipping = feeShippingService.getFeeShippingByTransportProviderShortName(shippingProvider);

        return switch (distance) {
            case 0 -> estimatedDeliveryTime(feeShipping.getZeroEstimatedDeliveryTime(), LocalDateTime.now());
            case 1 -> estimatedDeliveryTime(feeShipping.getOneEstimatedDeliveryTime(), LocalDateTime.now());
            case 2 -> estimatedDeliveryTime(feeShipping.getTwoEstimatedDeliveryTime(), LocalDateTime.now());
            case 3 -> estimatedDeliveryTime(feeShipping.getThreeEstimatedDeliveryTime(), LocalDateTime.now());
            default -> estimatedDeliveryTime(feeShipping.getFourEstimatedDeliveryTime(), LocalDateTime.now());
        };
    }

    @Override
    public Date estimateDeliveryTimeByOrderDate(int distance, String shippingProvider, LocalDateTime orderDate) {
        if (distance < 0 || distance > 4) {
            throw new BadRequestException("Khoảng cách không hợp lệ");
        }

        FeeShipping feeShipping = feeShippingService.getFeeShippingByTransportProviderShortName(shippingProvider);

        return switch (distance) {
            case 0 -> estimatedDeliveryTime(feeShipping.getZeroEstimatedDeliveryTime(), orderDate);
            case 1 -> estimatedDeliveryTime(feeShipping.getOneEstimatedDeliveryTime(), orderDate);
            case 2 -> estimatedDeliveryTime(feeShipping.getTwoEstimatedDeliveryTime(), orderDate);
            case 3 -> estimatedDeliveryTime(feeShipping.getThreeEstimatedDeliveryTime(), orderDate);
            default -> estimatedDeliveryTime(feeShipping.getFourEstimatedDeliveryTime(), orderDate);
        };
    }

    private Date estimatedDeliveryTime(int additionalDays, LocalDateTime orderDate) {
        LocalDate today = orderDate.toLocalDate();

        return Date.from(today.plusDays(additionalDays).atTime(LocalTime.MAX).atZone(ZoneId.systemDefault()).toInstant());
    }


    private Date calculateDeliveryTime(int distance, int additionalDays) {
        if (distance < 0 || distance > 4) {
            throw new BadRequestException("Khoảng cách không hợp lệ");
        }

        LocalDate today = LocalDate.now();

        return Date.from(today.plusDays(distance + additionalDays).atTime(LocalTime.MAX).atZone(ZoneId.systemDefault()).toInstant());
    }
}
