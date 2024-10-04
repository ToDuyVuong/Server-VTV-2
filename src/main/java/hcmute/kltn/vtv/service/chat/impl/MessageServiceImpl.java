package hcmute.kltn.vtv.service.chat.impl;

import hcmute.kltn.vtv.model.data.chat.request.ChatMessageRequest;
import hcmute.kltn.vtv.model.data.chat.response.PageMessageResponse;
import hcmute.kltn.vtv.model.entity.chat.Message;
import hcmute.kltn.vtv.model.entity.chat.RoomChat;
import hcmute.kltn.vtv.model.extra.Status;
import hcmute.kltn.vtv.repository.chat.MessageRepository;
import hcmute.kltn.vtv.service.chat.IMessageService;
import hcmute.kltn.vtv.service.chat.IRoomChatService;
import hcmute.kltn.vtv.util.exception.InternalServerErrorException;
import hcmute.kltn.vtv.util.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements IMessageService {

    private final IRoomChatService roomChatService;
    private final MessageRepository messageRepository;

    @Override
    @Transactional
    public Message addNewMessage(String username, ChatMessageRequest chatMessageRequest) {
        Message message = new Message();
        message.setContent(chatMessageRequest.getContent());
        message.setSenderUsername(username);
        message.setDate(new Date());
        message.setUsernameSenderDelete(false);
        message.setUsernameReceiverDelete(false);
        message.setRoomChat(roomChatService.getRoomChatById(chatMessageRequest.getRoomChatId()));
        message.setStatus(Status.ACTIVE);

        try {
            return messageRepository.save(message);
        } catch (Exception e) {
            throw new InternalServerErrorException("Lỗi hệ thống tin nhắn! Vui lòng thử lại sau.");
        }
    }

    @Override
    public PageMessageResponse getListChatMessagesPage(String username, UUID roomChatId, int page, int size) {

        Page<Message> messages = messageRepository
                .findByRoomChatRoomChatIdOrderByDateDesc(roomChatId, PageRequest.of(page - 1, size))
                .orElseThrow(() -> new NotFoundException("Không tìm danh sách tin nhắn!"));

        String message = "Lấy danh sách tin nhắn theo phòng chat thành công!";

        return PageMessageResponse.pageMessageResponse(messages, message);
    }

    @Override
    public PageMessageResponse getListChatMessagesPageByUsername(String senderUsername, String receiverUsername,
                                                                 int page, int size) {

        RoomChat roomChat = roomChatService.getRoomChatBySenderUsernameAndReceiverUsername(senderUsername,
                receiverUsername);

        Page<Message> messages = messageRepository
                .findByRoomChatRoomChatId(roomChat.getRoomChatId(), PageRequest.of(page - 1, size))
                .orElseThrow(() -> new NotFoundException("Không tìm danh sách tin nhắn!"));

        String message = "Lấy danh sách tin nhắn thành công!";

        return PageMessageResponse.pageMessageResponse(messages, message);

    }



}
