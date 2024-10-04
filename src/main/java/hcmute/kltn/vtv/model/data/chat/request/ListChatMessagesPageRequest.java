package hcmute.kltn.vtv.model.data.chat.request;

import hcmute.kltn.vtv.util.exception.BadRequestException;
import lombok.*;

import java.util.UUID;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ListChatMessagesPageRequest {
    private int page;
    private int size;
    private String username;
    private UUID roomChatId;

    public  void validate() {
        if (page < 0) {
            throw new BadRequestException("Số trang phải lớn hơn 0");
        }
        if (size < 0) {
            throw new BadRequestException("Số lượng phải lớn hơn 0");
        }
        if (username == null || username.isEmpty()) {
            throw new BadRequestException("Tên đăng nhập không được để trống");
        }
        if (roomChatId == null) {
            throw new BadRequestException("Phòng chat không được để trống");
        }

    }
}
