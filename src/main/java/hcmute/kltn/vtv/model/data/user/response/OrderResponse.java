package hcmute.kltn.vtv.model.data.user.response;

import hcmute.kltn.vtv.model.dto.shipping.ShippingDTO;
import hcmute.kltn.vtv.model.dto.shipping.TransportDTO;
import hcmute.kltn.vtv.model.dto.user.OrderDTO;
import hcmute.kltn.vtv.model.entity.shipping.Transport;
import hcmute.kltn.vtv.model.entity.user.Order;
import hcmute.kltn.vtv.model.extra.ResponseAbstract;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse extends ResponseAbstract {

    private Long balance;

    private Long totalPoint;

    private OrderDTO orderDTO;

    private TransportDTO transportDTO;

    private ShippingDTO shippingDTO;


    public static OrderResponse orderResponse(Order order, String message, String status) {
        OrderResponse response = new OrderResponse();
        response.setOrderDTO(OrderDTO.convertEntityToDTO(order));
        response.setMessage(message);
        response.setStatus(status);
        response.setCode(200);

        return response;
    }

    public static OrderResponse orderResponse(Order order, ShippingDTO shippingDTO, String message, String status) {
        OrderResponse response = orderResponse(order, message, status);
        response.setShippingDTO(shippingDTO);

        return response;
    }

    public static OrderResponse orderResponse(Order order, Transport transport, ShippingDTO shippingDTO, String message, String status) {
        OrderResponse response = orderResponse(order, shippingDTO, message, status);
        response.setTransportDTO(TransportDTO.convertEntityToDTO(transport));

        return response;
    }

    public static OrderResponse orderResponse(Long balance, Long totalPoint, Order order, ShippingDTO shippingDTO, String message, String status) {
        OrderResponse response = orderResponse(order, shippingDTO, message, status);
        response.setBalance(balance);
        response.setTotalPoint(totalPoint);

        return response;
    }


}
