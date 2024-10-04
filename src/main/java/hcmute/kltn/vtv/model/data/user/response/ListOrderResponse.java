package hcmute.kltn.vtv.model.data.user.response;

import hcmute.kltn.vtv.model.dto.user.OrderDTO;
import hcmute.kltn.vtv.model.entity.user.Order;
import hcmute.kltn.vtv.model.extra.ResponseAbstract;
import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ListOrderResponse extends ResponseAbstract {

    private int count;

    private Long totalPayment;

    private Long totalPrice;

    private List<OrderDTO> orderDTOs;


    public static ListOrderResponse listOrderResponse(List<Order> orders, String message, String status) {
        ListOrderResponse response = new ListOrderResponse();
        response.setOrderDTOs(OrderDTO.convertEntitiesToDTOs(orders));
        response.setTotalPayment(orders.stream().mapToLong(Order::getPaymentTotal).sum());
        response.setTotalPrice(orders.stream().mapToLong(Order::getTotalPrice).sum());
        response.setCount(orders.size());
        response.setMessage(message);
        response.setStatus(status);
        response.setCode(200);
        return response;
    }

}
