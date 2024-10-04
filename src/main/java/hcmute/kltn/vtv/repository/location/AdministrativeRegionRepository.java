package hcmute.kltn.vtv.repository.location;

import hcmute.kltn.vtv.model.entity.location.AdministrativeRegion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdministrativeRegionRepository extends JpaRepository<AdministrativeRegion, Integer> {
    // Additional custom queries can be added here if needed
}
