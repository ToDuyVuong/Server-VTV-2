package hcmute.kltn.vtv.model.data.user.response;

import hcmute.kltn.vtv.model.extra.ResponseAbstract;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class MessengersResponse extends ResponseAbstract {

    private String username;

    private int count;

    private Long roomChatId;

//    private List<MessengerDTO> messengerDTOs;
}
