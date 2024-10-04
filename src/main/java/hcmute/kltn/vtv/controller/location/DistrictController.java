package hcmute.kltn.vtv.controller.location;

import hcmute.kltn.vtv.util.exception.BadRequestException;
import hcmute.kltn.vtv.model.data.location.ListDistrictResponse;
import hcmute.kltn.vtv.service.location.IDistrictService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/location/district")
@RequiredArgsConstructor
public class DistrictController {

    private final IDistrictService districtService;

    @GetMapping("/get-all-by-province-code/{provinceCode}")
    public ResponseEntity<ListDistrictResponse> getAllDistrictByProvinceCode(@PathVariable String provinceCode) {
        if (provinceCode == null || provinceCode.isEmpty()) {
            throw new BadRequestException("Mã tỉnh thành phố không được để trống.");
        }
        ListDistrictResponse response = districtService.getAllDistrictByProvinceCode(provinceCode);
        return ResponseEntity.ok(response);
    }
}
