package hcmute.kltn.vtv.model.data.user.response;

import hcmute.kltn.vtv.model.dto.user.NotificationDTO;
import hcmute.kltn.vtv.model.entity.user.Notification;
import hcmute.kltn.vtv.model.extra.ResponseAbstract;
import lombok.*;
import org.springframework.data.domain.Page;

import java.util.List;


@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class NotificationResponse extends ResponseAbstract {

    private int count;
    private int page;
    private int size;
    private int totalPage;

    private List<NotificationDTO> notificationDTOs;


    public static NotificationResponse notificationResponse(Page<Notification> notifications, String message) {
        NotificationResponse response = new NotificationResponse();
        response.setNotificationDTOs(NotificationDTO.convertEntitiesToDTOs(notifications.getContent()));
        response.setTotalPage(notifications.getTotalPages());
        response.setCount(notifications.getNumberOfElements());
        response.setPage(notifications.getNumber() + 1);
        response.setSize(notifications.getSize());
        response.setStatus("OK");
        response.setCode(200);
        response.setMessage(message);

        return response;
    }
}
