package hcmute.kltn.vtv.service.shipping;

import java.time.LocalDateTime;
import java.util.Date;

public interface IShippingTimeEstimationService {
    Date estimateDeliveryTime(int distance, String shippingProvider);

    Date estimateDeliveryTimeByOrderDate(int distance, String shippingProvider, LocalDateTime orderDate);
}
