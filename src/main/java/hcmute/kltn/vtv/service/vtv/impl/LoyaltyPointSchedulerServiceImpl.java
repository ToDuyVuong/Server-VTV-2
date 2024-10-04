package hcmute.kltn.vtv.service.vtv.impl;


import hcmute.kltn.vtv.model.entity.wallet.LoyaltyPoint;
import hcmute.kltn.vtv.repository.wallet.LoyaltyPointRepository;
import hcmute.kltn.vtv.service.vtv.ILoyaltyPointSchedulerService;
import hcmute.kltn.vtv.service.wallet.ILoyaltyPointHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LoyaltyPointSchedulerServiceImpl implements ILoyaltyPointSchedulerService {

    private final LoyaltyPointRepository loyaltyPointRepository;
    private final ILoyaltyPointHistoryService loyaltyPointHistoryService;



    @Override
    @Transactional
    public void resetLoyaltyPointBeforeDate(LocalDateTime dateTime) {
        List<LoyaltyPoint> loyaltyPoints = loyaltyPointRepository.findAllByUpdateAtBefore(dateTime)
                .orElse(Collections.emptyList());



        if (!loyaltyPoints.isEmpty()) {
            for (LoyaltyPoint loyaltyPoint : loyaltyPoints) {
                if (loyaltyPoint.getTotalPoint() == 0) {
                    continue;
                }
                loyaltyPointHistoryService.addNewLoyaltyPointHistory(loyaltyPoint, -loyaltyPoint.getTotalPoint(), "RESET_POINT");
                loyaltyPoint.setTotalPoint(0L);
                loyaltyPoint.setUpdateAt(LocalDateTime.now());
                loyaltyPointRepository.save(loyaltyPoint);
            }
        }
    }




}
