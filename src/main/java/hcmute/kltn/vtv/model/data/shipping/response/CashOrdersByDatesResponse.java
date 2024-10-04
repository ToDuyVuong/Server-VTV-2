package hcmute.kltn.vtv.model.data.shipping.response;


import hcmute.kltn.vtv.model.dto.shipping.CashOrdersByDateDTO;
import hcmute.kltn.vtv.model.entity.shipping.CashOrder;
import hcmute.kltn.vtv.model.extra.ResponseAbstract;
import lombok.*;

import java.util.List;


@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CashOrdersByDatesResponse extends ResponseAbstract {

    private int count;
    private List<CashOrdersByDateDTO> cashOrdersByDateDTOs;

    public static CashOrdersByDatesResponse cashOrdersByDatesResponse(List<CashOrder> cashOrders, String message) {
        CashOrdersByDatesResponse response = new CashOrdersByDatesResponse();
        response.setCount(cashOrders.size());
        response.setCashOrdersByDateDTOs(CashOrdersByDateDTO.cashOrdersByDateDTOs(cashOrders));
        response.setMessage(message);
        response.setCode(200);
        response.setStatus("OK");


        return response;
    }



}
