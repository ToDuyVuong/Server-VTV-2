package hcmute.kltn.vtv.model.data.shipping.response;

import hcmute.kltn.vtv.model.dto.shipping.TransportDTO;
import hcmute.kltn.vtv.model.entity.shipping.Transport;
import hcmute.kltn.vtv.model.extra.ResponseAbstract;
import lombok.*;
import org.springframework.data.domain.Page;

import java.util.Date;
import java.util.List;


@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TransportPageResponse extends ResponseAbstract {
    private int count;
    private int page;
    private int size;
    private int totalPage;
    private List<TransportDTO> transportDTOs;


    public static TransportPageResponse transportPageResponse(Page<Transport> transports, String message) {
        TransportPageResponse response = new TransportPageResponse();
        response.setTransportDTOs(TransportDTO.convertEntitiesToDTOs(transports.getContent()));
        response.setCount(transports.getNumberOfElements());
        response.setSize(transports.getSize());
        response.setPage(transports.getNumber() + 1);
        response.setTotalPage(transports.getTotalPages());
        response.setMessage(message);
        response.setStatus("OK");
        response.setCode(200);

        return response;
    }

}
