package hcmute.kltn.vtv.repository.chat;

import hcmute.kltn.vtv.model.entity.chat.RoomChat;
import hcmute.kltn.vtv.model.extra.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;


@Repository
public interface RoomChatRepository extends JpaRepository<RoomChat, UUID> {

    boolean existsBySenderUsernameAndReceiverUsername(String senderUsername, String receiverUsername);


    Optional<RoomChat> findBySenderUsernameAndReceiverUsername(String senderUsername, String receiverUsername);


    Optional<Page<RoomChat>> findAllBySenderUsernameOrReceiverUsernameAndStatus(String senderUsername, String receiverUsername, Status status, Pageable pageable);

    Optional<Page<RoomChat>> findAllBySenderUsernameOrReceiverUsername(String senderUsername, String receiverUsername, Pageable pageable);

}
