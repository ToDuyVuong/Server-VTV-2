package hcmute.kltn.vtv.model.data.shipping.response;

import hcmute.kltn.vtv.model.dto.shipping.CashOrderDTO;
import hcmute.kltn.vtv.model.dto.shipping.TransportDTO;
import hcmute.kltn.vtv.model.dto.user.OrderDTO;
import hcmute.kltn.vtv.model.entity.shipping.CashOrder;
import hcmute.kltn.vtv.model.entity.shipping.Transport;
import hcmute.kltn.vtv.model.entity.user.Order;
import hcmute.kltn.vtv.model.extra.ResponseAbstract;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CashOrderDetailResponse extends ResponseAbstract {

    private CashOrderDTO cashOrderDTO;
    private OrderDTO orderDTO;
    private TransportDTO transportDTO;

    public static CashOrderDetailResponse cashOrderDetailResponse(CashOrder cashOrder, Order order,
                                                                  Transport transport, String message) {
        CashOrderDetailResponse response = new CashOrderDetailResponse();
        response.setCashOrderDTO(CashOrderDTO.convertEntityToDTO(cashOrder));
        response.setOrderDTO(OrderDTO.convertEntityToDTO(order));
        response.setTransportDTO(TransportDTO.convertEntityToDTO(transport));
        response.setMessage(message);
        response.setCode(200);
        response.setStatus("OK");

        return response;
    }
}
