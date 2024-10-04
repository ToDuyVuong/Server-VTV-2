package hcmute.kltn.vtv.controller.chat;

import hcmute.kltn.vtv.model.data.chat.request.ChatMessageRequest;
import hcmute.kltn.vtv.model.data.chat.response.ChatMessageResponse;
import hcmute.kltn.vtv.service.chat.IChatService;
import hcmute.kltn.vtv.util.exception.InternalServerErrorException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Date;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;
    private final IChatService chatService;

    @PostMapping("/send")
    @SendTo("/topic/{romChatId}/public")
    public ResponseEntity<ChatMessageResponse> sendMessage(@RequestBody ChatMessageRequest chatMessageRequest,
            HttpServletRequest request) {

        try {
            String username = (String) request.getAttribute("username");
            chatMessageRequest.validate(chatMessageRequest);

            ChatMessageResponse response = chatService.saveMessage(username, chatMessageRequest);

            messagingTemplate.convertAndSendToUser(
                    chatMessageRequest.getReceiverUsername(),
                    "/topic/" + chatMessageRequest.getRoomChatId() + "/public",
                    chatMessageRequest);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            throw new InternalServerErrorException("Lỗi hệ thống tin nhắn! Vui lòng thử lại sau. " + e.getMessage());
        }
    }

}