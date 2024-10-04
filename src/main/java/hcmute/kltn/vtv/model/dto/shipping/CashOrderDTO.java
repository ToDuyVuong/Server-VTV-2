package hcmute.kltn.vtv.model.dto.shipping;

import hcmute.kltn.vtv.model.entity.shipping.CashOrder;
import hcmute.kltn.vtv.model.extra.ResponseAbstract;
import hcmute.kltn.vtv.model.extra.Status;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CashOrderDTO {
    private UUID cashOrderId;
    private UUID transportId;
    private UUID orderId;
    private Long money;
    private String shipperUsername;
    private boolean shipperHold;
    private String waveHouseUsername;
    private boolean waveHouseHold;
    private boolean handlePayment;
    private Status status;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;


    public static CashOrderDTO convertEntityToDTO(CashOrder cashOrder) {
        CashOrderDTO cashOrderDTO = new CashOrderDTO();
        cashOrderDTO.setCashOrderId(cashOrder.getCashOrderId());
        cashOrderDTO.setTransportId(cashOrder.getTransportId());
        cashOrderDTO.setOrderId(cashOrder.getOrderId());
        cashOrderDTO.setMoney(cashOrder.getMoney());
        cashOrderDTO.setShipperUsername(cashOrder.getShipperUsername());
        cashOrderDTO.setShipperHold(cashOrder.isShipperHold());
        cashOrderDTO.setWaveHouseUsername(cashOrder.getWaveHouseUsername());
        cashOrderDTO.setWaveHouseHold(cashOrder.isWaveHouseHold());
        cashOrderDTO.setHandlePayment(cashOrder.isHandlePayment());
        cashOrderDTO.setStatus(cashOrder.getStatus());
        cashOrderDTO.setCreateAt(cashOrder.getCreateAt());
        cashOrderDTO.setUpdateAt(cashOrder.getUpdateAt());

        return cashOrderDTO;
    }



    public static List<CashOrderDTO> convertEntitiesToDTOs(List<CashOrder> cashOrders) {
        return cashOrders.stream()
                .map(CashOrderDTO::convertEntityToDTO)
                .sorted(Comparator.comparing(CashOrderDTO::getUpdateAt).reversed())
                .collect(Collectors.toList());
    }
}