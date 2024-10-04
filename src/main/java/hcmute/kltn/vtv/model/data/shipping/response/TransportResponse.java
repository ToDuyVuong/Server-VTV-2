package hcmute.kltn.vtv.model.data.shipping.response;

import hcmute.kltn.vtv.model.dto.shipping.TransportDTO;
import hcmute.kltn.vtv.model.entity.shipping.Transport;
import hcmute.kltn.vtv.model.extra.ResponseAbstract;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TransportResponse  extends ResponseAbstract {

    private TransportDTO transportDTO;



    public static TransportResponse transportResponse(Transport transport, String message, String status) {
        TransportResponse response = new TransportResponse();
        response.setTransportDTO(TransportDTO.convertEntityToDTO(transport));
        response.setMessage(message);
        response.setStatus(status);
        response.setCode(200);
        return response;
    }

}
