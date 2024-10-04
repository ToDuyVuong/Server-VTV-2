package hcmute.kltn.vtv.controller.chat;

import hcmute.kltn.vtv.authentication.service.IJwtService;
import hcmute.kltn.vtv.util.exception.BadRequestException;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import hcmute.kltn.vtv.model.data.chat.request.ChatMessageRequest;
import hcmute.kltn.vtv.model.dto.chat.MessageDTO;
import hcmute.kltn.vtv.service.chat.IChatService;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class WebSocketController {

    private final IChatService chatService;
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final IJwtService jwtService;

    @MessageMapping("/chat")
    public void processMessage(@Payload ChatMessageRequest chatMessageRequest, SimpMessageHeaderAccessor headerAccessor) {
        String token = headerAccessor.getNativeHeader("Authorization").get(0);
        String username = jwtService.extractUsername(token);
        if (username == null || username.isEmpty()) {
            throw new BadRequestException("Không tìm thấy thông tin người gửi trong token");
        }
        chatMessageRequest.validate(chatMessageRequest);
        MessageDTO msgDTO = chatService.saveChatMessage(username, chatMessageRequest);
        simpMessagingTemplate.convertAndSendToUser(msgDTO.getRoomChatId().toString(), "/chat", msgDTO);
    }
}
