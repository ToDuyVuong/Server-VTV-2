package hcmute.kltn.vtv.model.data.chat.response;


import hcmute.kltn.vtv.model.entity.chat.Message;
import hcmute.kltn.vtv.model.extra.ResponseAbstract;
import lombok.*;

import java.util.Date;
import java.util.UUID;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageResponse extends ResponseAbstract {

    private String content;
    private Date date;
    private String senderUsername;
    private String receiverUsername;
    private UUID roomChatId;

    public static ChatMessageResponse chatMessageResponse(Message message , String receiverUsername, String meg,  String status) {
        ChatMessageResponse chatMessageResponse = new ChatMessageResponse();
        chatMessageResponse.setContent(message.getContent());
        chatMessageResponse.setDate(new Date());
        chatMessageResponse.setSenderUsername(message.getSenderUsername());
        chatMessageResponse.setRoomChatId(message.getRoomChat().getRoomChatId());
        chatMessageResponse.setReceiverUsername(receiverUsername);
        chatMessageResponse.setMessage(meg);
        chatMessageResponse.setStatus(status);
        chatMessageResponse.setCode(200);
        return chatMessageResponse;
    }
}
