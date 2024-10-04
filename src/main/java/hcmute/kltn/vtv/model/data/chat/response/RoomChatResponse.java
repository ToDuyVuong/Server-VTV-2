package hcmute.kltn.vtv.model.data.chat.response;

import hcmute.kltn.vtv.model.dto.chat.RoomChatDTO;
import hcmute.kltn.vtv.model.extra.ResponseAbstract;
import lombok.*;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class RoomChatResponse extends ResponseAbstract {

    private RoomChatDTO roomChatDTO;
}
