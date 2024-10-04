package hcmute.kltn.vtv.model.data.manager.response;

import hcmute.kltn.vtv.model.dto.manager.ManagerShopDTO;
import hcmute.kltn.vtv.model.dto.vendor.ShopDTO;
import hcmute.kltn.vtv.model.entity.manager.ManagerShop;
import hcmute.kltn.vtv.model.entity.vendor.Shop;
import hcmute.kltn.vtv.model.extra.ResponseAbstract;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ManagerShopResponse extends ResponseAbstract {

    private ShopDTO shopDTO;
    private ManagerShopDTO managerShopDTO;

    public static ManagerShopResponse managerShopResponse(Shop shop, ManagerShop managerShop, String message, String status) {
        ManagerShopResponse response = new ManagerShopResponse();
        response.setShopDTO(ShopDTO.convertEntityToDTO(shop));
        response.setManagerShopDTO(ManagerShopDTO.convertEntityToDTO(managerShop));
        response.setMessage(message);
        response.setStatus(status);
        response.setCode(200);

        return response;
    }

}
