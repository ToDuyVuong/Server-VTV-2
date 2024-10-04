package hcmute.kltn.vtv.model.data.manager.response;

import hcmute.kltn.vtv.model.dto.vendor.ShopDTO;
import hcmute.kltn.vtv.model.entity.vendor.Shop;
import hcmute.kltn.vtv.model.extra.ResponseAbstract;
import lombok.*;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ShopsResponse extends ResponseAbstract {

    private int page;
    private int size;
    private int count;
    private int totalPage;
    private List<ShopDTO> shopDTOs;


    public static ShopsResponse shopsResponse(Page<Shop> shops, String message, int page, int size) {
        ShopsResponse response = new ShopsResponse();
        response.setCount(shops.getNumberOfElements());
        response.setShopDTOs(ShopDTO.convertEntitiesToDTOs(shops.getContent()));
        response.setMessage(message);
        response.setStatus("OK");
        response.setCode(200);
        response.setPage(page);
        response.setSize(size);
        response.setTotalPage(shops.getTotalPages());

        return response;
    }
}
