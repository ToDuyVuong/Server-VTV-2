package hcmute.kltn.vtv.model.data.vendor.request;

import hcmute.kltn.vtv.util.exception.BadRequestException;
import lombok.*;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductPageRequest {

    private int page;
    private int size;
    private Long categoryId;

    // private String sort;
    // private String search;
    // private Long brandId;
    // private Long priceFrom;
    // private Long priceTo;
    // private Long shopId;
    // private Long userId;
    // private Long voucherId;
    // private Long productId;
    // private Long reviewId;
    // private Long commentId;

    public static void validate(ProductPageRequest request) {
        if (request == null) {
            throw new BadRequestException("Yêu cầu không được để trống!");
        }
        if (request.getPage() < 0) {
            throw new BadRequestException("Trang không hợp lệ!");
        }
        if (request.getSize() < 0) {
            throw new BadRequestException("Kích thước trang không hợp lệ!");
        }
    }

}
