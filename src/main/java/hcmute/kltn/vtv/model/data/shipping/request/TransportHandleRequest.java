package hcmute.kltn.vtv.model.data.shipping.request;

import hcmute.kltn.vtv.model.extra.TransportStatus;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class TransportHandleRequest {

    private UUID transportId;
    private String username;
    private String wardCode;
    private boolean active;
    private boolean handled;
    private TransportStatus transportStatus;

    public static TransportHandleRequest createTransportHandleRequest(UUID transportId, String username, String wardCode, boolean active, boolean handled, TransportStatus transportStatus) {
        TransportHandleRequest transportHandleRequest = new TransportHandleRequest();
        transportHandleRequest.setTransportId(transportId);
        transportHandleRequest.setUsername(username);
        transportHandleRequest.setWardCode(wardCode);
        transportHandleRequest.setActive(active);
        transportHandleRequest.setHandled(handled);
        transportHandleRequest.setTransportStatus(transportStatus);
        return transportHandleRequest;
    }

}
