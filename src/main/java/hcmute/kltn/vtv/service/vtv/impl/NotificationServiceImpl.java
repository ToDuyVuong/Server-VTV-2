package hcmute.kltn.vtv.service.vtv.impl;


import hcmute.kltn.vtv.model.data.user.request.NotificationRequest;
import hcmute.kltn.vtv.model.data.user.response.NotificationResponse;
import hcmute.kltn.vtv.model.dto.user.NotificationDTO;
import hcmute.kltn.vtv.model.entity.user.Notification;
import hcmute.kltn.vtv.repository.user.NotificationRepository;
import hcmute.kltn.vtv.service.vtv.IFcmService;
import hcmute.kltn.vtv.service.vtv.INotificationService;
import hcmute.kltn.vtv.util.exception.InternalServerErrorException;
import hcmute.kltn.vtv.util.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements INotificationService {

    private final NotificationRepository notificationRepository;
    private final IFcmService fcmService;



    @Override
    @Async
    @Transactional
    public void addNewNotification(NotificationRequest request) {
        Notification notification = createNotificationByRequest(request);

        try {
            notificationRepository.save(notification);
            fcmService.sendNotification(notification);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    @Async
    @Transactional
    public void addNewNotification(String title, String body, String recipient, String sender, String type) {
        NotificationRequest notificationRequest = NotificationRequest.notificationRequest(title, body, recipient, sender, type);

        try {
            addNewNotification(notificationRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }





    @Override
    public NotificationResponse getNotificationsByUsername(String username, int page, int size) {
        Page<Notification> notifications = getNotifications(username, page, size);
        return NotificationResponse.notificationResponse(notifications, "Lấy danh sách thông báo thành công.");
    }


    @Override
    @Transactional
    public NotificationResponse deleteNotificationById(UUID notificationId, String username) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy thông báo nào"));

        try {
            notificationRepository.delete(notification);

            return NotificationResponse.notificationResponse(getNotifications(username, 1, 20), "Xóa thông báo thành công.");
        } catch (Exception e) {
            throw new InternalServerErrorException("Lỗi khi xóa thông báo");
        }
    }


    @Override
    @Transactional
    public NotificationResponse readNotification(UUID notificationId, String username) {
        Notification notification = getNotificationByIdAndRecipient(notificationId, username);

        try {
            notification.setSeen(true);
            notificationRepository.save(notification);

            return NotificationResponse.notificationResponse(getNotifications(username, 1, 20), "Đọc thông báo thành công.");
        } catch (Exception e) {
            throw new InternalServerErrorException("Lỗi khi đọc thông báo");
        }
    }


    private Notification getNotificationByIdAndRecipient(UUID notificationId, String username) {
        return notificationRepository.findByNotificationIdAndRecipient(notificationId, username)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy thông báo theo id và người nhận"));
    }




    private Page<Notification> getNotifications(String username, int page, int size) {
        return notificationRepository
                .findByRecipientOrderByCreateAtDesc(username, PageRequest.of(page - 1, size))
                .orElseThrow(() -> new NotFoundException("Không tìm thấy thông báo nào"));
    }




    private Notification createNotificationByRequest(NotificationRequest request) {
        Notification notification = new Notification();
        notification.setTitle(request.getTitle());
        notification.setBody(request.getBody());
        notification.setRecipient(request.getRecipient());
        notification.setSender(request.getSender());
        notification.setType(request.getType());
        notification.setSeen(false);
        notification.setCreateAt(LocalDateTime.now());

        return notification;
    }





}
