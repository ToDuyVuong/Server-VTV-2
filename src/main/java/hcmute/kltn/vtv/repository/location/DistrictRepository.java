package hcmute.kltn.vtv.repository.location;

import hcmute.kltn.vtv.model.entity.location.District;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DistrictRepository extends JpaRepository<District, String> {
    // Additional custom queries can be added here if needed
    Optional<List<District>> findAllByProvince_ProvinceCode(String provinceCode);

    Optional<List<District>> findAllByAdministrativeUnit_AdministrativeUnitId(Integer administrativeUnitId);

    Optional<District> findByDistrictCode(String districtCode);

    boolean existsByDistrictCode(String districtCode);

}