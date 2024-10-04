package hcmute.kltn.vtv.service.location.impl;

import hcmute.kltn.vtv.repository.location.AdministrativeUnitRepository;
import hcmute.kltn.vtv.service.location.IAdministrativeUnitService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdministrativeUnitServiceImpl implements IAdministrativeUnitService {
    private final AdministrativeUnitRepository administrativeUnitRepository;

}
