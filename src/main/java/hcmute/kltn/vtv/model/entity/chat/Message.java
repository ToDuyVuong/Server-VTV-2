package hcmute.kltn.vtv.model.entity.chat;

import hcmute.kltn.vtv.model.entity.user.Customer;
import hcmute.kltn.vtv.model.extra.Status;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Message {

    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID messengerId;

    @Column(columnDefinition = "TEXT")
    private String content;

    private String senderUsername;

    @Enumerated(EnumType.STRING)
    private Status status;

    private Date date;

    private boolean usernameSenderDelete;

    private boolean usernameReceiverDelete;

    /*
     * private String time;
     * 
     * @PrePersist
     * public void prePersist() {
     * // Set the time property with the current timestamp when a new entity is
     * persisted.
     * this.time = LocalDateTime.now().toString();
     * }
     */

//    @ManyToOne
//    @JoinColumn(name = "customer_id")
//    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "roomchat_id")
    private RoomChat roomChat;

}
