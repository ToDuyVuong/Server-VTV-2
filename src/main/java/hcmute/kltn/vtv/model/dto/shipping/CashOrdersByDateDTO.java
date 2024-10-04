package hcmute.kltn.vtv.model.dto.shipping;


import hcmute.kltn.vtv.model.entity.shipping.CashOrder;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CashOrdersByDateDTO {

    private LocalDateTime date;
    private int count;
    private Long totalMoney;
    private List<CashOrderDTO> cashOrderDTOs;





    public static CashOrdersByDateDTO convertEntitiesToDTO(List<CashOrder> cashOrders, LocalDateTime date) {
        CashOrdersByDateDTO cashOrdersByDateDTO = new CashOrdersByDateDTO();
        cashOrdersByDateDTO.setCashOrderDTOs(CashOrderDTO.convertEntitiesToDTOs(cashOrders));
        cashOrdersByDateDTO.setDate(date);
        cashOrdersByDateDTO.setCount(cashOrders.size());
        cashOrdersByDateDTO.setTotalMoney(cashOrders.stream().mapToLong(CashOrder::getMoney).sum());
        return cashOrdersByDateDTO;
    }


    public static List<CashOrdersByDateDTO> cashOrdersByDateDTOs(List<CashOrder> cashOrderDTOs) {

        Map<LocalDateTime, List<CashOrder>> map = convertEntityToMapWithDate(cashOrderDTOs);
        List<CashOrdersByDateDTO> cashOrdersByDateDTOs = new ArrayList<>();
        for (Map.Entry<LocalDateTime, List<CashOrder>> entry : map.entrySet()) {
            cashOrdersByDateDTOs.add(CashOrdersByDateDTO.convertEntitiesToDTO(entry.getValue(), entry.getKey()));
        }
        cashOrdersByDateDTOs.sort((o1, o2) -> o2.getDate().compareTo(o1.getDate()));
        return cashOrdersByDateDTOs;
    }



    public static Map<LocalDateTime, List<CashOrder>> convertEntityToMapWithDate(List<CashOrder> cashOrders) {
        HashMap<LocalDateTime, List<CashOrder>> map = new HashMap<>();
        for (CashOrder cashOrder : cashOrders) {
            if (map.containsKey(cashOrder.getUpdateAt().toLocalDate().atStartOfDay())) {
                map.get(cashOrder.getUpdateAt().toLocalDate().atStartOfDay()).add(cashOrder);
            } else {
                List<CashOrder> list = new ArrayList<>();
                list.add(cashOrder);
                map.put(cashOrder.getUpdateAt().toLocalDate().atStartOfDay(), list);
            }
        }
        return map;
    }


}
