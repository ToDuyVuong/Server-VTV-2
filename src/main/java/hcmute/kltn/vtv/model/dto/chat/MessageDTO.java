package hcmute.kltn.vtv.model.dto.chat;

import hcmute.kltn.vtv.model.entity.chat.Message;
import lombok.*;

import java.util.*;


@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class MessageDTO {

    private UUID messengerId;

    private String content;

    private String senderUsername;

    private Date date;

    private boolean usernameSenderDelete;

    private boolean usernameReceiverDelete;

    private UUID roomChatId;

    public static MessageDTO convertEntityToDTO(Message message) {
        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setMessengerId(message.getMessengerId());
        messageDTO.setContent(message.getContent());
        messageDTO.setSenderUsername(message.getSenderUsername());
        messageDTO.setDate(message.getDate());
        messageDTO.setUsernameSenderDelete(message.isUsernameSenderDelete());
        messageDTO.setUsernameReceiverDelete(message.isUsernameReceiverDelete());
        messageDTO.setRoomChatId(message.getRoomChat().getRoomChatId());
        return messageDTO;
    }

    public static List<MessageDTO> convertEntitiesToDTOs(List<Message> messages) {
        List<MessageDTO> messageDTOs = new ArrayList<>();

        for (Message message : messages) {
            messageDTOs.add(convertEntityToDTO(message));
        }
        messageDTOs.sort(Comparator.comparing(MessageDTO::getDate).reversed());
        return messageDTOs;
    }


}
