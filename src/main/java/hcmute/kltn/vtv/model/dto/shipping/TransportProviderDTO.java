package hcmute.kltn.vtv.model.dto.shipping;

import com.fasterxml.jackson.annotation.JsonIgnore;
import hcmute.kltn.vtv.model.dto.location.ProvinceDTO;
import hcmute.kltn.vtv.model.entity.shipping.TransportProvider;
import hcmute.kltn.vtv.model.entity.user.Customer;
import hcmute.kltn.vtv.model.extra.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransportProviderDTO {

    private Long transportProviderId;

    private String fullName;

    private String shortName;

    private String email;

    private String phone;

    private String usernameAdded;

    private Status status;

    private Long customerId;

    private int countProvince;

    private FeeShippingDTO feeShippingDTO;

    private List<ProvinceDTO> provinceDTOs;

    public static TransportProviderDTO convertEntityToDTO(TransportProvider transportProvider) {
        TransportProviderDTO dto = new TransportProviderDTO();
        dto.setTransportProviderId(transportProvider.getTransportProviderId());
        dto.setFullName(transportProvider.getFullName());
        dto.setShortName(transportProvider.getShortName());
        dto.setEmail(transportProvider.getEmail());
        dto.setPhone(transportProvider.getPhone());
        dto.setUsernameAdded(transportProvider.getUsernameAdded());
        dto.setStatus(transportProvider.getStatus());
        dto.setCustomerId(transportProvider.getCustomer().getCustomerId());
        dto.setCountProvince(transportProvider.getProvinces().size());
        dto.setFeeShippingDTO(FeeShippingDTO.convertEntityToDTO(transportProvider.getFeeShipping()));
        dto.setProvinceDTOs(ProvinceDTO.convertEntitiesToDTOs(transportProvider.getProvinces()));

        return dto;
    }

    public static TransportProviderDTO convertNotProvinceEntityToDTO(TransportProvider transportProvider) {
        TransportProviderDTO dto = new TransportProviderDTO();
        dto.setTransportProviderId(transportProvider.getTransportProviderId());
        dto.setFullName(transportProvider.getFullName());
        dto.setShortName(transportProvider.getShortName());
        dto.setEmail(transportProvider.getEmail());
        dto.setPhone(transportProvider.getPhone());
        dto.setCountProvince(transportProvider.getProvinces().size());
        dto.setProvinceDTOs(null);
        return dto;
    }

    public static List<TransportProviderDTO> convertEntitiesToDTOs(List<TransportProvider> transportProviders) {
        List<TransportProviderDTO> dtos = new ArrayList<>();
        for (TransportProvider transportProvider : transportProviders) {
            dtos.add(convertEntityToDTO(transportProvider));
        }
        dtos.sort(Comparator.comparing(TransportProviderDTO::getFullName));

        return dtos;
    }

    public static List<TransportProviderDTO> convertNotProvinceEntitiesToDTOs(List<TransportProvider> transportProviders) {
        List<TransportProviderDTO> dtos = new ArrayList<>();
        for (TransportProvider transportProvider : transportProviders) {
            dtos.add(convertNotProvinceEntityToDTO(transportProvider));
        }
        dtos.sort(Comparator.comparing(TransportProviderDTO::getFullName));

        return dtos;
    }

}
