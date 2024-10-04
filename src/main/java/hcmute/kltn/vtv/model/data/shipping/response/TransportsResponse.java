package hcmute.kltn.vtv.model.data.shipping.response;

import hcmute.kltn.vtv.model.dto.shipping.TransportDTO;
import hcmute.kltn.vtv.model.entity.shipping.Transport;
import hcmute.kltn.vtv.model.extra.ResponseAbstract;
import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TransportsResponse extends ResponseAbstract {
    private int count;
    private List<TransportDTO> transportDTOs;



    public static TransportsResponse transportsResponse(List<Transport> transports, String message, String status) {
        TransportsResponse response = new TransportsResponse();
        response.setTransportDTOs(TransportDTO.convertEntitiesToDTOs(transports));
        response.setMessage(message);
        response.setStatus(status);
        response.setCode(200);
        return response;
    }
}
