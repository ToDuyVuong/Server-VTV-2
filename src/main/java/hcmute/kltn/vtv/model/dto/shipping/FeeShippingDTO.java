package hcmute.kltn.vtv.model.dto.shipping;

import hcmute.kltn.vtv.model.entity.shipping.FeeShipping;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class FeeShippingDTO implements Serializable {
    private Long feeShippingId;

    private Long zeroArea;

    private int zeroEstimatedDeliveryTime;

    private Long oneArea;

    private int oneEstimatedDeliveryTime;

    private Long twoArea;

    private int twoEstimatedDeliveryTime;

    private Long threeArea;

    private int threeEstimatedDeliveryTime;

    private Long fourArea;

    private int fourEstimatedDeliveryTime;

    private Long transportProviderId;

    public static FeeShippingDTO convertEntityToDTO(FeeShipping feeShipping) {
        FeeShippingDTO feeShippingDTO = new FeeShippingDTO();
        feeShippingDTO.setFeeShippingId(feeShipping.getFeeShippingId());
        feeShippingDTO.setZeroArea(feeShipping.getZeroArea());
        feeShippingDTO.setZeroEstimatedDeliveryTime(feeShipping.getZeroEstimatedDeliveryTime());
        feeShippingDTO.setOneArea(feeShipping.getOneArea());
        feeShippingDTO.setOneEstimatedDeliveryTime(feeShipping.getOneEstimatedDeliveryTime());
        feeShippingDTO.setTwoArea(feeShipping.getTwoArea());
        feeShippingDTO.setTwoEstimatedDeliveryTime(feeShipping.getTwoEstimatedDeliveryTime());
        feeShippingDTO.setThreeArea(feeShipping.getThreeArea());
        feeShippingDTO.setThreeEstimatedDeliveryTime(feeShipping.getThreeEstimatedDeliveryTime());
        feeShippingDTO.setFourArea(feeShipping.getFourArea());
        feeShippingDTO.setFourEstimatedDeliveryTime(feeShipping.getFourEstimatedDeliveryTime());
        return feeShippingDTO;
    }

}