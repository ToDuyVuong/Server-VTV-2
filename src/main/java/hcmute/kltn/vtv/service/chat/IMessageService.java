package hcmute.kltn.vtv.service.chat;

import hcmute.kltn.vtv.model.data.chat.request.ChatMessageRequest;
import hcmute.kltn.vtv.model.data.chat.response.PageMessageResponse;
import hcmute.kltn.vtv.model.entity.chat.Message;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

public interface IMessageService {
    @Transactional
    Message addNewMessage(String username, ChatMessageRequest chatMessageRequest);


    PageMessageResponse getListChatMessagesPage(String username, UUID roomChatId, int page, int size);

    PageMessageResponse getListChatMessagesPageByUsername(String senderUsername, String receiverUsername, int page, int size);

}
