package hcmute.kltn.vtv.service.vtv.impl;

import com.google.firebase.messaging.*;
import hcmute.kltn.vtv.model.data.user.request.NotificationRequest;
import hcmute.kltn.vtv.model.entity.user.Notification;
import hcmute.kltn.vtv.model.entity.user.FcmToken;
import hcmute.kltn.vtv.model.entity.user.Token;
import hcmute.kltn.vtv.repository.user.FcmTokenRepository;
import hcmute.kltn.vtv.repository.user.TokenRepository;
import hcmute.kltn.vtv.service.vtv.IFcmService;
import hcmute.kltn.vtv.util.exception.InternalServerErrorException;
import hcmute.kltn.vtv.util.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class FcmServiceImpl implements IFcmService {

    private final FirebaseMessaging firebaseMessaging;
    private final FcmTokenRepository fcmTokenRepository;
    private final TokenRepository tokenRepository;


    @Override
    @Transactional
    public void addNewFcmToken(String fcmToken, String username, UUID refreshTokenId) {
        if (fcmTokenRepository.existsByTokenFcm(fcmToken)) {
            updateFcmToken(fcmToken, username, refreshTokenId);
            return;
        }
        
        FcmToken fcmTokenEntity = new FcmToken();
        fcmTokenEntity.setTokenFcm(fcmToken);
        fcmTokenEntity.setUsername(username);
        fcmTokenEntity.setRefreshTokenId(refreshTokenId);
        try {
            fcmTokenRepository.save(fcmTokenEntity);
        } catch (Exception e) {
            throw new InternalServerErrorException("Lỗi khi thêm firebase cloud messaging token");
        }
    }


    @Transactional
    public void updateFcmToken(String fcmToken, String username, UUID refreshTokenId) {
        FcmToken fcmTokenEntity = fcmTokenRepository.findByTokenFcm(fcmToken)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy fcm token theo fcmToken và username"));

        fcmTokenEntity.setUsername(username);
        fcmTokenEntity.setRefreshTokenId(refreshTokenId);
        try {
            fcmTokenRepository.save(fcmTokenEntity);
        } catch (Exception e) {
            throw new InternalServerErrorException("Lỗi khi cập nhật refresh token cho fcm token");
        }
    }


    @Async
    @Override
    @Transactional
    public void deleteFcmTokenByRefreshTokens(List<Token> tokens) {
        try {
            List<UUID> refreshTokens = tokens.stream().map(Token::getTokenId).toList();
            fcmTokenRepository.deleteAllByRefreshTokenIdIn(refreshTokens);
        } catch (Exception e) {
            throw new InternalServerErrorException("Lỗi khi xóa fcm token");
        }
    }

    @Override
    @Async
    public void sendNotification(Notification notice) {
        List<String> registrationTokens = getFcmTokens(notice.getRecipient());
        MulticastMessage message = createMulticastMessage(notice, registrationTokens);
        try {
            firebaseMessaging.sendMulticast(message);
        } catch (FirebaseMessagingException e) {
            throw new InternalServerErrorException("Lỗi khi gửi nhiều thông báo");
        }
    }


    @Override
    @Async
    public void  sendNotificationNewMessage(String title, String body, String recipient, String sender, String type, UUID roomChatId) {
        NotificationRequest notificationRequest = NotificationRequest.notificationRequest(title, body, recipient, sender, type);
        List<String> registrationTokens = getFcmTokens(notificationRequest.getRecipient());

        MulticastMessage message = createMulticastMessageWithNewMessage(notificationRequest, roomChatId, registrationTokens);
        try {
            firebaseMessaging.sendMulticast(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    @Override
    public void deleteFcmTokenByRefreshToken(String refreshToken) {
        try {
            Token token = tokenRepository.findByToken(refreshToken)
                    .orElseThrow(() -> new NotFoundException("Không tìm thấy token theo refreshToken"));
            fcmTokenRepository.deleteAllByRefreshTokenId(token.getTokenId());
        } catch (Exception e) {
            throw new InternalServerErrorException("Lỗi khi xóa fcm token");
        }
    }


    private List<String> getFcmTokens(String username) {
        List<FcmToken> fcmTokens = fcmTokenRepository.findAllByUsername(username)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy fcm token"));
        return fcmTokens.stream().map(FcmToken::getTokenFcm).toList();
    }


    private com.google.firebase.messaging.Notification createNotification(Notification notice) {
        return com.google.firebase.messaging.Notification.builder()
                .setTitle(notice.getTitle())
                .setBody(notice.getBody())
                .build();
    }


    private com.google.firebase.messaging.Notification createNotificationByNotificationRequest(NotificationRequest notificationRequest) {
        return com.google.firebase.messaging.Notification.builder()
                .setTitle(notificationRequest.getTitle())
                .setBody(notificationRequest.getBody())
                .build();
    }


    private MulticastMessage createMulticastMessage(Notification notice, List<String> registrationTokens) {
        com.google.firebase.messaging.Notification notification = createNotification(notice);
        Map<String, String> data = new HashMap<>();
        // Add data to the map
        data.put("notificationId", notice.getNotificationId().toString());
        data.put("title", notice.getTitle());
        data.put("body", notice.getBody());
        data.put("recipient", notice.getRecipient());
        data.put("sender", notice.getSender());
        data.put("type", notice.getType());

        return MulticastMessage.builder()
                .addAllTokens(registrationTokens)
                .setNotification(notification)
                .putAllData(data)
                .build();
    }


    private MulticastMessage createMulticastMessageWithNewMessage(NotificationRequest notificationRequest, UUID roomChatId, List<String> registrationTokens) {
        com.google.firebase.messaging.Notification notification = createNotificationByNotificationRequest(notificationRequest);
        Map<String, String> data = new HashMap<>();
        // Add data to the map
        data.put("title", notificationRequest.getTitle());
        data.put("body", notificationRequest.getBody());
        data.put("recipient", notificationRequest.getRecipient());
        data.put("sender", notificationRequest.getSender());
        data.put("type", notificationRequest.getType());
        data.put("roomChatId", roomChatId.toString());

        return MulticastMessage.builder()
                .addAllTokens(registrationTokens)
                .setNotification(notification)
                .putAllData(data)
                .build();
    }






}

