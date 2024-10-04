package hcmute.kltn.vtv.service.vtv;

import hcmute.kltn.vtv.model.data.user.request.NotificationRequest;
import hcmute.kltn.vtv.model.data.user.response.NotificationResponse;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

public interface INotificationService {
    @Transactional
    @Async
    void addNewNotification(NotificationRequest request);

    @Async
    @Transactional
    void addNewNotification(String title, String body, String recipient, String sender, String type);

    NotificationResponse getNotificationsByUsername(String username, int page, int size);

    NotificationResponse deleteNotificationById(UUID notificationId, String username);

    NotificationResponse readNotification(UUID notificationId, String username);
}
