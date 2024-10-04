package hcmute.kltn.vtv.controller.chat;


import hcmute.kltn.vtv.model.data.chat.request.ListRoomChatsPageRequest;
import hcmute.kltn.vtv.model.data.chat.response.ListRoomChatsPageResponse;
import hcmute.kltn.vtv.model.data.chat.response.RoomChatResponse;
import hcmute.kltn.vtv.service.chat.IRoomChatService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/chat/room")
@RequiredArgsConstructor
public class RoomChatController {

    private final IRoomChatService roomChatService;


    @PostMapping("/create-room")
    public ResponseEntity<RoomChatResponse> createChatRoom(@RequestParam String receiverUsername,
                                                           HttpServletRequest request) {
        String senderUsername = (String) request.getAttribute("username");

        return ResponseEntity.ok(roomChatService.addNewRoomChat(senderUsername, receiverUsername));
    }

    @GetMapping("/list/page/{page}/size/{size}")
    public ResponseEntity<ListRoomChatsPageResponse> getListRoomChat(
            @PathVariable int page,
            @PathVariable int size,
            HttpServletRequest requestServlet) {

        String username = (String) requestServlet.getAttribute("username");
        roomChatService.checkRequestPageParams(page, size);
        ListRoomChatsPageRequest request = new ListRoomChatsPageRequest();
        request.setPage(page);
        request.setSize(size);
        request.setUsername(username);

        return ResponseEntity.ok(roomChatService.getListRoomChatsPageByUsername(username, page, size));
    }

    @DeleteMapping("/delete-room/{roomChatId}")
    public ResponseEntity<RoomChatResponse> deleteRoomChat(@PathVariable UUID roomChatId,
                                                           HttpServletRequest request) {
        String username = (String) request.getAttribute("username");

        return ResponseEntity.ok(roomChatService.deleteRoomChatByUsername(roomChatId, username));
    }

}
