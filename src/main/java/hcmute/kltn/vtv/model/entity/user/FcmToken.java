package hcmute.kltn.vtv.model.entity.user;


import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "fcm_token")
public class FcmToken {

    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID fcmTokenId;

    private String tokenFcm;

    private String username;

    private UUID refreshTokenId;

}
