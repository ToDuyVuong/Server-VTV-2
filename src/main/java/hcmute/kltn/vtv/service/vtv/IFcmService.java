package hcmute.kltn.vtv.service.vtv;

import hcmute.kltn.vtv.model.entity.user.Notification;
import hcmute.kltn.vtv.model.entity.user.Token;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

public interface IFcmService {

    @Transactional

    void addNewFcmToken(String fcmToken, String username, UUID refreshTokenId);

    @Async
    @Transactional
    void deleteFcmTokenByRefreshTokens(List<Token> tokens);

    @Async
    void sendNotification(Notification notice);


    @Async
    void  sendNotificationNewMessage(String title, String body, String recipient, String sender, String type, UUID roomChatId);

    void deleteFcmTokenByRefreshToken(String refreshToken);
}
