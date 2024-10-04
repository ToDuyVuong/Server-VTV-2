package hcmute.kltn.vtv.service.chat;

import hcmute.kltn.vtv.model.data.chat.request.ListRoomChatsPageRequest;
import hcmute.kltn.vtv.model.data.chat.response.ListRoomChatsPageResponse;
import hcmute.kltn.vtv.model.data.chat.response.RoomChatResponse;
import hcmute.kltn.vtv.model.dto.chat.RoomChatDTO;
import hcmute.kltn.vtv.model.entity.chat.RoomChat;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.UUID;

public interface IRoomChatService {
    @Transactional
    RoomChatResponse addNewRoomChat(String senderUsername, String receiverUsername);

    RoomChatResponse deleteRoomChatByUsername(UUID roomChatId, String username);

    RoomChat getRoomChatBySenderUsernameAndReceiverUsername(String senderUsername, String receiverUsername);



    ListRoomChatsPageResponse getListRoomChatsPageByUsername(String username, int page, int size);

    RoomChat getRoomChatById(UUID roomChatId);


    @Transactional
    void updateDateRoomChatById(UUID roomChatId, Date date, String lastMessage, String senderUsername);

    void checkRequestPageParams(int page, int size);
}
