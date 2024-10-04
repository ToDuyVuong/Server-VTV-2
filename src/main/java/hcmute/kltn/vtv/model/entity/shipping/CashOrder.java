package hcmute.kltn.vtv.model.entity.shipping;


import hcmute.kltn.vtv.model.extra.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class CashOrder {
    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID cashOrderId;

    private UUID transportId;

    private UUID orderId;

    private Long money;

    private String shipperUsername;

    // shipper có thể giữ tiền hay không
    private boolean shipperHold;

    private String waveHouseUsername;

    // kho đã nhận tiền hay chưa
    private boolean waveHouseHold;

    // thể hiện gửi tiền cho cửa hàng hay chưa
    private boolean handlePayment;

    @Enumerated(EnumType.STRING)
    private Status status;

    private LocalDateTime createAt;

    private LocalDateTime updateAt;
}
