package hcmute.kltn.vtv.repository.location;

import hcmute.kltn.vtv.model.entity.location.AdministrativeUnit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdministrativeUnitRepository extends JpaRepository<AdministrativeUnit, Integer> {
    // Additional custom queries can be added here if needed

    Optional<AdministrativeUnit> findByAdministrativeUnitId(Integer administrativeUnitId);

}
