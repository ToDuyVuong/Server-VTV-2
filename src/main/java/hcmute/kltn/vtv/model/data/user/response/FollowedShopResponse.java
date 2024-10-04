package hcmute.kltn.vtv.model.data.user.response;

import hcmute.kltn.vtv.model.dto.user.FollowedShopDTO;
import hcmute.kltn.vtv.model.entity.user.FollowedShop;
import hcmute.kltn.vtv.model.extra.ResponseAbstract;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class FollowedShopResponse extends ResponseAbstract {

    private FollowedShopDTO followedShopDTO;

    public static FollowedShopResponse followedShopResponse(FollowedShop followedShop, String message, String status) {
        FollowedShopResponse response = new FollowedShopResponse();
        response.setFollowedShopDTO(FollowedShopDTO.convertEntityToDTO(followedShop));
        response.setMessage(message);
        response.setStatus(status);
        return response;
    }
}
