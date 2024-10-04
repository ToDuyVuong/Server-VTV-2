package hcmute.kltn.vtv.service.shipping;

import hcmute.kltn.vtv.model.data.shipping.response.TransportPageResponse;
import hcmute.kltn.vtv.model.extra.TransportStatus;

import java.util.Date;

public interface IManagerTransport {
    TransportPageResponse getTransportPageByManagerAndDate(String username, int page, int size,
                                                           TransportStatus status, Date startDate, Date endDate);
}
