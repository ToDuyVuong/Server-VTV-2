package hcmute.kltn.vtv.controller.chat;


import hcmute.kltn.vtv.model.data.chat.response.PageMessageResponse;
import hcmute.kltn.vtv.service.chat.IMessageService;
import hcmute.kltn.vtv.service.vtv.IPageService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/chat/message")
@RequiredArgsConstructor
public class MessageController {

    private final IMessageService messageService;
    private final IPageService pageService;

    @GetMapping("/list/room-chat/{roomChatId}/page/{page}/size/{size}")
    public ResponseEntity<PageMessageResponse> getListMessageByRoomChatId(@PathVariable UUID roomChatId,
                                                                          @PathVariable int page,
                                                                          @PathVariable int size,
                                                                          HttpServletRequest servletRequest) {

        String username = (String) servletRequest.getAttribute("username");
        pageService.validatePageNumberAndSize(page, size);
        return ResponseEntity.ok(messageService.getListChatMessagesPage(username, roomChatId, page, size));
    }


}
