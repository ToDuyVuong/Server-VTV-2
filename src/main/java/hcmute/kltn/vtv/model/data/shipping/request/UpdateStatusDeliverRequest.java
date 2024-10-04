package hcmute.kltn.vtv.model.data.shipping.request;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import hcmute.kltn.vtv.model.extra.Status;
import hcmute.kltn.vtv.model.extra.UUIDDeserializer;
import hcmute.kltn.vtv.util.exception.BadRequestException;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.UUID;

@Data
@ToString
@RequiredArgsConstructor
public class UpdateStatusDeliverRequest {

//    @JsonDeserialize(using = UUIDDeserializer.class)
    private Long deliverId;

    private Status status;

    private String usernameAdded;

    public void validate() {
        if (this.deliverId == null) {
            throw new BadRequestException("Mã người giao hàng không được để trống.");
        }
        if (this.status == null) {
            throw new BadRequestException("Trạng thái không được để trống.");
        }
        if (this.usernameAdded == null || this.usernameAdded.isEmpty()) {
            throw new BadRequestException("Tên người đăng ký không được để trống.");
        }
    }


}
