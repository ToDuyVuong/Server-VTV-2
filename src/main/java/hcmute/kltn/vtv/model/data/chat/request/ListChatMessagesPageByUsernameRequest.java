package hcmute.kltn.vtv.model.data.chat.request;


import hcmute.kltn.vtv.util.exception.BadRequestException;
import lombok.*;


@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ListChatMessagesPageByUsernameRequest {
    private int page;
    private int size;
    private String senderUsername;
    private String receiverUsername;

    public void validate(ListChatMessagesPageByUsernameRequest request) {
        if (request == null) {
            throw new BadRequestException("Yêu cầu không hợp lệ");
        }
        if (request.getPage() < 0) {
            throw new BadRequestException("Trang không được nhỏ hơn 0");
        }
        if (request.getSize() < 0) {
            throw new BadRequestException("Kích thước trang không được nhỏ hơn 0");
        }

        if (request.getSenderUsername() == null) {
            throw new BadRequestException("Tên người gửi không được để trống");
        }

        if (request.getReceiverUsername() == null) {
            throw new BadRequestException("Tên người nhận không được để trống");
        }

        if (request.getSenderUsername().equals(request.getReceiverUsername())) {
            throw new BadRequestException("Tên người gửi và người nhận không được trùng nhau");
        }

        trim();
    }

    public void trim() {
        this.senderUsername = this.senderUsername.trim();
        this.receiverUsername = this.receiverUsername.trim();
    }
}