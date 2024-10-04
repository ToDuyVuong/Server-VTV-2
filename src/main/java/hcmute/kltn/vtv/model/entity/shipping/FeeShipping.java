package hcmute.kltn.vtv.model.entity.shipping;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class FeeShipping {

    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long feeShippingId;

    private Long zeroArea;

    private int zeroEstimatedDeliveryTime;

    private Long oneArea;

    private int oneEstimatedDeliveryTime;

    private Long twoArea;

    private int twoEstimatedDeliveryTime;

    private Long threeArea;

    private int threeEstimatedDeliveryTime;

    private Long fourArea;

    private int fourEstimatedDeliveryTime;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transport_provider_id")
    private TransportProvider transportProvider;


}
