package hcmute.kltn.vtv.service.vtv;

import org.springframework.transaction.annotation.Transactional;

public interface ICashOrderSchedulerService {
    @Transactional
    void autoHandlePaymentCashOrderAfterFiveDayCompleted();
}
