package hcmute.kltn.vtv.model.dto.user;

import hcmute.kltn.vtv.model.entity.user.FollowedShop;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class FollowedShopDTO {

    private Long followedShopId;

    private Long shopId;

    private String shopName;

    private String avatar;


    public static  FollowedShopDTO convertEntityToDTO(FollowedShop followedShop) {
        FollowedShopDTO followedShopDTO = new FollowedShopDTO();
            followedShopDTO.setFollowedShopId(followedShop.getFollowedShopId());
            followedShopDTO.setShopId(followedShop.getShop().getShopId());
            followedShopDTO.setShopName(followedShop.getShop().getName());
            followedShopDTO.setAvatar(followedShop.getShop().getAvatar());

        return followedShopDTO;
    }


    public static List<FollowedShopDTO> convertEntitiesToDTOs(List<FollowedShop> followedShops) {
        List<FollowedShopDTO> followedShopDTOs = new ArrayList<>();
        followedShops.sort((o1, o2) -> o2.getCreateAt().compareTo(o1.getCreateAt()));
        for (FollowedShop followedShop : followedShops) {
            followedShopDTOs.add(convertEntityToDTO(followedShop));
        }

        return followedShopDTOs;
    }

}
