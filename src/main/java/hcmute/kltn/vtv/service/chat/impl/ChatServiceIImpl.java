package hcmute.kltn.vtv.service.chat.impl;

import hcmute.kltn.vtv.model.data.chat.request.ChatMessageRequest;
import hcmute.kltn.vtv.model.data.chat.response.ChatMessageResponse;
import hcmute.kltn.vtv.model.dto.chat.MessageDTO;
import hcmute.kltn.vtv.model.entity.chat.Message;
import hcmute.kltn.vtv.service.chat.IChatService;
import hcmute.kltn.vtv.service.chat.IMessageService;
import hcmute.kltn.vtv.service.chat.IRoomChatService;
import hcmute.kltn.vtv.service.vtv.IFcmService;
import hcmute.kltn.vtv.service.vtv.INotificationService;
import hcmute.kltn.vtv.util.exception.InternalServerErrorException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChatServiceIImpl implements IChatService {

    private final IMessageService messageService;
    private final IRoomChatService roomChatService;
    private final IFcmService fcmService;

    @Override
    @Transactional
    public ChatMessageResponse saveMessage(String username, ChatMessageRequest request) {
        try {
            Message message = messageService.addNewMessage(username, request);
            roomChatService.updateDateRoomChatById(message.getRoomChat().getRoomChatId(), message.getDate(),
                    message.getContent(), message.getSenderUsername());

            return ChatMessageResponse.chatMessageResponse(message, request.getReceiverUsername(), "Gửi tin nhắn thành công!",
                    "Success");
        } catch (Exception e) {
            throw new InternalServerErrorException("Lỗi hệ thống tin nhắn! Vui lòng thử lại sau. " + e.getMessage());
        }
    }

    @Override
    public MessageDTO saveChatMessage(String username, ChatMessageRequest request) {
        try {
            Message msg = messageService.addNewMessage(username, request);
            roomChatService.updateDateRoomChatById(msg.getRoomChat().getRoomChatId(), msg.getDate(), msg.getContent(),
                    msg.getSenderUsername());

            String titleNotification = "Tin nhắn mới từ " + msg.getSenderUsername();
            String bodyNotification = msg.getContent();
            String receiverUsername = request.getReceiverUsername();

            fcmService.sendNotificationNewMessage(
                    titleNotification,
                    bodyNotification,
                    receiverUsername,
                    msg.getSenderUsername(),
                    "NEW_MESSAGE",
                    msg.getRoomChat().getRoomChatId()
            );


            return MessageDTO.convertEntityToDTO(msg);

        } catch (Exception e) {
            throw new InternalServerErrorException("Lỗi hệ thống tin nhắn! Vui lòng thử lại sau. " + e.getMessage());
        }
    }

}
