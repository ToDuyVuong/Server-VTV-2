package hcmute.kltn.vtv.model.data.shipping.response;

import hcmute.kltn.vtv.model.dto.shipping.CashOrderDTO;
import hcmute.kltn.vtv.model.entity.shipping.CashOrder;
import hcmute.kltn.vtv.model.extra.ResponseAbstract;
import lombok.*;

import java.util.List;


@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CashOrdersResponse extends ResponseAbstract {

    private int count;
    private Long totalMoney;
    private List<CashOrderDTO> cashOrderDTOs;



    public static CashOrdersResponse cashOrdersResponse(List<CashOrder> cashOrders, String message, String status) {
        CashOrdersResponse response = new CashOrdersResponse();
        response.setCount(cashOrders.size());
        response.setCashOrderDTOs(CashOrderDTO.convertEntitiesToDTOs(cashOrders));
        response.setTotalMoney(response.getCashOrderDTOs().stream().mapToLong(CashOrderDTO::getMoney).sum());
        response.setMessage(message);
        response.setCode(200);
        response.setStatus(status);

        return response;
    }

}
