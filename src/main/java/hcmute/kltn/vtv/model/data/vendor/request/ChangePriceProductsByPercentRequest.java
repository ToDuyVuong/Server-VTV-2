package hcmute.kltn.vtv.model.data.vendor.request;

import hcmute.kltn.vtv.util.exception.BadRequestException;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.List;
@Data
@ToString
@RequiredArgsConstructor
public class ChangePriceProductsByPercentRequest {
    private List<Long> productIds;
    private int percent;

    public void validate() {
        if (this.productIds == null || this.productIds.isEmpty()) {
            throw new BadRequestException("Danh sách sản phẩm không được để trống!");
        }

        if (this.percent <= 4) {
            throw new BadRequestException("Phần trăm giá phải lớn hơn 4%!");
        }

        if (this.percent > 100) {
            throw new BadRequestException("Phần trăm giá phải nhỏ hơn 100%!");
        }
    }
}
