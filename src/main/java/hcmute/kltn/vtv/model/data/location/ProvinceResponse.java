package hcmute.kltn.vtv.model.data.location;

import hcmute.kltn.vtv.model.dto.location.ProvinceDTO;
import hcmute.kltn.vtv.model.extra.ResponseAbstract;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ProvinceResponse extends ResponseAbstract {

        private ProvinceDTO provinceDTO;

}
