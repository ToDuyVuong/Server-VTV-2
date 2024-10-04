package hcmute.kltn.vtv.model.data.location;

import hcmute.kltn.vtv.model.dto.location.DistrictDTO;
import hcmute.kltn.vtv.model.extra.ResponseAbstract;
import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ListDistrictResponse extends ResponseAbstract {

    private int count;
    private String provinceCode;
    private List<DistrictDTO> districtDTOs;
}
