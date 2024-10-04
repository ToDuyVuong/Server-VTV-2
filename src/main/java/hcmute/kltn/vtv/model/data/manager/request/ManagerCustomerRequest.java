package hcmute.kltn.vtv.model.data.manager.request;

import hcmute.kltn.vtv.util.exception.BadRequestException;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Data
@ToString
@RequiredArgsConstructor
public class ManagerCustomerRequest {

    private Long customerId;
    private String managerUsername;
    private String note;
    private boolean isLock;

    public void validate() {
        if (this.customerId == null || this.customerId <= 0) {
            throw new BadRequestException("Mã khách hàng không hợp lệ");
        }
    }

}
