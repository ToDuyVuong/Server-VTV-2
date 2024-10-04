package hcmute.kltn.vtv.service.location.impl;

import hcmute.kltn.vtv.util.exception.BadRequestException;
import hcmute.kltn.vtv.model.data.location.ListDistrictResponse;
import hcmute.kltn.vtv.model.dto.location.DistrictDTO;
import hcmute.kltn.vtv.model.entity.location.District;
import hcmute.kltn.vtv.repository.location.DistrictRepository;
import hcmute.kltn.vtv.service.location.IDistrictService;
import hcmute.kltn.vtv.util.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DistrictServiceImpl implements IDistrictService {
    private final DistrictRepository districtRepository;

    @Override
    public ListDistrictResponse getAllDistrictByProvinceCode(String provinceCode) {
        List<District> districts = districtRepository.findAllByProvince_ProvinceCode(provinceCode)
                .orElseThrow(() -> new BadRequestException(
                        "Không tìm thấy quận huyện nào có mã tỉnh thành phố là " + provinceCode));

        List<DistrictDTO> districtDTOs = new ArrayList<>();
        if (!districts.isEmpty()) {
            districtDTOs = DistrictDTO.convertEntitiesToDTOs(districts);
        }
        ListDistrictResponse response = new ListDistrictResponse();
        response.setDistrictDTOs(districtDTOs);
        response.setCount(response.getDistrictDTOs().size());
        response.setProvinceCode(provinceCode);
        response.setCode(200);
        response.setMessage("Lấy danh sách quận huyện thành công.");
        response.setStatus("OK");
        return response;
    }

    @Override
    public District getDistrictByCode(String districtCode) {
        District district = districtRepository.findByDistrictCode(districtCode)
                .orElseThrow(() -> new NotFoundException(
                        "Không tìm thấy quận huyện nào có mã là " + districtCode));
        return district;
    }

}
