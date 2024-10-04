package hcmute.kltn.vtv.controller.user;


import hcmute.kltn.vtv.model.data.shipping.response.ListTransportProviderResponse;
import hcmute.kltn.vtv.model.data.shipping.response.TransportProviderResponse;
import hcmute.kltn.vtv.service.shipping.ITransportProviderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/customer/transport-provider")
public class CustomerTransportProviderController {

    private final ITransportProviderService transportProvider;

    @GetMapping("/list/not-province")
    public ResponseEntity<ListTransportProviderResponse> getListTransportProviderNotProvince() {
        return ResponseEntity.ok(transportProvider.getAllTransportProvidersNotProvince());
    }

    @GetMapping("/list")
    public ResponseEntity<ListTransportProviderResponse> getListTransportProvider() {
        return ResponseEntity.ok(transportProvider.getAllTransportProviders());
    }

    @GetMapping("/detail/{transportProviderId}")
    public ResponseEntity<TransportProviderResponse> getTransportProviderDetail(@PathVariable Long transportProviderId) {
        return ResponseEntity.ok(transportProvider.getTransportProviderById(transportProviderId));
    }

}
