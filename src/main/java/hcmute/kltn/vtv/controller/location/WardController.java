package hcmute.kltn.vtv.controller.location;

import hcmute.kltn.vtv.model.data.location.LocationResponse;
import hcmute.kltn.vtv.util.exception.BadRequestException;
import hcmute.kltn.vtv.model.data.location.ListWardResponse;
import hcmute.kltn.vtv.service.location.IWardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/location/ward")
@RequiredArgsConstructor
public class WardController {

    private final IWardService wardService;

    @GetMapping("/get-all-by-district-code/{districtCode}")
    public ResponseEntity<ListWardResponse> getAllWardByDistrictCode(@PathVariable String districtCode) {
        if (districtCode == null || districtCode.isEmpty()) {
            throw new BadRequestException("Mã quận huyện không được để trống.");
        }
        ListWardResponse response = wardService.getAllWardByDistrictCode(districtCode);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/full-address/{wardCode}")
    public ResponseEntity<LocationResponse> getFullAddressByWardCode(@PathVariable String wardCode) {
        if (wardCode == null || wardCode.isEmpty()) {
            throw new BadRequestException("Mã phường xã không được để trống.");
        }
        LocationResponse response = wardService.getLocationByWardCode(wardCode);
        return ResponseEntity.ok(response);
    }
}
