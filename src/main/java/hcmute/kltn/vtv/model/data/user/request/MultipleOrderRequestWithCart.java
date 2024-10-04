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
public class MultipleOrderRequestWithCart {

    private List<OrderRequestWithCart> orderRequestWithCarts;

    public static void validate(MultipleOrderRequestWithCart request) {
        validateOrderRequestWithCarts(request.getOrderRequestWithCarts());
        checkLoyaltyPoints(request.getOrderRequestWithCarts());
//        checkVoucherSystem(request.getOrderRequestWithCarts());
        checkPaymentMethod(request.getOrderRequestWithCarts());
    }

    private static void validateOrderRequestWithCarts(List<OrderRequestWithCart> orderRequestWithCarts) {
        if (orderRequestWithCarts == null || orderRequestWithCarts.isEmpty()) {
            throw new BadRequestException("Danh sách đơn hàng không được để trống!");
        }

        for (OrderRequestWithCart orderRequestWithCart : orderRequestWithCarts) {
            OrderRequestWithCart.validate(orderRequestWithCart);
        }
    }

    private static void checkLoyaltyPoints(List<OrderRequestWithCart> orderRequestWithCarts) {
        int countLoyalty = 0;
        for (OrderRequestWithCart orderRequestWithCart : orderRequestWithCarts) {
            if (orderRequestWithCart.isUseLoyaltyPoint()) {
                countLoyalty++;
            }
            if (countLoyalty > 1) {
                throw new BadRequestException("Chỉ được sử dụng 1 lần điểm thưởng!");
            }
        }
    }

//    public static void checkVoucherSystem(List<OrderRequestWithCart> orderRequestWithCarts) {
//        int countVoucherSystem = 0;
//        for (OrderRequestWithCart orderRequestWithCart : orderRequestWithCarts) {
//            if (orderRequestWithCart.getSystemVoucherCode() != null) {
//                countVoucherSystem++;
//            }
//            if (countVoucherSystem > 1) {
//                throw new BadRequestException("Chỉ được sử dụng 1 mã voucher hệ thống!");
//            }
//        }
//    }

    public static void checkPaymentMethod(List<OrderRequestWithCart> orderRequestWithCarts) {
        String paymentMethod = orderRequestWithCarts.get(0).getPaymentMethod();
        for (OrderRequestWithCart orderRequestWithCart : orderRequestWithCarts) {
            if (!orderRequestWithCart.getPaymentMethod().equals(paymentMethod)) {
                throw new BadRequestException("Phương thức thanh toán không hợp lệ! Vui lòng chọn cùng 1 phương thức thanh toán.");
            }
        }
    }
}
