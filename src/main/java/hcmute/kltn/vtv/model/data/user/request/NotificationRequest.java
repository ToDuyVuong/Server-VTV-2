package hcmute.kltn.vtv.model.data.user.request;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class NotificationRequest {

    private String title;

    private String body;

    private String recipient;

    private String sender;

    private String type;


    public static NotificationRequest notificationRequest(String title, String body, String recipient, String sender, String type) {
        NotificationRequest notificationRequest = new NotificationRequest();
        notificationRequest.setTitle(title);
        notificationRequest.setBody(body);
        notificationRequest.setRecipient(recipient);
        notificationRequest.setSender(sender);
        notificationRequest.setType(type);
        return notificationRequest;
    }
}
