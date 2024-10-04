package hcmute.kltn.vtv.model.data.user.response;

import hcmute.kltn.vtv.model.dto.user.FollowedShopDTO;
import hcmute.kltn.vtv.model.entity.user.FollowedShop;
import hcmute.kltn.vtv.model.extra.ResponseAbstract;
import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ListFollowedShopResponse extends ResponseAbstract {

    private int count;
    private List<FollowedShopDTO> followedShopDTOs;

    public static ListFollowedShopResponse listFollowedShopResponse(List<FollowedShop> followedShops, String message, String status) {
        ListFollowedShopResponse response = new ListFollowedShopResponse();
        response.setFollowedShopDTOs(FollowedShopDTO.convertEntitiesToDTOs(followedShops));
        response.setCount(followedShops.size());
        response.setMessage(message);
        response.setStatus(status);
        response.setCode(200);
        return response;
    }
}
