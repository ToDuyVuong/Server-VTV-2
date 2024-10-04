package hcmute.kltn.vtv.model.data.user.request;

import hcmute.kltn.vtv.util.exception.BadRequestException;
import lombok.*;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessengerRequest {

    private String content;
    private String sender;
    private String receiver;

    public void validate() {

        if (content == null || content.isEmpty()) {
            throw new BadRequestException("Nội dung không được để trống.");
        }

        if (sender == null || sender.isEmpty()) {
            throw new BadRequestException("Người gửi không được để trống.");
        }

        if (receiver == null || receiver.isEmpty()) {
            throw new BadRequestException("Người nhận không được để trống.");
        }

        trim();
    }

    public void trim() {
        this.content = this.content.trim();
        this.sender = this.sender.trim();
        this.receiver = this.receiver.trim();
    }
}
