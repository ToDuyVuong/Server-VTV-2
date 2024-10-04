package hcmute.kltn.vtv.model.data.shipping.request;


import hcmute.kltn.vtv.util.exception.BadRequestException;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.List;
import java.util.UUID;

@Data
@ToString
@RequiredArgsConstructor
public class CashOrdersRequest {

    private List<UUID> cashOrderIds;
    private String waveHouseUsername;

    public   void validate(){
        if (this.cashOrderIds == null || this.cashOrderIds.isEmpty()) {
            throw new BadRequestException("Danh sách mã đơn tiền không được để trống.");
        }
        if (this.waveHouseUsername == null || this.waveHouseUsername.isEmpty()) {
            throw new BadRequestException("Tài khoản kho không được để trống.");
        }
    }

    public static CashOrdersRequest cashOrdersRequest(List<UUID> cashOrderIds, String waveHouseUsername) {
        CashOrdersRequest cashOrdersRequest = new CashOrdersRequest();
        cashOrdersRequest.setCashOrderIds(cashOrderIds);
        cashOrdersRequest.setWaveHouseUsername(waveHouseUsername);
        return cashOrdersRequest;
    }

}
