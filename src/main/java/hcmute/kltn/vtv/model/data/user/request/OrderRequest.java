package hcmute.kltn.vtv.model.data.user.request;

import hcmute.kltn.vtv.util.exception.BadRequestException;
import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequest {

    private String username;

    private List<Long> cartIds;

    private Long addressId;

    private List<Long> voucherIds;

    private String paymentMethod;

    private String shippingMethod;

    private String note;

    private String codeWard;

    public static void validate(OrderRequest request) {

        if (request.getCartIds() == null || request.getCartIds().isEmpty()) {
            throw new BadRequestException("Danh sách sản phẩm không được để trống!");
        }

        if (request.getAddressId() == null) {
            throw new BadRequestException("Mã địa chỉ không được để trống!");
        }

        if (request.getPaymentMethod() == null || request.getPaymentMethod().isEmpty()) {
            throw new BadRequestException("Phương thức thanh toán không được để trống! ");
        }
        if (!request.getPaymentMethod().equals("COD")) {
            throw new BadRequestException("Phương thanh toán không hợp lệ! Hiện tai chỉ hỗ trợ COD");
        }

        if (request.getShippingMethod() == null || request.getShippingMethod().isEmpty()) {
            throw new BadRequestException("Phương thức vận chuyển không được để trống.");
        }
        if (!request.getShippingMethod().equals("GHN") &&
                !request.getShippingMethod().equals("GHTK") &&
                !request.getShippingMethod().equals("EXPRESS")) {
            throw new BadRequestException(
                    "Phương thức vận chuyển không hợp lệ! Hiện tại chỉ hỗ trợ GHN, GHTK, EXPRESS");
        }

        if (request.getCodeWard() == null || request.getCodeWard().isEmpty()) {
            throw new BadRequestException("Mã phường xã không được để trống!");
        }

        request.trim();
    }

    public void trim() {
        this.username = this.username.trim();
        this.note = this.note.trim();
        this.paymentMethod = this.paymentMethod.trim();
        this.shippingMethod = this.shippingMethod.trim();
        this.codeWard = this.codeWard.trim();
    }

}
