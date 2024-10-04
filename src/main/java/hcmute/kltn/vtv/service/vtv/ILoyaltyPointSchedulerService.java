package hcmute.kltn.vtv.service.vtv;

import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

public interface ILoyaltyPointSchedulerService {
    @Transactional
    void resetLoyaltyPointBeforeDate(LocalDateTime dateTime);
}
