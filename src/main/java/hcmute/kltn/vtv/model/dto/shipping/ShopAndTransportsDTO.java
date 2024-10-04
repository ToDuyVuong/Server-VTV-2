package hcmute.kltn.vtv.model.dto.shipping;


import hcmute.kltn.vtv.model.dto.vendor.ShopDTO;
import hcmute.kltn.vtv.model.entity.shipping.Transport;
import hcmute.kltn.vtv.model.entity.vendor.Shop;
import lombok.*;

import java.util.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ShopAndTransportsDTO {

    private int count;
    private String wardCode;
    private String wardName;
    private ShopDTO shopDTO;
    private List<TransportDTO> transportDTOs;

    public static ShopAndTransportsDTO shopAndTransportsDTO(List<Transport> transports, Shop shop) {
        ShopAndTransportsDTO dto = new ShopAndTransportsDTO();
        dto.setCount(transports.size());
        dto.setWardCode(shop.getWard().getWardCode());
        dto.setWardName(shop.getWard().getName());
        dto.setTransportDTOs(TransportDTO.convertEntitiesToDTOs(transports));
        dto.setShopDTO(ShopDTO.convertEntityToDTO(shop));

        return dto;
    }


    public static List<ShopAndTransportsDTO> shopAndTransportsDTOs(List<Shop> shops, List<Transport> transports) {
        Map<Long, List<Transport>> shopIdToTransports = new HashMap<>();
        for (Transport transport : transports) {
            shopIdToTransports.computeIfAbsent(transport.getShopId(), k -> new ArrayList<>()).add(transport);
        }

        List<ShopAndTransportsDTO> dtos = new ArrayList<>();
        for (Shop shop : shops) {
            List<Transport> transportList = shopIdToTransports.getOrDefault(shop.getShopId(), new ArrayList<>());
            transportList.sort(Comparator.comparing(Transport::getCreateAt));
            dtos.add(shopAndTransportsDTO(transportList, shop));
        }
        return dtos;
    }
}