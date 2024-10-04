package hcmute.kltn.vtv.model.data.chat.response;


import hcmute.kltn.vtv.model.dto.chat.MessageDTO;
import hcmute.kltn.vtv.model.entity.chat.Message;
import hcmute.kltn.vtv.model.extra.ResponseAbstract;
import lombok.*;
import org.springframework.data.domain.Page;

import java.util.List;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PageMessageResponse extends ResponseAbstract {

    private int count;
    private int page;
    private int size;
    private int totalPage;
    private List<MessageDTO> messageDTOs;

    public static PageMessageResponse pageMessageResponse(Page<Message> messages, String message) {
        PageMessageResponse response = new PageMessageResponse();
        response.setMessageDTOs(MessageDTO.convertEntitiesToDTOs(messages.getContent()));
        response.setCount(messages.getNumberOfElements());
        response.setSize(messages.getSize());
        response.setPage(messages.getNumber() + 1);
        response.setTotalPage(messages.getTotalPages());
        response.setMessage(message);
        response.setStatus("OK");
        response.setCode(200);
        return response;
    }

}
