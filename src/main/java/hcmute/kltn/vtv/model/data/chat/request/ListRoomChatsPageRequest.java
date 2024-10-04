package hcmute.kltn.vtv.model.data.chat.request;

import hcmute.kltn.vtv.util.exception.BadRequestException;
import lombok.*;



@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ListRoomChatsPageRequest {


    private int page;
    private int size;
    private String username;

    public void validate() {
        if (page <= 0) {
            throw new BadRequestException("Số trang phải lớn hơn 0");
        }
        if (size <= 0) {
            throw new BadRequestException("Số lượng phải lớn hơn 0");
        }
        if (username == null || username.isEmpty()) {
            throw new BadRequestException("Tên đăng nhập không được để trống");
        }
    }
}
