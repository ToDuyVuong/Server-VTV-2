package hcmute.kltn.vtv.model.data.user.response;

import hcmute.kltn.vtv.model.extra.ResponseAbstract;
import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class MultipleOrderResponse extends ResponseAbstract {

    private int count;
    private int totalProduct;
    private int totalQuantity;
    private Long totalPrice;
    private Long totalPayment;
    private Long totalDiscount;
    private Long totalShippingFee;
    private Long totalLoyaltyPoint;
    private Long discountShop;
    private Long discountSystem;

    private List<OrderResponse> orderResponses;

    public static MultipleOrderResponse multipleOrderResponse(List<OrderResponse> orderResponses, String message, String status) {
        MultipleOrderResponse response = new MultipleOrderResponse();
        response.setOrderResponses(orderResponses);
        response.setCount(orderResponses.size());
        response.setTotalProduct(response.calculateTotalProduct(orderResponses));
        response.setTotalQuantity(response.calculateTotalQuantity(orderResponses));
        response.setTotalPrice(response.calculateTotalPrice(orderResponses));
        response.setTotalPayment(response.calculateTotalPayment(orderResponses));
        response.setTotalDiscount(response.calculateTotalDiscount(orderResponses));
        response.setTotalShippingFee(response.calculateTotalShippingFee(orderResponses));
        response.setTotalLoyaltyPoint(response.calculateTotalLoyaltyPoint(orderResponses));
        response.setDiscountShop(response.calculateDiscountShop(orderResponses));
        response.setDiscountSystem(response.calculateDiscountSystem(orderResponses));

        response.setMessage(message);
        response.setStatus(status);
        response.setCode(200);

        return response;
    }


    private int calculateTotalProduct(List<OrderResponse> orderResponses) {
        int totalProduct = 0;
        for (OrderResponse orderResponse : orderResponses) {
            totalProduct += orderResponse.getOrderDTO().getOrderItemDTOs().size();
        }
        return totalProduct;
    }

    private int calculateTotalQuantity(List<OrderResponse> orderResponses) {
        int totalQuantity = 0;
        for (OrderResponse orderResponse : orderResponses) {
            totalQuantity += orderResponse.getOrderDTO().getCount();
        }
        return totalQuantity;
    }


    private Long calculateTotalPrice(List<OrderResponse> orderResponses) {
        Long totalPrice = 0L;
        for (OrderResponse orderResponse : orderResponses) {
            totalPrice += orderResponse.getOrderDTO().getTotalPrice();
        }
        return totalPrice;
    }

    private Long calculateTotalPayment(List<OrderResponse> orderResponses) {
        Long totalPayment = 0L;
        for (OrderResponse orderResponse : orderResponses) {
            totalPayment += orderResponse.getOrderDTO().getPaymentTotal();
        }
        return totalPayment;
    }


    private Long calculateTotalDiscount(List<OrderResponse> orderResponses) {
        Long totalDiscount = 0L;
        for (OrderResponse orderResponse : orderResponses) {
            totalDiscount += orderResponse.getOrderDTO().getDiscountShop() + orderResponse.getOrderDTO().getDiscountSystem();
        }
        return totalDiscount;
    }


    private Long calculateTotalShippingFee(List<OrderResponse> orderResponses) {
        Long totalShippingFee = 0L;
        for (OrderResponse orderResponse : orderResponses) {
            totalShippingFee += orderResponse.getOrderDTO().getShippingFee();
        }
        return totalShippingFee;
    }


    private Long calculateTotalLoyaltyPoint(List<OrderResponse> orderResponses) {
        return orderResponses.get(0).getTotalPoint();
    }


    private Long calculateDiscountShop(List<OrderResponse> orderResponses) {
        Long discountShop = 0L;
        for (OrderResponse orderResponse : orderResponses) {
            discountShop += orderResponse.getOrderDTO().getDiscountShop();
        }
        return discountShop;
    }


    private Long calculateDiscountSystem(List<OrderResponse> orderResponses) {
        Long discountSystem = 0L;
        for (OrderResponse orderResponse : orderResponses) {
            discountSystem += orderResponse.getOrderDTO().getDiscountSystem();
        }
        return discountSystem;
    }









}
