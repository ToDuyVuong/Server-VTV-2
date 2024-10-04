package hcmute.kltn.vtv.service.location;

import hcmute.kltn.vtv.model.data.location.ListDistrictResponse;
import hcmute.kltn.vtv.model.entity.location.District;

public interface IDistrictService {
    ListDistrictResponse getAllDistrictByProvinceCode(String provinceCode);

    District getDistrictByCode(String districtCode);
}
