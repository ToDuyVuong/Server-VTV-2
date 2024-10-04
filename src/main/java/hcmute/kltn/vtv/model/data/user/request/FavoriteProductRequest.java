package hcmute.kltn.vtv.model.data.user.request;

import hcmute.kltn.vtv.util.exception.BadRequestException;
import lombok.*;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FavoriteProductRequest {

    private Long favoriteProductId;

    private String username;

    private Long productId;

    public void validate() {
        if (productId == null) {
            throw new BadRequestException("Mã sản phẩm không được để trống!");
        }

        if (username == null || username.isEmpty()) {
            throw new BadRequestException("Tài khoản không được để trống!");
        }
    }

    public void validateUpdate() {
        if (favoriteProductId == null) {
            throw new BadRequestException("Mã sản phẩm yêu thích không được để trống!");
        }
    }

}
