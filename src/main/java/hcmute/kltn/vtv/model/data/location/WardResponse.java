package hcmute.kltn.vtv.model.data.location;

import hcmute.kltn.vtv.model.dto.location.WardDTO;
import hcmute.kltn.vtv.model.extra.ResponseAbstract;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class WardResponse extends ResponseAbstract {

    WardDTO wardDTO;
}
