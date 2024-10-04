package hcmute.kltn.vtv.model.entity.shipping;

import hcmute.kltn.vtv.model.entity.location.Province;
import hcmute.kltn.vtv.model.entity.user.Customer;
import hcmute.kltn.vtv.model.extra.Status;
import hcmute.kltn.vtv.model.extra.TransportStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class TransportProvider {

    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transportProviderId;

    @Column(nullable = false, unique = true)
    private String fullName;

    private String shortName;

    private String email;

    private String phone;

    private String usernameAdded;

    @Enumerated(EnumType.STRING)
    private Status status;

    private LocalDateTime createAt;

    private LocalDateTime updateAt;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;


    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "transport_provider_province",
            joinColumns = @JoinColumn(name = "transport_provider_id"),
            inverseJoinColumns = @JoinColumn(name = "province_code")
    )
    private List<Province> provinces;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "fee_shipping_id")
    private FeeShipping feeShipping;

}