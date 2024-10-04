package hcmute.kltn.vtv.model.entity.shipping;

import hcmute.kltn.vtv.model.extra.TransportStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Transport {
    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID transportId;

    @Column(unique = true)
    private UUID orderId;

    private Long shopId;

    private String wardCodeShop;

    private String wardCodeCustomer;

    private String shippingMethod;

    private LocalDateTime createAt;

    private LocalDateTime updateAt;

    @Enumerated(EnumType.STRING)
    private TransportStatus status;

    @OneToMany(mappedBy = "transport", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<TransportHandle> transportHandles;
}
