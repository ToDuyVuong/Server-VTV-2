package hcmute.kltn.vtv.controller.manager;


import hcmute.kltn.vtv.model.data.manager.request.FeeShippingRequest;
import hcmute.kltn.vtv.model.data.manager.request.TransportProviderRegisterRequest;
import hcmute.kltn.vtv.model.data.manager.request.UpdateTransportProviderWithProvincesRequest;
import hcmute.kltn.vtv.model.data.shipping.response.ListTransportProviderResponse;
import hcmute.kltn.vtv.model.data.shipping.response.TransportProviderResponse;
import hcmute.kltn.vtv.service.manager.IManagerTransportProvider;
import hcmute.kltn.vtv.service.shipping.ITransportProviderService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/manager/transport-provider")
public class ManagerTransportProviderController {

    private final ITransportProviderService transportProvider;
    private final IManagerTransportProvider managerTransportProvider;

    @PostMapping("/add")
    public ResponseEntity<TransportProviderResponse> addNewTransportProvider(@RequestBody TransportProviderRegisterRequest request,
                                                                             HttpServletRequest servletRequest) {
        String usernameAdded = (String) servletRequest.getAttribute("username");
        request.setUsernameAdded(usernameAdded);
        request.validate();

        return ResponseEntity.ok(managerTransportProvider.addNewTransportProvider(request));
    }


    @PutMapping("/update")
    public ResponseEntity<TransportProviderResponse> updateTransportProvider(@RequestBody UpdateTransportProviderWithProvincesRequest request,
                                                                             HttpServletRequest servletRequest) {
        String username = (String) servletRequest.getAttribute("username");

        request.setUsernameAdded(username);
        request.validate();
        return ResponseEntity.ok(managerTransportProvider.updateTransportProviderWithProvinces(request));
    }

    @PutMapping("/update-fee-shipping/{transportProviderId}")
    public ResponseEntity<TransportProviderResponse> updateTransportProviderFeeShipping(@RequestBody FeeShippingRequest request,
                                                                             @PathVariable Long transportProviderId,
                                                                             HttpServletRequest servletRequest) {
        String username = (String) servletRequest.getAttribute("username");

        request.validate();
        return ResponseEntity.ok(managerTransportProvider.updateTransportProviderFeeShipping(username, transportProviderId, request));
    }


    @GetMapping("/all")
    public ResponseEntity<ListTransportProviderResponse> getAllTransportProviders() {
        return ResponseEntity.ok(managerTransportProvider.getAllTransportProviders());
    }


    @GetMapping("/detail/{transportProviderId}")
    public ResponseEntity<TransportProviderResponse> getTransportProviderDetail(@PathVariable Long transportProviderId) {
        return ResponseEntity.ok(transportProvider.getTransportProviderById(transportProviderId));
    }

}
