package hcmute.kltn.vtv.model.data.vendor.request;

import hcmute.kltn.vtv.util.exception.BadRequestException;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@ToString
@RequiredArgsConstructor
public class ChangePriceProductsRequest {
    private List<Long> productIds;
    private Long price;


    public void validate() {
        if (this.productIds == null || this.productIds.isEmpty()) {
            throw new BadRequestException("Danh sách sản phẩm không được để trống!");
        }
        if (this.price == null || this.price <= 0) {
            throw new BadRequestException("Giá sản phẩm không hợp lệ!");
        }
    }
}
